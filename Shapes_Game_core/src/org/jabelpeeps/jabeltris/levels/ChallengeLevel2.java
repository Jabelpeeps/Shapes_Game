package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOneBlue;
import org.jabelpeeps.jabeltris.shapes.CrossOneGreen;
import org.jabelpeeps.jabeltris.shapes.CrossOneMagenta;
import org.jabelpeeps.jabeltris.shapes.CrossOneRed;
import org.jabelpeeps.jabeltris.shapes.CrossOneYellow;

import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel2 extends ChallengeLevelAbstract {

	private int[][] blanks = {{0, 0},{0, 9},{8, 0},{8, 9}};
	
// ---------------------------------------------Constructors--------
	public ChallengeLevel2() {
		super();
		baseColor = new Color(0.75f, 1f, 1f, 1f);
		title = "Challenge Level 2\n";
		firstMessage = title + "\n\n"
				+ "\n"
				+ "\n"
				+ ""
				+ "";
	}
	public ChallengeLevel2(Core c) {
		this();
		core = c;
		game = new PlayArea(9, 10);
		game.initialise(this);
		logic = new InteractiveGameLogic(game);
		logic.waitForStartSignal();
		logic.setEndlessPlayMode(true);
		logic.start();
		while ( !game.playAreaIsReady() ) {
			Core.delay(10);
		}
		game.setBlanks(blanks);
	}
	public ChallengeLevel2(Core c, boolean playNext) {
		this(c);
		playOn = playNext;
	}
// ---------------------------------------------Methods--------------
	@Override
	public Shape makeNewShape(int x, int y) {
		
		switch ( rand.nextInt(5) + 1 ) {
			case 1:                       
				return new CrossOneBlue();
			case 2:                       
				return new CrossOneYellow();
			case 3:                       
				return new CrossOneGreen();	
			case 4:
				return new CrossOneMagenta();
			case 5:
				return new CrossOneRed();
		}
		return null;
	}
	@Override
	protected void stage3Tasks(int matches) {
		messageCentered("Sets Matched:- " + matches + "/30", 45);
		if ( matches < 6 ) {
			messageCentered("", 2);
		} else if ( matches < 11 ) {
			messageCentered("[#FFD700]Pro Tip[]:- ", -2);
		} else if ( matches < 16 ) {
			messageCentered("", 4);
		} else if ( matches < 20 ) {
			messageCentered("", 2);
		} else if ( matches > 29 ) {
			levelStage = 7;
			logic.setEndlessPlayMode(false);
		}	
	}
	@Override
	protected void nextLevel() {
		core.setScreen(new TrainingLevel3(core));
	}
}
