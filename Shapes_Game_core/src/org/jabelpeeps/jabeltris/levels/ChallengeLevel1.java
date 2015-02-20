package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.SquareBlue;
import org.jabelpeeps.jabeltris.shapes.SquareRed;
import org.jabelpeeps.jabeltris.shapes.TriangleGreen;
import org.jabelpeeps.jabeltris.shapes.TriangleYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel1 extends LevelMaster{

	private float alpha = 0.3f;
	private boolean playOn = true;
	private boolean levelIsFinished = false;
	private int levelStage = 1;
	private int score = 0;
	
// ---------------------------------------------Constructors--------	
	
	public ChallengeLevel1(Core c) {
		this(c, true);
	}

	public ChallengeLevel1(Core c, boolean playNext) {
		super(c);
		playOn = playNext;
		baseColor = new Color(0.75f, 1f, 1f, 1f);
		x = 7;
		y = 7;
		initPlayArea();
		logic = new InteractiveGameLogic(game);
		logic.waitForStartSignal();
		logic.setEndlessPlayMode(true);
		setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
		logic.start();
	}

	@Override
	public void render(float delta) {
		if ( logic.getBackKeyWasPressed() ) {
			if ( !logic.isAlive() && alpha <= 0f ) {
				core.setScreen(new MainMenu(core));
				dispose();
			} else {
				levelIsFinished = true;
				levelStage = 0;
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
		
		switch ( levelStage ) {
		case 1:
			batch.begin();
			messageCentered( "Level 3 - Mix Up\n\n"
					+ "Ok. No demo this time. You know these shapes. "
					+ "Your target is 30 matches, and I'll be keeping score too...", 48);
			messageCentered("[#FFD700]Touch here[] to begin", 0);
			batch.end();
			if ( Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y < -2) {
					levelStage++;
					logic.sendStartSignal();
				}
			}
			break;
		case 2:
			batch.begin();
			messageCentered("Level 3 - Mix up", 48);
			messageCentered("Have Fun!", 4);
			batch.end();
			if ( alpha < 1f ) {
				alpha += 0.01f;
			} else {
				levelStage++;
			}
			Gdx.graphics.requestRendering();
			break;
		case 3:
			score = game.getScore();
		case 7:
			int matches = game.getTotalMatches();
			batch.begin();
			messageCentered("Score:- " + score, 48);
			messageCentered("Sets Matched:- " + matches + " / 30", 42);
			if ( matches < 6 ) {
				messageCentered("", 2);
			} else if ( matches < 11 ) {
				messageCentered("[#FFD700]Pro Tip[]:- ", 4);
			} else if ( matches < 16 ) {
				messageCentered("", 4);
			} else if ( matches < 20 ) {
				messageCentered("", 2);
			} else if ( matches > 29 ) {
				levelStage = 7;
				if ( playOn ) {
					messageCentered("Well done! No further score will be counted. [#FFD700]Touch here[] to continue.", 4);
				} else {
					messageCentered("Well done! No further score will be counted. [#FFD700]Touch here[] to finish.", 4);
				}
			} 		
		    batch.end();
		    
			if ( levelStage == 7 && Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y < 8 ) {
					levelIsFinished = true;
				} 
			}
			if ( !logic.isAlive() && alpha <= 0f ) {
				if ( levelStage == 7 && playOn ) {
					core.setScreen(new TrainingLevel3(core, true));
				} else {	
					core.setScreen(new MainMenu(core));
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
		int option = rand.nextInt(4) + 1;
		switch (option) {
			case 1:                       
				newShape = new SquareRed();
				break;
			case 2:                       
				newShape = new SquareBlue();
				break;
			case 3:                       
				newShape = new TriangleYellow();				
				break;
			case 4:
				newShape = new TriangleGreen();
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
