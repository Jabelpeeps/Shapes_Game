package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;

import com.badlogic.gdx.Gdx;

public abstract class ChallengeLevelAbstract extends LevelMaster {

	protected float alpha = 0f;
	protected boolean playOn = true;
	protected boolean levelIsFinished = false;
	protected int levelStage = 1;
	protected int score = 0;
	protected String title;
	
// ---------------------------------------------Constructors--------	

	public ChallengeLevelAbstract(Core c) {
		super(c);
	}

// ---------------------------------------------Methods--------------	

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
	protected void case1commonTasks() {
		
		alpha = (alpha < 0.4f) ? (alpha + 0.003f) : 0.4f ;
		messageCentered("[GOLD]Touch here[] to begin", 0);
		
		if ( Gdx.input.isTouched() ) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Core.camera.unproject(touch);
			if ( touch.y < 0) {
				levelStage++;
				setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
				logic.sendStartSignal();
			}
		}
	}
	protected void case2commonTasks() {
		
		alpha = (alpha < 1f) ? (alpha + 0.05f) : 1f ;
		messageCentered(title, 48);
		messageCentered("Have Fun!", 4);
		if ( alpha == 1f ) {
			levelStage++;
		}
	}
	protected void finalMessages() {

		messageCentered("[RED]Scoring has ended.[]", 45);
		messageCentered("Level Over!\n"
				+ "[GOLD]Touch here[] to " + (playOn ? "continue." : "finish."), 2);
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
