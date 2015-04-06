package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOne;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TrainingLevel4 extends TrainingLevelAbstract implements Screen {

	public Sprite tinyCrossOne = new Sprite(LevelMaster.invcrone);
	private int[][] crossOneFilled = {{-1, 0}, {0, 0}, {1, 0}, {0, -1}, {0, 1}};
	private int[][] crossOneUnfilled = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}}; 
	private final float SIZE = 2.5f;
	private int[][] blanks = {	{0, 0}, {1, 0}, {0, 1}, {2, 0}, {1, 1}, {0, 2},
								{0, 8}, {0, 7}, {1, 8}, {0, 6}, {1, 7}, {2, 8},
								{8, 0}, {7, 0}, {8, 1}, {6, 0}, {7, 1}, {8, 2},
								{8, 8}, {7, 8}, {8, 7}, {6, 8}, {7, 7}, {8, 6}	};
//----------------------------------------------Constructors--------
	public TrainingLevel4() {
		super();
		title = "Demo Level 4\nCrosses";
		firstMessage = title + "\n\n"
				+ "Crosses match in plus(+) shaped groups.";
	}
	public TrainingLevel4(boolean playNext) {
		this();
		playOn = playNext;
		game = new PlayArea(9, 9);
		game.baseColor = new Color(1f, 1f, 1f, 1f);
		game.initialise(this);
		Shape.addHintVisitor( new StandardMoveHints() );
		logic = new DemoGameLogic(game);
		logic.waitForStartSignal();
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
		while ( !game.playAreaIsReady() ) 
			Core.delay(10);
		game.setBlanks(blanks);
	}
	public TrainingLevel4(boolean playNext, boolean learningLevels) {
		this(playNext);
		playingLearningLevels = learningLevels;
	}
//----------------------------------------------Methods----------
	@Override
	protected Shape getNewShape() {
		
		switch ( rand.nextInt(4) + 1 ) {
		case 1:                       
			return new CrossOne("Blue");
		case 2:                       
			return new CrossOne("Yellow");
		case 3:                       
			return new CrossOne("Green");	
		case 4:
			return new CrossOne("Red");
		}
		return null;
	}
	private void drawDemoMatches() {
		tinyCrossOne.setSize(SIZE, SIZE);
		tinyCrossOne.setAlpha(0.8f);
		for ( int[] each : crossOneUnfilled ) {
			tinyCrossOne.setPosition(3+each[0]*SIZE, 6+each[1]*SIZE);
			tinyCrossOne.draw(batch);
		}
		for ( int[] each : crossOneFilled ) {
			tinyCrossOne.setPosition(35+each[0]*SIZE, 6+each[1]*SIZE);
			tinyCrossOne.draw(batch);
		}			
	}
	@Override
	protected void stage1Tasks() {
		Core.textCentre("<-Like these->", 12);
		drawDemoMatches();
		super.stage1Tasks();
	}	
	@Override
	protected void stage2Tasks() {
		drawDemoMatches();
		super.stage2Tasks();
	}	
	@Override
	protected void stage3Tasks() {
		drawDemoMatches();
		super.stage3Tasks();
	}
	@Override
	protected void stage4Tasks() {
		drawDemoMatches();
		super.stage4Tasks();
	}
	@Override
	protected void stage6Tasks(int score) {
		Core.textCentre("Target:- 20", Core.topEdge - 10);
		if ( score < 6 ) {
			drawDemoMatches();
			Core.textInBounds("I'll leave the examples showing for a little while...", Core.bottomEdge, 0);
		} else if ( score < 11 ) {
			drawDemoMatches();
			Core.textInBounds("[GOLD]Pro Tip[]:- The match on the right scores double.", Core.bottomEdge, 0);
		} else if ( score < 17 ) {
			Core.textInBounds("Still learning fast!\n"
					+ "You don't need the examples now.", Core.bottomEdge, 0);
		} else if ( score < 20 ) {
			Core.textCentre("Keep going...", 0);
		} else if ( score > 19 ) {
			levelStage = 7;
			logic.setEndlessPlayMode(false);
		}
	}

	@Override
	protected void nextLearningLevel() {
		core.setScreen( Core.levelCompleted("TrainingLevel5")? new TrainingLevel5(true, true)
															 : new MainMenu(core, 3) );
	}
	@Override
	protected void nextLevel() {
	}
}
