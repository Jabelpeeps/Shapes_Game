package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.GameLogic;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.TwoSwap;
import org.jabelpeeps.jabeltris.shapes.HorizontalLine;
import org.jabelpeeps.jabeltris.shapes.VerticalLine;

import com.badlogic.gdx.graphics.Color;

public class TrainingLevel3 extends TrainingLevelAbstract {

// ---------------------------------------------Constructors--------
	public TrainingLevel3() {
		super();
		title = "Demo Level 3\nLines";
		firstMessage = title + "\n\n"
					+ "These Lines match in groups of 3...\n\n"
					+ "...but only in one direction.";
	}
	public TrainingLevel3(boolean playNext) {	
		this();
		playOn = playNext;
		game = new PlayArea(8, 8);
		game.baseColor = new Color(1f, 1f, 1f, 1f);
		logic = new GameLogic(game, this);
		game.initialise(this, logic);
		logic.addGameMechanic( new TwoSwap(game, logic) );
		logic.inDemoMode = true;
		logic.waitForStartSignal();
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}
	public TrainingLevel3(boolean playNext, boolean learningLevels) {
		this(playNext);
		playingLearningLevels = learningLevels;
	}
// ---------------------------------------------Methods----------
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(4) + 1 ) {
			case 1:
				return new HorizontalLine("Red");
			case 2: 
				return new HorizontalLine("Blue");
			case 3: 
				return new VerticalLine("Yellow");
			case 4:
				return new VerticalLine("Green");
		}
		return null;
	}
		
	@Override
	protected void stage6Tasks(int score) {
		Core.textCentre("Target:- 20", Core.topEdge - 10);
		if ( score < 6 ) 
			Core.textCentre("", 2);
		else if ( score < 11 )
			Core.textCentre("[GOLD]Pro Tip[]:- ", 4);
		else if ( score < 16 )
			Core.textCentre("", 4);
		else if ( score < 20 ) 
			Core.textCentre("Almost there...", 2);
		else if ( score > 19 ) {
			levelStage = 7;
			logic.endlessPlayMode = false;
		}
	}
	@Override
	protected void nextLearningLevel() {
		core.setScreen( Core.levelCompleted("TrainingLevel4")? new TrainingLevel4(true, true) 
															 : new MainMenu(core, 3) );
	}
	@Override
	protected void nextLevel() {
		core.setScreen(new ChallengeLevel2(true));
	}
}
