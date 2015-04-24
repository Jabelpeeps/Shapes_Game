package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.GameLogic;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.TwoSwapMech;
import org.jabelpeeps.jabeltris.shapes.Square;

import com.badlogic.gdx.graphics.Color;

public class TrainingLevel1 extends TrainingLevelAbstract {

// ---------------------------------------------Constructors--------	
	public TrainingLevel1() {
		super();
		title = "Demo Level 1\nSquares";
		firstMessage = title + "\n\n"
				+ "Squares match when placed in square groups.\n\n"
				+ "AutoPlay will show you how this works.";
	}
	public TrainingLevel1(boolean playNext) {	
		this();
		playOn = playNext;
		game = new PlayArea(6, 6);
		game.baseColor = new Color(1f, 1f, 1f, 1f);
		logic = new GameLogic(game, this);
		game.initialise(this, logic);
		logic.addGameMechanic( new TwoSwapMech(game, logic) );
		logic.inDemoMode = true;
		logic.waitForStartSignal();
		setupInput();
		logic.start();
	}
	public TrainingLevel1(boolean playNext, boolean learningLevels) {
		this(playNext);
		playingLearningLevels = learningLevels;
	}
// ---------------------------------------------Methods----------
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(3) + 1 ) {
			case 1:
				return new Square("Blue");
			case 2:
				return new Square("Red");
			case 3:
				return new Square("Yellow");
		}
		return null;
	}	
	@Override
	protected void stage6Tasks(int score) {
		Core.textCentre("Target:- 20", Core.topEdge - 10);
		if ( score < 6 ) 
			Core.textCentre("Touch and drag shapes where you want them to go.", 2);
		else if ( score < 11 )
			Core.textCentre("[GOLD]Pro Tip[]:- Look for moves that make more than one match.", 4);
		else if ( score < 16 )
			Core.textCentre("Multiple matches will score [ORANGE]extra points[] in the scoring levels.", 4);
		else if ( score < 20 )
			Core.textCentre("Almost there...", 2);
		else if ( score > 19 ) {
			levelStage = 7;
			logic.endlessPlayMode = false;
		}
	}
	@Override
	protected void nextLearningLevel() {
		core.setScreen( Core.levelCompleted("TrainingLevel2")? new TrainingLevel2(true, true) 
															 : new MainMenu(core, 3) );
	}
	@Override
	protected void nextLevel(){
		core.setScreen(new TrainingLevel2(true));		
	}	
}
