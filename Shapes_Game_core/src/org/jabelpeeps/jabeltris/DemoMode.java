package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.shapes.CrossOneInverted;
import org.jabelpeeps.jabeltris.shapes.CrossTwoInverted;
import org.jabelpeeps.jabeltris.shapes.LineInverted;
import org.jabelpeeps.jabeltris.shapes.SquareInverted;
import org.jabelpeeps.jabeltris.shapes.TriangleInverted;

public class DemoMode extends LevelMaster {
	
	private Shape newShape;
	private float alpha = 0f;
	private String alphaDirection = "up";
	
	public DemoMode(Core g) {
		super(g);
		
//		// make demo board fill the screen.
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		x = 10;
//		y = (int) (10f*h/w);
//		camera.position.set(2*x, 2*y, 0);
		
		setLogic();
		setupInput(new BorderButtonsInput(null));
		startDemoLogicThread();
	}
	
	@Override
	public void render(float delta) {
		prepScreenAndCamera();
		
		if ( alphaDirection == "up" ) {
			alpha += 0.003f;
			if ( alpha >= 0.75f ) alphaDirection = "down";
		} else {
			alpha -= 0.003f;
			if ( alpha <= 0.2f ) alphaDirection = "up";
		}
						
//		batch.begin();
//		font.draw(batch, "This ", 4, 39);
//		font.draw(batch, "is ", 4, 35);
//		font.draw(batch, "testing ", 4, 31);
//		font.draw(batch, "putting ", 4, 27);
//		font.draw(batch, "text ", 4, 23);
//		font.draw(batch, "under ", 4, 19);
//		font.draw(batch, "a running ", 4, 15);
//		font.draw(batch, "demo game. ", 4, 11);
//		
//		font.draw(batch, "Alpha = " + alpha , 4, -8);
//		batch.end();
						
		renderBoard(alpha);

		batch.begin();
		font.draw(batch, "This ", 4, 39);
		font.draw(batch, "is ", 4, 35);
		font.draw(batch, "testing ", 4, 31);
		font.draw(batch, "putting ", 4, 27);
		font.draw(batch, "text ", 4, 23);
		font.draw(batch, "over ", 4, 19);
		font.draw(batch, "a running ", 4, 15);
		font.draw(batch, "demo game. ", 4, 11);
		
		font.draw(batch, "Alpha = " + alpha , 4, -8);
		batch.end();
	}
	
	@Override
	public Shape makeNewShape(int x, int y) {
		
		int option = rand.nextInt(5)+1;
		switch (option) {
			case 1:                       
				newShape = new LineInverted();
				break;
			case 2:                       
				newShape = new CrossOneInverted();
				break;
			case 3:                       
				newShape = new CrossTwoInverted();				
				break;
			case 4:                       
				newShape = new TriangleInverted();				
				break;
			case 5:                       
				newShape = new SquareInverted();				
				break;
		}
		Shape tmpShape = setOriginAndBounds(newShape, x , y);
		return tmpShape;
	}

	@Override
	public LevelMaster nextLevel() {
		return null;
	}
	@Override
	public boolean IsFinished() {
		return false;
	}

}
