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
import com.badlogic.gdx.math.Vector3;

public class TrainingLevel2 extends LevelMaster {

	private Shape newShape;
	private int demoStage = 1;
	private Vector3 touch = new Vector3();
	private static float alpha = 0.3f;
	private boolean playOn = true;
	private boolean levelIsFinished = false;
	public Sprite tinyTri = new Sprite(LevelMaster.triangle);
	private Integer[][] triPtDown  = new Integer[][]{{-2,0}, {0,0}, {+2,0}, {0,-2}};
	private Integer[][] triPtUp    = new Integer[][]{{-2,0}, {0,0}, {+2,0}, {0,+2}}; 
	private Integer[][] triPtLeft  = new Integer[][]{{0,+2}, {0,0}, {0,-2}, {-2,0}}; 
	private Integer[][] triPtRight = new Integer[][]{{0,+2}, {0,0}, {0,-2}, {+2,0}};  
	
// ---------------------------------------------Constructors--------	
	public TrainingLevel2(Core c) {
		this(c, true);
	}
	
	public TrainingLevel2(Core c, boolean playNext) {
		super(c);
		playOn = playNext;
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
			if ( alpha > 0f ) {
				alpha -= 0.01f;
			}
			if ( logic.isAlive() ) {
				prepScreenAndCamera();
				renderBoard(alpha);
			}
			Gdx.graphics.requestRendering();
			return;
		}	
		switch ( demoStage ) {
		case 1:
			prepScreenAndCamera();
			renderBoard(alpha);
			batch.begin();
			messageCentered("Level 2 - Triangles\n\n"
					+ "Triangles match when placed in T-shaped groups.", 48);
			messageLeft("Like these:-", 12);
			drawDemoMatches();
			messageCentered("[#FFD700]Touch here[] to remove this text.", -2);
			batch.end();
			if ( Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y < 0 ) {
					demoStage = 2;
				}
			}
			Gdx.graphics.requestRendering();
			break;
		case 2:
			if ( alpha < 1f ) {
				alpha += 0.003f;
			}
			prepScreenAndCamera();
			renderBoard(alpha);
			batch.begin();
			messageCentered("Level 2 - Triangles", 48);
			drawDemoMatches();
			messageCentered("[#FFD700]Touch the board[] when you are ready to take over.", -1);
			batch.end();
			if ( Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y > 8 ) {
					demoStage = 3;
					logic.endDemo();
				}
			}
			Gdx.graphics.requestRendering();
			break;
		case 3:
			if ( alpha < 1f ) {
				alpha += 0.003f;
			}
			prepScreenAndCamera();
			renderBoard(alpha);
			batch.begin();
			messageCentered("Level 2 - Triangles", 48);
			drawDemoMatches();
			messageCentered("Ending AutoPlay...", -2);
			batch.end();
			if ( !logic.isAlive() ) {
				demoStage++;
			}
			Gdx.graphics.requestRendering();
			break;
		case 4:
			Core.delay(300);
			prepScreenAndCamera();
			renderBoard(alpha);
			logic = new InteractiveGameLogic(game);
			logic.setEndlessPlayModeOn();
			batch.begin();
			messageCentered("Sets Matched:- 0", 46);
			messageCentered("Target:- 20", 40);
			drawDemoMatches();
			messageCentered("Get Ready to Play...", -2);
			batch.end();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 5:
			Core.delay(1000);
			setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
			logic.start();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 6:
		case 7:
			prepScreenAndCamera();
			int matches = game.getTotalMatches();
			batch.begin();
			font.draw(batch, "Sets Matched:- " + matches, 6, 46);
			font.draw(batch, "Target:- 20", 10, 40);
			if ( matches < 6 ) {
				drawDemoMatches();
				messageCentered("I'll leave these examples here for now...", 0);
			} else if ( matches < 11 ) {
				drawDemoMatches();
				messageCentered("", 4);
			} else if ( matches < 16 ) {
				messageCentered("I don't think you need those any more.", 4);
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
		    renderBoard(alpha);
		
			if ( demoStage == 7 && Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y < 8 ) {
					levelIsFinished = true;
				} 
			}
			if ( !logic.isAlive() && alpha <= 0f ) {
//				if ( demoStage == 7 && playOn ) {
//					core.setScreen(new TrainingLevel2(core));
//				} else {
					core.setScreen(new MainMenu(core, 4));
//				}
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
			tinyTri.setPosition(8+each[0], 3+each[1]);
			tinyTri.draw(batch);
		}
		for ( Integer[] each : triPtRight ) {
			tinyTri.setPosition(16+each[0], 3+each[1]);
			tinyTri.draw(batch);
		}
		for ( Integer[] each : triPtUp ) {
			tinyTri.setPosition(24+each[0], 3+each[1]);
			tinyTri.draw(batch);
		}
		for ( Integer[] each : triPtLeft ) {
			tinyTri.setPosition(32+each[0], 3+each[1]);
			tinyTri.draw(batch);
		}				
	}
	@Override
	public Shape makeNewShape(int x, int y) {
				
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
		Shape tmpShape = setOriginAndBounds(newShape, x , y);
		return tmpShape;
	}
	@Override
	public boolean IsFinished() {
		return levelIsFinished;
	}
}
