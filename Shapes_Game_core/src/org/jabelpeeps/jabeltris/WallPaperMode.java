package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.shapes.CrossOneInverted;
import org.jabelpeeps.jabeltris.shapes.CrossTwoInverted;
import org.jabelpeeps.jabeltris.shapes.HorizontalLineInverted;
import org.jabelpeeps.jabeltris.shapes.SquareInverted;
import org.jabelpeeps.jabeltris.shapes.TriangleInverted;
import org.jabelpeeps.jabeltris.shapes.VerticalLineInverted;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class WallPaperMode extends LevelMaster {
	
	public WallPaperMode() {
		super();
		
		// make demo board fill the screen.
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		game = new PlayArea( 10 , (int) (10f*h/w) );
		game.baseColor = new Color(0.75f, 1f, 0.75f, 1f);
		game.initialise(this);
		logic = new DemoGameLogic(game);
//		Shape.clearHintVisitorList();
//		Shape.addHintVisitor( new StandardMoveHints() );
		Shape.addHintVisitor( new RotatingSquareHints() );
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
		levelIsFinished = true;
	}
	
	@Override
	public void render(float delta) {
		if ( !logic.isAlive() ) {
			core.setScreen(new MainMenu(core));	
			Gdx.graphics.requestRendering();
			dispose();
		}
		if ( logic.getBackKeyWasPressed() ) {
			logic.shutDown();
		}
		prepScreenAndCamera();
		renderBoard(1f);
	}
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(9) + 1 ) {
		
			case 1: 
				return new HorizontalLineInverted();
			case 2:
			case 8:
				return new CrossOneInverted();
			case 3:
			case 9:
				return new CrossTwoInverted();				
			case 4: 
			case 5:
				return new TriangleInverted();				
			case 6: 
				return new SquareInverted();
			case 7:
				return new VerticalLineInverted();
		}
		return null;
	}

	@Override
	protected void nextLevel() {
	}
	@Override
	protected void menuScreen() {
	}
}
