package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;

import com.badlogic.gdx.Gdx;

public abstract class ChallengeLevelAbstract extends LevelMaster {
	
	protected ChallengeLevelAbstract() {
		super();
	}
// ---------------------------------------------Methods--------------	
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			doWhenBackKeyWasPressed();	
			return;
		}
		
		prepScreenAndCamera();
		batch.begin();
		renderBoard(alpha);
		
		switch ( levelStage ) {
		case 1:
			stage1Tasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 2:
			stage2Tasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 3:
			score = game.getScore();
		case 7:
			matches = game.getTotalMatches();
			messageCentered("Score:- " + score, 48);
			if ( levelStage == 3 ) {
				stage3Tasks(matches);
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
			    	
			    	if ( !logic.isAlive() && alpha <= 0f ) {
			    		Core.delay(200);
						if ( playOn ) {
							nextLevel();
						} else {	
							core.setScreen(new MainMenu(core));
						}
						dispose();
					}
					Gdx.graphics.requestRendering();
				}
			}
		}
	}
	protected void doWhenBackKeyWasPressed() {
		
		if ( !logic.isAlive() && alpha <= 0f ) {
			core.setScreen(new MainMenu(core));
			dispose();
		} else {
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
		
		messageCentered( firstMessage , 48);
		messageCentered("[GOLD]Touch here[] to begin", 0);
		
		if ( Gdx.input.justTouched() ) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Core.camera.unproject(touch);
			if ( touch.y < 0) {
				levelStage++;
				setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
				logic.sendStartSignal();
			}
		}
	}
	protected void stage2Tasks() {
		alpha = (alpha < 1f) ? (alpha + 0.05f) : 1f ;
		
		messageCentered(title, 48);
		messageCentered("Have Fun!", 4);
		if ( alpha == 1f ) {
			levelStage++;
		}
	}
	protected abstract void stage3Tasks(int matches);
	
	protected void finalMessages() {

		messageCentered("[RED]Scoring has ended.[]", 45);
		messageCentered("Level Over!\n"
				+ "[GOLD]Touch here[] to " + (playOn ? "continue." : "finish."), 2);
	}
	protected void levelNeedsFinishing() {
		
		if ( game.getHintListSize() == 0 ) {
    		levelIsFinished = true;
    	}
		if ( Gdx.input.justTouched() ) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Core.camera.unproject(touch);
			if ( touch.y < 0 ) {
				levelIsFinished = true;
			} 
	    }
	}
	protected abstract void nextLevel();
	
	@Override
	public boolean IsFinished() {
		return levelIsFinished;
	}
}
