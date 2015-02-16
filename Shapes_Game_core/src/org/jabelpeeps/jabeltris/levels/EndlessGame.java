package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;

public abstract class EndlessGame extends LevelMaster {
	
// ---------------------------------------------Constructor(s)--------	
	public EndlessGame(Core g) {
		super(g);
		initPlayArea(this);
		logic = new InteractiveGameLogic(game);
		logic.setEndlessPlayModeOn();
		setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
		logic.start();
	}
// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		if ( !logic.isAlive() ) {
			core.setScreen(new MainMenu(core));	
		}
		prepScreenAndCamera();
		int hints = game.getHintListSize();
		batch.begin();
		if ( hints > 0 ) {
			font.draw(batch, "Possible moves:- " + hints, 4, 48);
		} else {
			font.draw(batch, game.getMessage(), 4, 48);
		}                 
	    batch.end();
		renderBoard();
	}
}
