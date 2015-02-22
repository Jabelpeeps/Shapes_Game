package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.SquareBlue;
import org.jabelpeeps.jabeltris.shapes.SquareRed;
import org.jabelpeeps.jabeltris.shapes.SquareYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class TrainingLevel1 extends LevelMaster {

	private int demoStage = 1;
	private float alpha = 0.3f;
	private boolean playOn = true;
	private boolean playingLearningLevels = false;
	private boolean levelIsFinished = false;
	
// ---------------------------------------------Constructors--------	
	public TrainingLevel1(Core c) {
		this(c, true, false);
	}
	public TrainingLevel1(Core c, boolean playNext) {	
		this(c, playNext, false);
	}
	public TrainingLevel1(Core c, boolean playNext, boolean learningLevels) {
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
			messageCentered("Level 1 - Squares\n\n"
					+ "Squares match when placed in square groups.\n\n\n"
					+ "AutoPlay is showing you how this works.", 48);
			messageCentered("[#FFD700]Touch here[] to remove the text.", 0);
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
			messageCentered("Level 1 - Squares", 48);
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
			messageCentered("Level 1 - Squares", 48);
			messageCentered("Ending AutoPlay...", 2);
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
			messageCentered("Get Ready to Play...", 2);
			batch.end();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 5:
			Core.delay(500);
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
				messageCentered("Touch and drag shapes where you want them to go.", 2);
			} else if ( matches < 11 ) {
				messageCentered("[#FFD700]Pro Tip[]:- Look for moves that make more than one match.", 4);
			} else if ( matches < 16 ) {
				messageCentered("Multiple matches will score [#FFD700]extra points[] in the scoring levels.", 4);
			} else if ( matches < 20 ) {
				messageCentered("Almost there...", 2);
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
					core.setScreen(new TrainingLevel2(core, true, true));
				} else if ( demoStage == 7 && playOn ) {
					core.setScreen(new TrainingLevel2(core, true));
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
	
	@Override
	public Shape makeNewShape(int x, int y) {
		Shape newShape = null;			
		int option = rand.nextInt(3) + 1;
		switch (option) {
			case 1:                       
				newShape = new SquareRed();
				break;
			case 2:                       
				newShape = new SquareBlue();
				break;
			case 3:                       
				newShape = new SquareYellow();				
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
