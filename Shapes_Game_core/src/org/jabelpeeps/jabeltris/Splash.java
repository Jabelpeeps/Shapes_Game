package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.levels.Level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Splash extends Core implements Screen {
	
// -------------------------------------------------Fields---------	
	final Core game;
	Sprite logo;
	Master level = new Level1();
	private boolean nowLoaded = false;
	float logoScale = 0.1f;
// ------------------------------------------------Constructor-----
	Splash(final Core g) {
		game = g;

		Core.manager.load("pack.atlas", TextureAtlas.class);
		Core.manager.load("board10x10.jpg", Texture.class);
		Texture.setAssetManager(Core.manager);
		
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
				
				camera.update();
				batch.setProjectionMatrix(camera.combined);
				
//				background = new Sprite(base);
//				background.setSize(30, 30);
//				background.setPosition(0, 0);

				boardBaseTiles = TextureRegion.split(boardBase, 51, 51);
				
				// we are done loading, let's move on..
				nowLoaded = true;
				
				// turn off continuous rendering (to save battery) 
				Gdx.graphics.setContinuousRendering(false);
				Gdx.graphics.requestRendering();
			
		} else if ( nowLoaded && !Gdx.input.isTouched() ) {
				Gdx.graphics.requestRendering();
				
		} else if ( nowLoaded && Gdx.input.isTouched() ) {
				game.setScreen(new PlayGame(game, level));
			    this.dispose();
		} 	
	    logo.setScale(logoScale);
		if (logoScale < 1 ) {
				logoScale += 0.1f;
		}
		// displays splash image while AssetManager is loading.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.disableBlending();
		logo.draw(batch);
		batch.enableBlending();
		
		// when loaded, let's display some prompts...
		if ( nowLoaded ) {
			font.setUseIntegerPositions(false);
			font.setScale(0.2f);
			font.draw(batch, "Loading Complete!", 8, -4);
			font.draw(batch, "Tap Screen to Play!", 7, -8);
		}
		batch.end();
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
