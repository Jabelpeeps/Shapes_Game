package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.SquareBlue;
import org.jabelpeeps.jabeltris.shapes.SquareRed;
import org.jabelpeeps.jabeltris.shapes.TriangleGreen;
import org.jabelpeeps.jabeltris.shapes.TriangleYellow;

import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel1 extends ChallengeLevelAbstract {
		
// ---------------------------------------------Constructors--------	
	public ChallengeLevel1() {
		super();
		baseColor = new Color(0.75f, 1f, 1f, 1f);
		title = "Challenge Level 1\nFirst Mix!";
		firstMessage = title + "\n\n"
				+ "Ok.\n"
				+ "No demo this time.\n"
				+ "You know these shapes. "
				+ "Your target is 30 matches, and"
				+ " I'll be keeping score too...";
	}
	public ChallengeLevel1(Core c) {
		this();
		core = c;
		game = new PlayArea(7, 7);
		game.initialise(this);
		logic = new InteractiveGameLogic(game);
		logic.waitForStartSignal();
		logic.setEndlessPlayMode(true);
		logic.start();
	}
	public ChallengeLevel1(Core c, boolean playNext) {
		this(c);
		playOn = playNext;
	}
// ---------------------------------------------Methods--------------
	@Override
	public Shape makeNewShape(int x, int y) {
		
		switch ( rand.nextInt(4) + 1 ) {
			case 1:
				return new SquareRed();
			case 2:
				return new SquareBlue();
			case 3: 
				return new TriangleYellow();
			case 4:
				return new TriangleGreen();
		}
		return null;
	}
	@Override
	protected void stage3Tasks(int matches) {
		messageCentered("Sets Matched:- " + matches + "/30", 42);
		if ( matches < 6 ) {
			messageCentered("", 2);
		} else if ( matches < 11 ) {
			messageCentered("[GOLD]Pro Tip[]:- ", 4);
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
