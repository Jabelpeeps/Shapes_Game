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
		logo.setOrigin(15, 15);
		logo.setBounds(0, 0, 30, 30);
	}
// ------------------------------------------------Methods---------	
	
	@Override
	public void render(float delta) {
		
		if ( manager.update() && !nowLoaded ) {
	    					
				// unpack the resources from the asset manager
				atlas = manager.get("pack.atlas");
				base = manager.get("board10x10.jpg");
	
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

				baseTiles = TextureRegion.split(base, 51, 51);
				
				for( int i=0; i<=9; i++ ) {
			    	for( int j=0; j<=9; j++ ) {
					    baseSprites[i][j] = new Sprite(baseTiles[i][j]);
					    baseSprites[i][j].setBounds(i*3, j*3, 3, 3);
					}           
				}
				
				// and finally, display some prompts...
				
				batch.begin();
				font.setUseIntegerPositions(false);
				font.setScale(0.15f);
				font.draw(batch, "Loading Complete!", 6f, -3f);
				font.draw(batch, "Tap Screen to Play!", 5.5f, -6f);
				
				batch.end();
				
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
