package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.FourSwap;
import org.jabelpeeps.jabeltris.FourSwapInput;
import org.jabelpeeps.jabeltris.GameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.SelectShape;
import org.jabelpeeps.jabeltris.TwoSwap;
import org.jabelpeeps.jabeltris.TwoSwapInput;

import com.badlogic.gdx.graphics.Color;

public abstract class EndlessGame extends LevelMaster {
	
	public EndlessGame() {
		super();
	}
	protected void initialise(Color baseColor) {
		game = new PlayArea();
		game.baseColor = baseColor;
		logic = new GameLogic(game, this);
		game.initialise(this, logic);
		logic.addGameMechanics( new TwoSwap(game, logic),
						  		new FourSwap(game, logic) );
		logic.endlessPlayMode = true;
		setupInput(	new BorderButtonsInput(game, logic),
					new FourSwapInput(game, logic),
					new SelectShape(game, logic),
					new TwoSwapInput(game, logic) );
		logic.start();
	}
	
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			fadeOutAndReturnToMenu();	
			return;
		}
		prepScreenAndCamera();
		int hints = logic.getTotalHints();
		
		batch.begin();
		Core.textCentre( (hints > 0)? "Possible moves:- " + hints
							   		: "No moves left\nShuffling!"  , 48);
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
