package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.SquareBlue;
import org.jabelpeeps.jabeltris.shapes.SquareRed;
import org.jabelpeeps.jabeltris.shapes.SquareYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

public class TrainingLevel2 extends LevelMaster {

	private Shape newShape;
	private int demoStage = 1;
	private Vector3 touch = new Vector3();
	
// ---------------------------------------------Constructor--------		
	public TrainingLevel2(Core c) {
		super(c);
		baseColor = new Color(1f, 1f, 1f, 1f);
		x = 6;
		y = 6;
		initPlayArea(this);
		logic = new DemoGameLogic(game);
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}

// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		if ( !logic.isAlive() && demoStage < 3 ) {
			core.setScreen(new MainMenu(core, 5));
		}
		switch ( demoStage ) {
		case 1:
			prepScreenAndCamera();
			renderBoard(0.6f);
			batch.begin();
			font.drawWrapped(batch, "Level 1 - Squares\n\n"
					+ "Squares match when placed in square groups.\n\n"
					+ "AutoPlay is showing you how this works."
					, 2, 48, 37, BitmapFont.HAlignment.LEFT);
			font.drawWrapped(batch, "Touch here remove the text."
					,2 ,0 , 37, BitmapFont.HAlignment.LEFT );
			batch.end();
			if ( Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y < 0 ) {
					demoStage = 2;
				}
			}
			Gdx.graphics.requestRendering();
			break;
		case 2:
			prepScreenAndCamera();
			renderBoard(0.8f);
			batch.begin();
			font.drawWrapped(batch, "Touch the board to take over."
					,2 ,0 , 37, BitmapFont.HAlignment.LEFT );
			batch.end();
			if ( Gdx.input.isTouched() ) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				Core.camera.unproject(touch);
				if ( touch.y > 8 ) {
					demoStage = 3;
				}
			}
			Gdx.graphics.requestRendering();
			break;
		case 3:
			prepScreenAndCamera();
			renderBoard(0.9f);
			batch.begin();
			font.drawWrapped(batch, "Ending AutoPlay..."
					,2 ,0 , 37, BitmapFont.HAlignment.CENTER );
			batch.end();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 4:
			Core.delay(300);
			prepScreenAndCamera();
			renderBoard();
			logic.endDemo();
			while ( logic.isAlive() ) {
				Core.delay(50);
			}
			logic = new InteractiveGameLogic(game);
			logic.setEndlessPlayModeOn();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 5:
			prepScreenAndCamera();
			renderBoard();
			batch.begin();
			font.drawWrapped(batch, "Get Ready to Play..."
					,2 ,0 , 37, BitmapFont.HAlignment.CENTER );
			batch.end();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 6:
			Core.delay(800);
			prepScreenAndCamera();
			renderBoard();
			batch.begin();
			font.drawWrapped(batch, "Go!"
					,2 ,0 , 37, BitmapFont.HAlignment.CENTER );
			batch.end();
			setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
			logic.start();
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 7:
			Core.delay(600);
			demoStage++;
			Gdx.graphics.requestRendering();
			break;
		case 8:	
			prepScreenAndCamera();
			batch.begin();
			font.draw(batch, "Sets Matched:- " + game.getTotalMatches(), 6, 46);
			font.draw(batch, "Target:- 20", 10, 40);
			font.drawWrapped(batch, "Touch and drag shapes where you want them to go."
					, 2, 4, 36, BitmapFont.HAlignment.CENTER);
		    batch.end();
		    renderBoard();
		}		                 
	}

	@Override
	public Shape makeNewShape(int x, int y) {
				
		int option = rand.nextInt(3) + 1;
		switch (option) {
			case 1:                       
				newShape = new SquareRed();
				break;
			case 2:                       
				newShape = new SquareBlue();
				break;
			case 3:                       
				newShape = new SquareYellow();				
				break;
		}
		Shape tmpShape = setOriginAndBounds(newShape, x , y);
		return tmpShape;
	}
	@Override
	public boolean IsFinished() {
	//	if ( game.getHintListSize() <= 0 ) {
	//		System.out.println("No Further Moves.");
	//		return true;
	//	}
		return false;
	}
	@Override
	public LevelMaster nextLevel() {
			return null;
	}
}
