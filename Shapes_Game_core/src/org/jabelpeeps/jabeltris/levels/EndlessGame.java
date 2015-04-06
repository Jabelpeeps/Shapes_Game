package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.FourSwapInput;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.SelectShape;
import org.jabelpeeps.jabeltris.Shape;

import com.badlogic.gdx.graphics.Color;

public abstract class EndlessGame extends LevelMaster {
	
	public EndlessGame() {
		super();
//		Shape.addHintVisitor( new StandardMoveHints() );
		Shape.addHintVisitor( new RotatingSquareHints() );
	}
	protected void initialise(Color baseColor) {
		game = new PlayArea();
		game.baseColor = baseColor;
		game.initialise(this);
		logic = new InteractiveGameLogic(game);
		logic.setEndlessPlayMode(true);
		setupInput(	new BorderButtonsInput(game, logic),
					new FourSwapInput(game, logic),
					new SelectShape(game, logic)//,
//					new TwoSwapInput(game, logic) 
					);
		logic.start();
	}
	
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			fadeOutAndReturnToMenu();	
			return;
		}
		prepScreenAndCamera();
		int hints = game.getHintListSize();
		
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
