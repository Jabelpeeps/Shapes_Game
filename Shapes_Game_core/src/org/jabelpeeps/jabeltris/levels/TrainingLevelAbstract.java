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
		
		if ( logic.getBackKeyWasPressed() && ( levelStage < 3 || levelStage > 5 ) ) {
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
			break;
		case 2:
			stage2Tasks();
			batch.end();
			break;
		case 3:
			stage3Tasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 4:
			stage4Tasks();
			batch.end();
			Core.delay(500);
			Gdx.graphics.requestRendering();
			break;
		case 5:
			stage5Tasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 6:
		case 7:
			matches = game.getTotalMatches();
			Core.textCentre("Sets Matched:- " + matches, Core.topEdge - 5);
			
			if ( levelStage == 6 ) {
				stage6Tasks(matches);
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
					
					if ( !logic.isAlive() && alpha == 0f ) {
						Core.delay(200);
						if ( playingLearningLevels ) 
							nextLearningLevel();
						else if ( playOn ) 
							nextLevel();
						else
							core.setScreen(new MainMenu(core, 4));
						dispose();
					}
					Gdx.graphics.requestRendering();
		    	}
			}
		}
	}
		
	protected void stage1Tasks() {
		alpha = (alpha < 0.4f) ? (alpha + 0.005f) : 0.4f ;
		
		Core.textCentre(firstMessage, 48);
		Core.textCentre("[GOLD]Touch here[] to start demo.", -2);
		
		if ( Gdx.input.justTouched() ) {
			cameraUnproject();
			
			if ( touch.y < 0 ) {
				logic.sendStartSignal();
				levelStage = 2;
			}
		}
		Gdx.graphics.requestRendering();
	}
	protected void stage2Tasks() {
		alpha = (alpha < 1f) ? (alpha + 0.02f) : 1f ;
		
		Core.textCentre(title, 48);
		Core.textCentre("[ORANGE]Touch again[] to take over play.", -2);
	
		if ( Gdx.input.justTouched() ) {
			cameraUnproject();
			
			if ( touch.y < 0 ) {
				levelStage = 3;
				logic.shutDown();
			}
		}
	}
	protected void stage3Tasks() {
		alpha = (alpha < 1f) ? (alpha + 0.02f) : 1f ;
		
		Core.textCentre(title, 48);
		Core.textCentre("Ending AutoPlay...", -2);
		
		if ( !logic.isAlive() ) levelStage++;
	}
	protected void stage4Tasks() {
		logic = new InteractiveGameLogic(game, true);
		logic.setEndlessPlayMode(true);
		
		Core.textCentre("Sets Matched:- 0", Core.topEdge - 5);
		Core.textCentre("Target:- 20", Core.topEdge - 10);
		Core.textCentre("Get Ready to Play...", -2);
		
		levelStage++;
	}
	protected void stage5Tasks() {
		setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
		logic.start();
		levelStage++;
	}
	protected abstract void stage6Tasks(int score);
		
	protected void finalMessages() {
		Core.textCentre("[RED]You have finished.[]", Core.topEdge - 15);
		Core.textCentre("Well done! \n"
				+ "[GOLD]Touch here[] to " 
				+ (playOn? "continue."
						 : "finish."), 4);
	}
	@Override
	protected void menuScreen() {
		core.setScreen(new MainMenu(core, 6, (playingLearningLevels? 3 : 1) ));
	}
	protected abstract void nextLearningLevel();
}