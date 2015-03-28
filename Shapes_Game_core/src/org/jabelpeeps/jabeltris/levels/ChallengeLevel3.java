package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.MirrorEll;
import org.jabelpeeps.jabeltris.shapes.RealEll;
import org.jabelpeeps.jabeltris.shapes.TriangleRealT;
import org.jabelpeeps.jabeltris.shapes.ZigLeft;
import org.jabelpeeps.jabeltris.shapes.ZigRight;

import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel3 extends ChallengeLevelAbstract {

	private int[][] blanks = {{3, 3},{3, 6},{6, 3},{6, 6}};
	
// ---------------------------------------------Constructors--------	
	public ChallengeLevel3() {
		super();
		baseColor = new Color(0.75f, 1f, 1f, 1f);
		title = "Challenge Level 3\n";
		firstMessage = title + "\n\n"
				+ "\n"
				+ "\n"
				+ ""
				+ "";
	}
	public ChallengeLevel3(boolean playNext) {
		this();
		playOn = playNext;
		game = new PlayArea(10, 10);
		game.initialise(this);
		logic = new InteractiveGameLogic(game);
		logic.waitForStartSignal();
		logic.setEndlessPlayMode(true);
		logic.start();
		while ( !game.playAreaIsReady() )
			Core.delay(10);
		game.setBlanks(blanks);
	}
// ---------------------------------------------Methods--------------
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(5) + 1 ) {
			case 1:                       
				return new MirrorEll("Blue");
			case 2:                        
				return new RealEll("Green");
			case 3:                        
				return new TriangleRealT("Red");				
			case 4:                       
				return new ZigRight("Yellow");
			case 5:                       
				return new ZigLeft("Orange");
		}
		return null;
	}
	@Override
	protected void stage3Tasks(int matches) {
		Core.textCentre("Sets Matched:- " + matches + "/30", Core.topEdge - 10);
		if ( matches < 6 ) 
			Core.textCentre("", 2);
		else if ( matches < 11 ) 
			Core.textCentre("[#FFD700]Pro Tip[]:- ", -2);
		else if ( matches < 16 )
			Core.textCentre("", 4);
		else if ( matches < 20 )
			Core.textCentre("", 2);
		else if ( matches > 29 ) {
			levelStage = 7;
			logic.setEndlessPlayMode(false);
		}	
	}
	@Override
	protected void nextLevel() {
		core.setScreen(new TrainingLevel3(true));
	}
}
