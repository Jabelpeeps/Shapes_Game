package org.jabelpeeps.jabeltris;

import java.util.HashSet;
import java.util.Set;

import org.jabelpeeps.jabeltris.Core.LogicInputVisitor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class GameLogic extends Thread implements Serializable {
	
	private PlayArea game;
	@SuppressWarnings("unused")
	private LevelMaster level;
	private LogicInputVisitor visitor;
	private boolean visitorAccepted = false;
	private boolean backKeyWasPressed = false;
	private boolean startSignalSet = true;
	private boolean loopIsEnding = false;
	private boolean reloading = false;
	protected Array<GameMechanic> mechanics = new Array<GameMechanic>(GameMechanic.class);
	private Set<Shape> combinedHintList = new HashSet<Shape>(32);
	public boolean endlessPlayMode = false;
	public boolean inDemoMode = false;
	protected Select select = new Select();
	
	public GameLogic(PlayArea g, LevelMaster l) {		
		game = g;
		level = l;
		select.activateShapeSelector( game , this ); 
		this.setDaemon( true );
	}
	@Override
	public void run() {
		
		if ( !reloading ) {
			activateAllMechanics();
			game.fillBoard();
			game.setPlayAreaReady();
		
			if ( inDemoMode ) 
				game.spinShapesIntoPlace();
			else 
				game.swirlShapesIntoPlace();
		}
		while ( !startSignalSet ) 
			Core.delay( 200 );
		
		while ( !loopIsEnding ) {

			synchronized( this ) {
				if ( inDemoMode ) {
					takeBestMoveFound();
					while ( game.boardHasMatches( 150 ) ) 
						game.replaceMatchedShapes();
					game.findHintsInAllshape();
				} 
				else if ( visitorAccepted ) {
					visitor.greet();
					visitor = null;
					visitorAccepted = false;
				}
				if ( getTotalHints() <= 0 && ( endlessPlayMode || inDemoMode ) ) {
					Core.delay( 100 );
					game.shuffleBoard();
				}
			}
			Core.delay( 80 );
		}
		return;    								
	}
	
	void takeBestMoveFound() {
		int bestMatches = 0;
		GameMechanic bestMover = null;
		
		for ( GameMechanic each : mechanics ) {
			int matchesFound = each.searchForMoves();
			
			if ( matchesFound > bestMatches 
					|| ( matchesFound == bestMatches 
					&& Core.rand.nextBoolean() ) ) {
				bestMatches = matchesFound;
				bestMover = each;
			} 
		}
		long time = (long) ( ( Core.rand.nextGaussian() + 2.5 ) * 150 );
		Core.delay( time );
		
		bestMover.takeMove();	
		clearAllHints();
	}
	void clearAllHints() {
		combinedHintList.clear();
		
		for ( GameMechanic each : mechanics )
			each.clearHints();
	}
	public int getTotalHints() {
		if ( combinedHintList.size() < 1 )
			combineHintLists();
		return combinedHintList.size();
	}	
	private void combineHintLists() {
		for ( GameMechanic each : mechanics ) 
			combinedHintList.addAll( each.getHintList() );
	}
	Shape getHint() {
		getTotalHints();
		return (Shape) combinedHintList.toArray()[ Core.rand.nextInt( combinedHintList.size() - 1 ) ];
	}
	
	public void addGameMechanics(GameMechanic...list) {
		for ( GameMechanic each : list )
			addGameMechanic( each );
	}
	public void addGameMechanic(GameMechanic gm) {
		boolean mechanicAlreadyAdded = false;
		for ( GameMechanic each : mechanics ) {
			if ( each.equals( gm ) )
				mechanicAlreadyAdded = true;
		}
		if ( !mechanicAlreadyAdded )
			mechanics.add( gm );
	}
	protected void activateAllMechanics() {
		for ( GameMechanic each : mechanics ) 
			each.activate();
	}
	protected void toggleMechanic(int mech) {
		if ( mech > mechanics.size ) return;
		mechanics.items[mech - 1].toggleActive();
	}
	
	public void acceptVisitor(LogicInputVisitor visitor) {
		visitorAccepted = true;
		this.visitor = visitor;
	}
	public boolean hasVisitor() {
		return visitorAccepted || inDemoMode;
	}
	public void shutDown() {
		loopIsEnding = true;
		startSignalSet = true;
	}
	public void setBackKeyWasPressed(boolean state) {
		backKeyWasPressed = state;
	}
	public boolean getBackKeyWasPressed() {
		return backKeyWasPressed;
	}
	public void waitForStartSignal() {
		startSignalSet = false;
	}
	public void sendStartSignal() {
		startSignalSet = true;
	}
	@Override
	public void write(Json json) {
			json.writeValue("endlessPlayMode", endlessPlayMode);
			json.writeValue("inDemoMode", inDemoMode);
			
			json.writeArrayStart("mechanics");
			for ( GameMechanic each : mechanics ) {
				json.writeObjectStart();
				json.writeValue("name", each.getClass().getSimpleName());
				json.writeValue("active", each.active);
				json.writeObjectEnd();
			}
			json.writeArrayEnd();
	}
	@Override
	public void read(Json json, JsonValue jsonData) {
		inDemoMode = jsonData.getBoolean("inDemoMode");
		endlessPlayMode = jsonData.getBoolean("endlessPlayMode");
		reloading = true;
		try {
			JsonValue mechs = jsonData.get("mechanics");
			
			for ( JsonValue each = mechs.child; each != null; each = each.next ) {
				Class<?> mechanicclass = ClassReflection.forName(Core.PACKAGE + each.getString("name") );
				Constructor mechanicconstructor = ClassReflection.getConstructor(mechanicclass, PlayArea.class, GameLogic.class);
				
				mechanics.add( (GameMechanic) mechanicconstructor.newInstance(game, this) );
				
				if ( each.getBoolean("active") ) 
					mechanics.peek().activate();
			}
		} catch (ReflectionException e) { e.printStackTrace(); }
	}
}
