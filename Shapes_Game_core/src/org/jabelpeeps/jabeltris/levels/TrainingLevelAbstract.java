package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;

import com.badlogic.gdx.Gdx;

public abstract class TrainingLevelAbstract extends LevelMaster {

	protected int demoStage = 1;
	protected float alpha = 0.3f;
	protected boolean playOn = true;
	protected boolean playingLearningLevels = false;
	protected boolean levelIsFinished = false;
	protected String title;
	
	protected TrainingLevelAbstract(Core c) {
		super(c);
	}

	protected void doWhenBackKeyPressed() {
		if ( !logic.isAlive() && alpha <= 0f ) {
			core.setScreen(new MainMenu(core, 3));
			dispose();
		} else if ( demoStage < 3 || demoStage > 5 ) {
			logic.endDemo();
			levelIsFinished = true;
			demoStage = 0;
		} 
		
		alpha = (alpha > 0f) ? (alpha - 0.01f) : 0f ;
		prepScreenAndCamera();
		renderBoard(alpha);
		Gdx.graphics.requestRendering();
	}
	
	protected void case1commonTasks() {
		
		messageCentered("[#FFD700]Touch here[] to remove the text.", 0);
		
		if ( Gdx.input.isTouched() ) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Core.camera.unproject(touch);
			if ( touch.y < 0 ) {
				demoStage = 2;
			}
		}
	}
	protected void case2commonTasks() {
		alpha = (alpha < 1f) ? (alpha + 0.01f) : 1f ;
		
		messageCentered(title, 48);
		messageCentered("[#FFD700]Touch the board[] when you are ready to take over.", 2);
	
		if ( Gdx.input.isTouched() ) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Core.camera.unproject(touch);
			if ( touch.y > 8 ) {
				demoStage = 3;
				logic.endDemo();
			}
		}
	}
	protected void case3commonTasks() {
		alpha = (alpha < 1f) ? (alpha + 0.02f) : 1f ;
		
		messageCentered(title, 48);
		messageCentered("Ending AutoPlay...", 2);
		if ( !logic.isAlive() ) {
			demoStage++;
		}
	}
	protected void case4commonTasks() {
		logic = new InteractiveGameLogic(game, true);
		logic.setEndlessPlayMode(true);
		
		messageCentered("Sets Matched:- 0", 46);
		messageCentered("Target:- 20", 40);
		messageCentered("Get Ready to Play...", 0);
		
		demoStage++;
	}
	protected void case5commonTasks() {
		setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
		logic.start();
		demoStage++;
		Gdx.graphics.requestRendering();
	}
	protected void finalMessages() {
		messageCentered("[#FF0000]You have finished.[]", 42);
		messageCentered("Well done! \n"
				+ "[#FFD700]Touch here[] to " + (playOn ? "continue." : "finish."), 4);
	}
	protected void levelNeedsFinishing() {
		if ( game.getHintListSize() == 0 ) {
    		levelIsFinished = true;
    	}
    	if ( Gdx.input.isTouched() ) {
		touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		Core.camera.unproject(touch);
			if ( touch.y < 8 ) {
				levelIsFinished = true;
			}
	    }
	}
	@Override
	public boolean IsFinished() {
		return levelIsFinished;
	}
}
