package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.GameLogic;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.TwoSwapMech;
import org.jabelpeeps.jabeltris.shapes.HorizontalLine;
import org.jabelpeeps.jabeltris.shapes.Square;
import org.jabelpeeps.jabeltris.shapes.Triangle;
import org.jabelpeeps.jabeltris.shapes.VerticalLine;

import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel02 extends ChallengeLevelAbstract {

	private int[][] blanks = {{0, 0},{0, 9},{8, 0},{8, 9}};
	
// ---------------------------------------------Constructors--------
	public ChallengeLevel02() {
		super();
		title = "Challenge Level 2\nRe-Mix";
		firstMessage = title + "\n\n"
				+ "\n"
				+ "\n"
				+ ""
				+ "";
	}
	public ChallengeLevel02(boolean playNext) {
		this();
		playOn = playNext;
		game = new PlayArea( 9 , 10 );
		game.baseColor = new Color( 0.75f, 1f, 1f, 1f );
		logic = new GameLogic( game , this );
		game.initialise( this , logic );
		setBlanks( blanks );
		logic.addGameMechanic( new TwoSwapMech( game , logic ) );
		logic.waitForStartSignal();
		logic.endlessPlayMode = true;
		logic.start();
	}
// ---------------------------------------------Methods--------------
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(5) + 1 ) {
			case 1:                       
				return new Square("Orange");
			case 2:                       
				return new Triangle("Blue");
			case 3:                       
				return new Square("Green");	
			case 4:
				return new HorizontalLine("Magenta");
			case 5:
				return new VerticalLine("Yellow");
		}
		return null;
	}
	@Override
	protected void stage3Tasks(int matches) {
		Core.textCentre("Sets Matched:- " + matches + "/30", Core.topEdge - 10);
		
		if ( matches < 6 ) 
			Core.textCentre("", 2);
		else if ( matches < 11 )
			Core.textCentre("[GOLD]Pro Tip[]:- ", -2);
		else if ( matches < 16 ) 
			Core.textCentre("", 4);
		else if ( matches < 20 ) 
			Core.textCentre("", 2);
		else if ( matches > 29 ) {
			levelStage = 7;
			logic.endlessPlayMode = false;
		}	
	}
	@Override
	protected void nextLevel() {
		core.setScreen( new ChallengeLevel03( true ) );
	}
}
