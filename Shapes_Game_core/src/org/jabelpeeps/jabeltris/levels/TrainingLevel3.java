package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.HorizontalLineBlue;
import org.jabelpeeps.jabeltris.shapes.HorizontalLineRed;
import org.jabelpeeps.jabeltris.shapes.VerticalLineGreen;
import org.jabelpeeps.jabeltris.shapes.VerticalLineYellow;

import com.badlogic.gdx.graphics.Color;

public class TrainingLevel3 extends TrainingLevelAbstract {

// ---------------------------------------------Constructors--------
	public TrainingLevel3() {
		super();
		baseColor = new Color(1f, 1f, 1f, 1f);
		title = "Demo Level 3\nLines";
		firstMessage = title + "\n\n"
					+ "Our Lines match in groups of 3, but only in their "
					+ "preferred direction. (The way the shape "
					+ "is pointing.)";
	}
	public TrainingLevel3(Core c) {
		this();
		core = c;
		game = new PlayArea(8, 8);
		game.initialise(this);
		logic = new DemoGameLogic(game);
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}
	public TrainingLevel3(Core c, boolean playNext) {	
		this(c);
		playOn = playNext;
	}
	public TrainingLevel3(Core c, boolean playNext, boolean learningLevels) {
		this(c, playNext);
		playingLearningLevels = learningLevels;
	}
// ---------------------------------------------Methods----------
	@Override
	public Shape makeNewShape(int x, int y) {
		
		switch ( rand.nextInt(4) + 1 ) {
			case 1:
				return new HorizontalLineRed();
			case 2: 
				return new HorizontalLineBlue();
			case 3: 
				return new VerticalLineYellow();
			case 4:
				return new VerticalLineGreen();
		}
		return null;
	}
		
	@Override
	protected void stage6Tasks(int score) {
		messageCentered("Target:- 20", 40);
		if ( score < 6 ) {
			messageCentered("Touch and drag shapes where you want them to go.", 2);
		} else if ( score < 11 ) {
			messageCentered("[GOLD]Pro Tip[]:- Look for moves that make more than one match.", 4);
		} else if ( score < 16 ) {
			messageCentered("Multiple matches will score [ORANGE]extra points[] in the scoring levels.", 4);
		} else if ( score < 20 ) {
			messageCentered("Almost there...", 2);
		} else if ( score > 19 ) {
			levelStage = 7;
			logic.setEndlessPlayMode(false);
		}
	}
	@Override
	protected void nextLearningLevel() {
		core.setScreen(new TrainingLevel1(core, true, true));
	}
	@Override
	protected void nextLevel() {
	//	core.setScreen(new ChallengeLevel2(core, true));
	}
}
