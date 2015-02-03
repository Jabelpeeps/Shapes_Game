package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayScreen extends Core implements Screen {

// ---------------------------------------------Field(s)------------
	final Core game;
	final Master masterShape;
	
	static InputAdapter inputEvents;
	static GameBoard board;
	
// ---------------------------------------------Constructor(s)--------	
	
	public PlayScreen(Core g, Master master) {
		game = g;
		masterShape = master;
		
		// instantiate the GameBoard
		board = new GameBoard(masterShape);
		
		// setup the actions to respond to user input.
		inputEvents = new InputEvents(board);
		Gdx.input.setInputProcessor(inputEvents);
		
		// turn off continuous rendering (to save battery on android) 
		Gdx.graphics.setContinuousRendering(false);
	}

// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		// clear screen and update camera
	    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Core.camera.update();
		Core.batch.setProjectionMatrix(Core.camera.combined);
		
		Core.batch.begin();
		Core.batch.disableBlending();
	    Core.background.draw(Core.batch);
	    Core.batch.enableBlending();
	    
	    for( int i=0; i<=9; i++ ) {
	    	for( int j=0; j<=9; j++ ) {
		    	board.getShape(i, j).draw(Core.batch);
	    	}           
	    }               
	    Core.batch.end();
	}
	
	@Override
	public void hide() {
	}
	@Override
	public void show() {
	}
}
