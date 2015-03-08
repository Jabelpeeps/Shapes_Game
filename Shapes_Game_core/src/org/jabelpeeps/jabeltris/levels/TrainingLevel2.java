package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.TriangleBlue;
import org.jabelpeeps.jabeltris.shapes.TriangleGreen;
import org.jabelpeeps.jabeltris.shapes.TriangleRed;
import org.jabelpeeps.jabeltris.shapes.TriangleYellow;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TrainingLevel2 extends TrainingLevelAbstract {

	public Sprite tinyTri = new Sprite(LevelMaster.triangle);
	private Integer[][] triPtDown  = new Integer[][]{{-2,0}, {0,0}, {+2,0}, {0,-2}};
	private Integer[][] triPtUp    = new Integer[][]{{-2,0}, {0,0}, {+2,0}, {0,+2}}; 
	private Integer[][] triPtLeft  = new Integer[][]{{0,+2}, {0,0}, {0,-2}, {-2,0}}; 
	private Integer[][] triPtRight = new Integer[][]{{0,+2}, {0,0}, {0,-2}, {+2,0}};  
	
// ---------------------------------------------Constructors--------
	public TrainingLevel2() {
		super();
		baseColor = new Color(1f, 1f, 1f, 1f);
		title = "Demo Level 2\nTriangles";
		firstMessage = title + "\n\n"
				+ "Triangles match when placed in T-shaped groups.";
	}
	public TrainingLevel2(Core c) {
		this();
		core = c;
		game = new PlayArea(6, 6);
		game.initialise(this);
		logic = new DemoGameLogic(game);
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}
	public TrainingLevel2(Core c, boolean playNext) {	
		this(c);
		playOn = playNext;
	}
	public TrainingLevel2(Core c, boolean playNext, boolean learningLevels) {
		this(c, playNext);
		playingLearningLevels = learningLevels;
	}
// ---------------------------------------------Methods----------
	@Override
	public Shape makeNewShape(int x, int y) {
		
		switch ( rand.nextInt(4) + 1 ) {
			case 1: 
				return new TriangleRed();
			case 2: 
				return new TriangleBlue();
			case 3: 
				return new TriangleGreen();	
			case 4:
				return new TriangleYellow();
		}
		return null;
	}
	
	private void drawDemoMatches() {
		tinyTri.setSize(2, 2);
		tinyTri.setAlpha(0.8f);
		for ( Integer[] each : triPtDown ) {
			tinyTri.setPosition(3+each[0], 12+each[1]);
			tinyTri.draw(batch);
		}
		for ( Integer[] each : triPtRight ) {
			tinyTri.setPosition(3+each[0], 20+each[1]);
			tinyTri.draw(batch);
		}
		for ( Integer[] each : triPtUp ) {
			tinyTri.setPosition(36+each[0], 12+each[1]);
			tinyTri.draw(batch);
		}
		for ( Integer[] each : triPtLeft ) {
			tinyTri.setPosition(36+each[0], 20+each[1]);
			tinyTri.draw(batch);
		}				
	}
	@Override
	protected void stage1Tasks() {
		messageCentered("<-Like these->", 18);
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
		messageCentered("Target:- 20", 40);
		if ( score < 6 ) {
			drawDemoMatches();
			messageCentered("I'll leave the examples showing for a little while...", 4);
		} else if ( score < 11 ) {
			drawDemoMatches();
			messageCentered("[GOLD]Pro Tip[]:- strangely shaped groups of "
							+ "triangles may have multiple matches.", 4);
		} else if ( score < 17 ) {
			messageCentered("You learn fast!\n"
					+ "I guess you don't need the examples any longer.", 4);
		} else if ( score < 20 ) {
			messageCentered("Keep going...", 2);
		} else if ( score > 19 ) {
			levelStage = 7;
			logic.setEndlessPlayMode(false);
		}
	}
	@Override
	protected void nextLearningLevel() {
		core.setScreen(new TrainingLevel3(core, true, true));
	}
	@Override
	protected void nextLevel() {
		core.setScreen(new ChallengeLevel1(core, true));
	}
}
