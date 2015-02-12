package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.PlayAreaInput;

public abstract class EndlessGame extends LevelMaster {
	
// ---------------------------------------------Constructor(s)--------	
	public EndlessGame(Core g) {
		super(g);

		setLogic();
		logic.setEndlessPlayMode();
		setupInput(new BorderButtonsInput(logic), new PlayAreaInput(logic));
		startInteractiveLogicThread();
	}
// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		prepScreenAndCamera();
		synchronized ( logic ) {
			int hints = logic.getNumberOfHints();
			batch.begin();
			if ( hints > 0 ) {
				font.draw(batch, "Possible moves:- " + hints, 4, 48);
			} else {
				font.draw(batch, logic.getMessage(), 4, 48);
			}                 
		    batch.end();
			renderBoard();
		}
	}
}
