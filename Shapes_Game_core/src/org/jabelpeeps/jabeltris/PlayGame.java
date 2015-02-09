package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayGame extends Core implements Screen {

// ---------------------------------------------Field(s)------------
	final Core game;
	final Master level;
	static GameLogic logic;
	static Thread gameLogic;
	
// ---------------------------------------------Constructor(s)--------	
	
	public PlayGame(Core g, Master l) {
		game = g;
		level = l;
		
		// instantiate the GameBoard
		logic = new GameLogic(level);
		
		// setup the actions to respond to user input.
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor( new BorderButtonsInput(logic) );
		multiplexer.addProcessor( new PlayAreaInput(logic) );
		Gdx.input.setInputProcessor(multiplexer);
				
		gameLogic = new Thread(logic);
		gameLogic.start();
		
		// turn off continuous rendering (to save battery on android)
		Gdx.graphics.setContinuousRendering(false);
	}

// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		
		if ( !gameLogic.isAlive() ) {
			font.setScale(0.3f);
			batch.begin();
			font.draw(batch, "GAME", 10, -2);
			font.draw(batch, "OVER", 10, -8);
			batch.end();
			return;
		}
				
		// clear screen and update camera
	    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();

		font.draw(batch, "Score:- " + logic.getScore(), 4, 50);
		font.draw(batch, "Possible moves:- " + logic.getNumberOfHints(), 4, 46);
		
		batch.disableBlending();
		for( int i = 0; i < logic.getXsize(); i++ ) {
		    	for( int j = 0; j < logic.getYsize(); j++ ) {
				    	logic.boardTile[i][j].draw(batch);
				 	}           
		}
	    batch.enableBlending();
	    
	    for( int i = 0; i < logic.getXsize(); i++ ) {
		    	for( int j = 0; j < logic.getYsize(); j++ ) {
				    	try {
					    	logic.getShape(i, j).draw(batch);
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
