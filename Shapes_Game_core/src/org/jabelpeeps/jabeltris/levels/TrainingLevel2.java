package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.TriangleBlue;
import org.jabelpeeps.jabeltris.shapes.TriangleGreen;
import org.jabelpeeps.jabeltris.shapes.TriangleRed;
import org.jabelpeeps.jabeltris.shapes.TriangleYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TrainingLevel2 extends TrainingLevelAbstract {

	public Sprite tinyTri = new Sprite(LevelMaster.triangle);
	private Integer[][] triPtDown  = new Integer[][]{{-2,0}, {0,0}, {+2,0}, {0,-2}};
	private Integer[][] triPtUp    = new Integer[][]{{-2,0}, {0,0}, {+2,0}, {0,+2}}; 
	private Integer[][] triPtLeft  = new Integer[][]{{0,+2}, {0,0}, {0,-2}, {-2,0}}; 
	private Integer[][] triPtRight = new Integer[][]{{0,+2}, {0,0}, {0,-2}, {+2,0}};  
	
// ---------------------------------------------Constructors--------	
	public TrainingLevel2(Core c) {
		this(c, true, false);
	}
	public TrainingLevel2(Core c, boolean playNext) {	
		this(c, playNext, false);
	}
	public TrainingLevel2(Core c, boolean playNext, boolean learningLevels) {
		super(c);
		playOn = playNext;
		playingLearningLevels = learningLevels;
		baseColor = new Color(1f, 1f, 1f, 1f);
		title = "Demo Level 2\nTriangles";
		
		game = new PlayArea(6, 6, this);
		logic = new DemoGameLogic(game);
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}
// ---------------------------------------------Methods----------
	@Override
	public Shape makeNewShape(int x, int y) {
		
		Shape newShape = null;			
		int option = rand.nextInt(4) + 1;
		switch (option) {
			case 1:                       
				newShape = new TriangleRed();
				break;
			case 2:                       
				newShape = new TriangleBlue();
				break;
			case 3:                       
				newShape = new TriangleGreen();				
				break;
			case 4:
				newShape = new TriangleYellow();
				break;
		}
		return newShape;
	}
	
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			doWhenBackKeyPressed();
			return;
		}
		
		prepScreenAndCamera();
		renderBoard(alpha);
	
		switch ( demoStage ) {
		case 1:
			batch.begin();
			messageCentered( title + "\n\n"
					+ "Triangles match when placed in T-shaped groups.", 48);
			messageCentered("<-Like these->", 18);
			drawDemoMatches();
			case1commonTasks();
			batch.end();
			break;
		case 2:
			batch.begin();
			drawDemoMatches();
			case2commonTasks();
			batch.end();
			break;
		case 3:
			batch.begin();
			drawDemoMatches();
			case3commonTasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 4:
			batch.begin();
			drawDemoMatches();
			case4commonTasks();
			batch.end();
			Core.delay(500);
			Gdx.graphics.requestRendering();
			break;
		case 5:
			case5commonTasks();
			break;
		case 6:
		case 7:
			int matches = game.getTotalMatches();
			batch.begin();
			messageCentered("Sets Matched:- " + matches, 46);
			if ( demoStage == 6 ) {
				messageCentered("Target:- 20", 40);
				if ( matches < 6 ) {
					drawDemoMatches();
					messageCentered("I'll leave the examples showing for a little while...", 4);
				} else if ( matches < 11 ) {
					drawDemoMatches();
					messageCentered("[#FFD700]Pro Tip[]:- strangely shaped groups of "
									+ "triangles may have multiple matches.", 4);
				} else if ( matches < 17 ) {
					messageCentered("You learn fast!\n"
							+ "I guess you don't need the examples any longer.", 4);
				} else if ( matches < 20 ) {
					messageCentered("Keep going...", 2);
				} else if ( matches > 19 ) {
					demoStage = 7;
					logic.setEndlessPlayMode(false);
				}
				batch.end();
				return;
			}
			if ( demoStage == 7 ) {
				finalMessages();
				batch.end();
				if ( !levelIsFinished ) {
					levelNeedsFinishing();
		    	} else {
					alpha = (alpha > 0f) ? (alpha - 0.01f) : 0f ;
					
		    		if ( !logic.isAlive() && alpha == 0f ) {
			    		Core.delay(200);
						if ( playingLearningLevels ) {
							core.setScreen(new TrainingLevel3(core, true, true));
						} else if ( playOn ) {
							core.setScreen(new ChallengeLevel1(core, true));
						} else {	
							core.setScreen(new MainMenu(core, 4));
						}
						dispose();
			    	}
			    	Gdx.graphics.requestRendering();
		    	}
		    }
		}
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
}
