package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;

import com.badlogic.gdx.Gdx;

public abstract class TrainingLevelAbstract extends LevelMaster {
	
	protected TrainingLevelAbstract() {
		super();
	}

	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			doWhenBackKeyPressed();
			return;
		}
		prepScreenAndCamera();
		renderBoard(alpha);
		
		switch ( levelStage ) {
		case 1:
			batch.begin();
			stage1Tasks();
			batch.end();
			break;
		case 2:
			batch.begin();
			stage2Tasks();
			batch.end();
			break;
		case 3:
			batch.begin();
			stage3Tasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 4:
			batch.begin();
			stage4Tasks();
			batch.end();
			Core.delay(500);
			Gdx.graphics.requestRendering();
			break;
		case 5:
			batch.begin();
			stage5Tasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 6:
		case 7:
			batch.begin();
			matches = game.getTotalMatches();
			messageCentered("Sets Matched:- " + matches, 46);
			
			if ( levelStage == 6 ) {
				stage6Tasks(matches);
				batch.end();
				return;
			}
		    if ( levelStage == 7 ) {
				finalMessages();
				batch.end();
		    	if ( !levelIsFinished ) {
					levelNeedsFinishing();
		    	} else {
					alpha = (alpha > 0f) ? (alpha - 0.01f) : 0f ;
					
					if ( !logic.isAlive() && alpha == 0f ) {
			    		Core.delay(200);
						if ( playingLearningLevels ) {
							nextLearningLevel();
						} else if ( playOn ) {
							nextLevel();
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
	
	protected void doWhenBackKeyPressed() {
		
		if ( !logic.isAlive() && alpha <= 0f ) {
			core.setScreen(new MainMenu(core, 3));
			dispose();
		} else if ( levelStage < 3 || levelStage > 5 ) {
			logic.endDemo();
			levelIsFinished = true;
			levelStage = 0;
		} 
		
		alpha = (alpha > 0f) ? (alpha - 0.01f) : 0f ;
		prepScreenAndCamera();
		renderBoard(alpha);
		Gdx.graphics.requestRendering();
	}
	
	protected void stage1Tasks() {
		alpha = (alpha < 0.4f) ? (alpha + 0.005f) : 0.4f ;
		
		messageCentered(firstMessage, 48);
		messageCentered("[#FFD700]Touch here[] to remove the text.", 0);
		
		if ( Gdx.input.justTouched() ) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Core.camera.unproject(touch);
			if ( touch.y < 0 ) {
				levelStage = 2;
			}
		}
	}
	protected void stage2Tasks() {
		alpha = (alpha < 1f) ? (alpha + 0.02f) : 1f ;
		
		messageCentered(title, 48);
		messageCentered("[#FFD700]Touch here again[] when you are ready to take over.", 2);
	
		if ( Gdx.input.justTouched() ) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Core.camera.unproject(touch);
			if ( touch.y < 0 ) {
				levelStage = 3;
				logic.endDemo();
			}
		}
	}
	protected void stage3Tasks() {
		alpha = (alpha < 1f) ? (alpha + 0.02f) : 1f ;
		
		messageCentered(title, 48);
		messageCentered("Ending AutoPlay...", 2);
		if ( !logic.isAlive() ) {
			levelStage++;
		}
	}
	protected void stage4Tasks() {
		logic = new InteractiveGameLogic(game, true);
		logic.setEndlessPlayMode(true);
		
		messageCentered("Sets Matched:- 0", 46);
		messageCentered("Target:- 20", 40);
		messageCentered("Get Ready to Play...", 0);
		
		levelStage++;
	}
	protected void stage5Tasks() {
		setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
		logic.start();
		levelStage++;
	}
	protected abstract void stage6Tasks(int score);
	
	protected void finalMessages() {
		messageCentered("[RED]You have finished.[]", 40);
		messageCentered("Well done! \n"
				+ "[GOLD]Touch here[] to " + (playOn ? "continue." : "finish."), 4);
	}
	protected void levelNeedsFinishing() {
		if ( game.getHintListSize() == 0 ) {
    		levelIsFinished = true;
    	}
    	if ( Gdx.input.justTouched() ) {
		touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		Core.camera.unproject(touch);
			if ( touch.y < 8 ) {
				levelIsFinished = true;
			}
	    }
	}
	protected abstract void nextLearningLevel();
	
	protected abstract void nextLevel();
	
	@Override
	public boolean IsFinished() {
		return levelIsFinished;
	}
}
