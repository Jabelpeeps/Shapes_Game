package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.levels.Level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Splash extends Core implements Screen {
	
// -------------------------------------------------Fields---------	
	private final Core game;
	private Sprite logo;
	Master level = new Level1();
	private boolean nowLoaded = false;
	private float logoScale = 0.1f;
	private String logoScaling = "up";
// ------------------------------------------------Constructor-----
	Splash(final Core g) {
		game = g;

        logo = new Sprite(new Texture("badlogic.jpg"));  
		logo.setOrigin(20, 20);
		logo.setBounds(0, 0, 40, 40);
	}
// ------------------------------------------------Methods---------	
	
	@Override
	public void render(float delta) {
		
		if ( manager.update() && !nowLoaded ) {
	    					
				// unpack the resources from the asset manager
				atlas = manager.get("pack.atlas");
				boardBase = manager.get("board10x10.jpg");
	
				Master.line = atlas.findRegion("line");
				Master.square = atlas.findRegion("square");
				Master.triangle = atlas.findRegion("triangle");
				Master.crossone = atlas.findRegion("crossone");
				Master.crosstwo = atlas.findRegion("crosstwo");
				

				boardBaseTiles = TextureRegion.split(boardBase, 51, 51);
				
				// we are done loading, let's move on..
				nowLoaded = true;
				
		} else if ( nowLoaded && Gdx.input.isTouched() ) {
				game.setScreen(new PlayGame(game, level));
				dispose();
		} 	
	    
		// displays splash image while AssetManager is loading.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glAttachShader(program, shader);
		
	    
		
		// when loaded, display some prompts...
		if ( nowLoaded && logoScale < 0f ) {    
			batch.begin();
			font.draw(batch, "Loading Complete!", 4, 24);
			font.draw(batch, "Touch Screen to Play!", 1, 16);
			batch.end();
			
			// turn off continuous rendering (to save battery) 
			Gdx.graphics.setContinuousRendering(false);
			
		} else {
			logo.setScale(logoScale);
		    if ( logoScale >= 1f ) {
		    		Core.delay(400);
		    		logoScaling = "down";
		    }
			if ( logoScaling == "up" ) {
					logoScale += 0.1f;
			} else {
					logoScale -= 0.1f;
			}
			Core.delay(60);
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			
			batch.begin();
			batch.disableBlending();
			logo.draw(batch);
			batch.enableBlending();
			batch.end();
		 }
	}	
				// REMEMBER to request renders with:-
				//    	Gdx.graphics.requestRendering();
				// (also triggered by input events)
	
// -------------------------------------------------Empty Methods--------
	@Override
	public void hide() {
	}
	@Override
	public void show() {
	}
	@Override
	public void dispose() {
	}
// ----------------------------------------------End-of-Class--------
}
