package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.Triangle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TrainingLevel2 extends TrainingLevelAbstract {

	public Sprite tinyTri = new Sprite(LevelMaster.triangle);
	private int[][] triPtDown  = new int[][]{{-2,0}, {0,0}, {+2,0}, {0,-2}};
	private int[][] triPtUp    = new int[][]{{-2,0}, {0,0}, {+2,0}, {0,+2}}; 
	private int[][] triPtLeft  = new int[][]{{0,+2}, {0,0}, {0,-2}, {-2,0}}; 
	private int[][] triPtRight = new int[][]{{0,+2}, {0,0}, {0,-2}, {+2,0}};  
	
//----------------------------------------------Constructors--------
	public TrainingLevel2() {
		super();
		baseColor = new Color(1f, 1f, 1f, 1f);
		title = "Demo Level 2\nTriangles";
		firstMessage = title + "\n\n"
				+ "Triangles match when placed in T-shaped groups.";
	}
	public TrainingLevel2(boolean playNext) {	
		this();
		playOn = playNext;
		game = new PlayArea(6, 6);
		game.initialise(this);
		logic = new DemoGameLogic(game);
		logic.waitForStartSignal();
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}
	public TrainingLevel2(boolean playNext, boolean learningLevels) {
		this(playNext);
		playingLearningLevels = learningLevels;
	}
//----------------------------------------------Methods----------
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(4) + 1 ) {
			case 1: 
				return new Triangle("Red");
			case 2: 
				return new Triangle("Blue");
			case 3: 
				return new Triangle("Green");	
			case 4:
				return new Triangle("Yellow");
		}
		return null;
	}
	
	private void drawDemoMatches() {
		tinyTri.setSize(2, 2);
		tinyTri.setAlpha(0.8f);
		for ( int[] each : triPtDown ) {
			tinyTri.setPosition(3+each[0], 12+each[1]);
			tinyTri.draw(batch);
		}
		for ( int[] each : triPtRight ) {
			tinyTri.setPosition(3+each[0], 20+each[1]);
			tinyTri.draw(batch);
		}
		for ( int[] each : triPtUp ) {
			tinyTri.setPosition(36+each[0], 12+each[1]);
			tinyTri.draw(batch);
		}
		for ( int[] each : triPtLeft ) {
			tinyTri.setPosition(36+each[0], 20+each[1]);
			tinyTri.draw(batch);
		}				
	}
	@Override
	protected void stage1Tasks() {
		Core.textCentre("<-Like these->", 18);
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
			Core.textCentre("I'll leave the examples showing for a little while...", 4);
		} else if ( score < 11 ) {
			drawDemoMatches();
			Core.textCentre("[GOLD]Pro Tip[]:- strangely shaped groups of "
							+ "triangles may have multiple matches.", 4);
		} else if ( score < 17 ) {
			Core.textCentre("You learn fast!\n"
					+ "I guess you don't need the examples any longer...", 4);
		} else if ( score < 20 ) {
			Core.textCentre("Keep going...", 2);
		} else if ( score > 19 ) {
			levelStage = 7;
			logic.setEndlessPlayMode(false);
		}
	}
	@Override
	protected void nextLearningLevel() {
		core.setScreen( Core.levelCompleted("TrainingLevel3")? new TrainingLevel3(true, true)
															 : new MainMenu(core, 3) );
	}
	@Override
	protected void nextLevel() {
		core.setScreen(new ChallengeLevel1(true));
	}
}
