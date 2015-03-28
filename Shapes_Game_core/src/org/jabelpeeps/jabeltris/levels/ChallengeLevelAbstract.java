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
			fadeOutAndReturnToMenu();	
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
			Core.textCentre("Score:- " + score, Core.topEdge - 5);
			
			if ( levelStage == 3 ) {
				stage3Tasks(matches);
			    batch.end();
			    return;
			}
			if ( levelStage == 7 ) {
				finalMessages();
			    batch.end();
			    recordCompleted();
			    
		    	if ( !levelIsFinished ) levelNeedsFinishing();
		    	
		    	else {
					alpha = (alpha > 0f) ? (alpha - 0.01f) : 0f ;
			    	
			    	if ( !logic.isAlive() && alpha <= 0f ) {
			    		Core.delay(200);
						if ( playOn ) 
							nextLevel();
						else 	
							menuScreen();
						dispose();
					}
					Gdx.graphics.requestRendering();
				}
			}
		}
	}
	
	protected void stage1Tasks() {
		alpha = (alpha < 0.4f) ? (alpha + 0.005f) : 0.4f ;
		
		Core.textCentre( firstMessage , 48);
		Core.textCentre("[GOLD]Touch here[] to begin", -2);
		
		if ( Gdx.input.justTouched() ) {
			cameraUnproject();
			
			if ( touch.y < 0 ) {
				levelStage++;
				setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
				logic.sendStartSignal();
			}
		}
	}
	protected void stage2Tasks() {
		alpha = (alpha < 1f) ? (alpha + 0.05f) : 1f ;
		
		Core.textCentre(title, 48);
		Core.textCentre("Have Fun!", 4);
		
		if ( alpha == 1f ) levelStage++;
	}
	
	protected abstract void stage3Tasks(int matches);
	
	protected void finalMessages() {

		Core.textCentre("[RED]Final Score.[]", 44);
		Core.textCentre("Level Over!\n"
					+ "[GOLD]Touch here[] to " 
					+ ( playOn? "continue."
							  : "finish." ), 2);
	}
	@Override
	protected void menuScreen() {
		core.setScreen(new MainMenu(core, levelIsFinished? 1 : 6 ));
	}
}
