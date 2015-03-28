package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.PlayAreaInput;

public abstract class EndlessGame extends LevelMaster {
	
// ---------------------------------------------Constructor(s)--------	
	public EndlessGame(boolean initialise) {
		super();
		if ( initialise ) {
			game = new PlayArea();
			game.initialise(this);
			logic = new InteractiveGameLogic(game);
			logic.setEndlessPlayMode(true);
			setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
			logic.start();
		}
	}
// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			fadeOutAndReturnToMenu();	
			return;
		}
		
		prepScreenAndCamera();
		
		batch.begin();
		int hints = game.getHintListSize();
		
		Core.textCentre( (hints > 0)? "Possible moves:- "+hints
							   		: game.getMessage()       , 48);
		renderBoard();
		batch.end();
	}
	@Override
	protected void menuScreen() {
		core.setScreen(new MainMenu(core, 6, 2));
	}
	@Override
	protected void nextLevel() {
	}
}
