package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayGame extends Core implements Screen {

// ---------------------------------------------Field(s)------------
	final Core game;
	final Master level;
	final GameBoard board;
	
// ---------------------------------------------Constructor(s)--------	
	
	public PlayGame(Core g, Master l) {
		game = g;
		level = l;
		
		// instantiate the GameBoard
		board = new GameBoard(level);
		
		// setup the actions to respond to user input.
		Gdx.input.setInputProcessor( new InputEvents(board) );
		
		Thread gameLogic = new Thread(board);
		gameLogic.start();
		
		// turn off continuous rendering (to save battery on android)
		Gdx.graphics.setContinuousRendering(false);
	}

// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
//		System.out.println("Continuous Rendering is:- " + Gdx.graphics.isContinuousRendering());
		
//		try {
//			wait();
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		
		// clear screen and update camera
	    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.disableBlending();
//	    background.draw(batch);
		
		for( int i=0; i<=9; i++ ) {
		    	for( int j=0; j<=9; j++ ) {
				    	baseSprites[i][j].draw(batch);
				 	}           
		}
	    batch.enableBlending();
	    
	    for( int i=0; i<=9; i++ ) {
		    	for( int j=0; j<=9; j++ ) {
				    	try {
					    	board.getShape(i, j).draw(batch);
					    } catch (NullPointerException e) {
					    	//e.printStackTrace();
					    } 
		    	}           
		}	                 
	    batch.end();
	}
	
	@Override
	public void hide() {
	}
	@Override
	public void show() {
	}
}
