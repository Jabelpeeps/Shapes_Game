package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.TriangleBlue;
import org.jabelpeeps.jabeltris.shapes.TriangleGreen;
import org.jabelpeeps.jabeltris.shapes.TriangleRed;
import org.jabelpeeps.jabeltris.shapes.TriangleYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TrainingLevel2 extends LevelMaster {

	private int demoStage = 1;
	private static float alpha = 0.3f;
	private boolean playOn = true;
	private boolean playingLearningLevels = false;
	private boolean levelIsFinished = false;
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
		x = 6;
		y = 6;
		initPlayArea();
		logic = new DemoGameLogic(game);
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}
// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			if ( !logic.isAlive() && alpha <= 0f ) {
				core.setScreen(new MainMenu(core, 3));
				dispose();
			} else if ( demoStage < 3 || demoStage > 5 ) {
				logic.endDemo();
				levelIsFinished = true;
				demoStage = 0;
			} 
			if ( alpha >= 0f ) {
				alpha -= 0.01f;
			}
			prepScreenAndCamera();
			renderBoard(alpha);
			Gdx.graphics.requestRendering();
			return;
		}
		
		prepScreenAndCamera();
		renderBoard(alpha);
	
		switch ( demoStage ) {
		case 1:
			batch.begin();
			messageCentered("Level 2 - Triangles\n\n"
					+ "Triangles match when placed in T-shaped groups.", 48);
			messageCentered("<-Like these->", 18);
			drawDemoMatches();
			messageCentered("[#FFD700]Touch here[] to remove the text.", 2);
			batch.end();
			if ( Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y < 0 ) {
					demoStage = 2;
				}
			}
			break;
		case 2:
			if ( alpha < 1f ) {
				alpha += 0.003f;
			}
			batch.begin();
			messageCentered("Level 2 - Triangles", 48);
			drawDemoMatches();
			messageCentered("[#FFD700]Touch the board[] when you are ready to take over.", 2);
			batch.end();
			if ( Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y > 8 ) {
					demoStage = 3;
					logic.endDemo();
				}
			}
			break;
		case 3:
			if ( alpha < 1f ) {
				alpha += 0.003f;
			}
			batch.begin();
			messageCentered("Level 2 - Triangles", 48);
			drawDemoMatches();
			messageCentered("Ending AutoPlay...", 0);
			batch.end();
			if ( !logic.isAlive() ) {
				demoStage++;
			}
			Gdx.graphics.requestRendering();
			break;
		case 4:
			Core.delay(300);
			logic = new InteractiveGameLogic(game, true);
			logic.setEndlessPlayMode(true);
			batch.begin();
			messageCentered("Sets Matched:- 0", 46);
			messageCentered("Target:- 20", 40);
			drawDemoMatches();
			messageCentered("Get Ready to Play...", 0);
			batch.end();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 5:
			Core.delay(1000);
			setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
			logic.start();
			demoStage++;
			break;
		case 6:
		case 7:
			int matches = game.getTotalMatches();
			batch.begin();
			font.draw(batch, "Sets Matched:- " + matches, 6, 46);
			font.draw(batch, "Target:- 20", 10, 40);
			if ( matches < 6 ) {
				drawDemoMatches();
				messageCentered("I'll leave the examples showing for now...", 4);
			} else if ( matches < 11 ) {
				drawDemoMatches();
				messageCentered("[#FFD700]Pro Tip[]:- strangely shaped groups of "
								+ "triangles may have multiple matches.", 4);
			} else if ( matches < 16 ) {
				messageCentered("You learn fast...\n"
						+ "...you won't be needing the examples now.", 4);
			} else if ( matches < 20 ) {
				messageCentered("Keep going...", 2);
			} else if ( matches > 19 ) {
				demoStage = 7;
				if ( playOn ) {
					messageCentered("Well done! You may play on, or [#FFD700]touch here[] to continue.", 4);
				} else {
					messageCentered("Well done! You may play on, or [#FFD700]touch here[] to finish.", 4);
				}
			}  		
		    batch.end();
		
			if ( demoStage == 7 && Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y < 8 ) {
					levelIsFinished = true;
				} 
			}
			if ( !logic.isAlive() && alpha <= 0f ) {
				if ( demoStage == 7 && playOn && playingLearningLevels ) {
//					core.setScreen(new TrainingLevel3(core, true, true));
				} else if ( demoStage == 7 && playOn ) {
					core.setScreen(new ChallengeLevel1(core, true));
				} else {	
					core.setScreen(new MainMenu(core, 4));
				}
				dispose();
			}
			if ( levelIsFinished ) {
				alpha -= 0.01f;
				Gdx.graphics.requestRendering();
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
		newShape.setOriginAndBounds(x , y);
		return newShape;
	}
	@Override
	public boolean IsFinished() {
		return levelIsFinished;
	}
}
