package org.jabelpeeps.jabeltris;

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
	Texture board;
	Master level = new Level1();
	private boolean loaded = false;
	
// ------------------------------------------------Constructor-----
	
	Splash(final Core g) {
		this.game = g;

		Core.manager.load("pack.atlas", TextureAtlas.class);
		Core.manager.load("board10x10.png", Texture.class);
		Texture.setAssetManager(Core.manager);
		
        logo = new Sprite(new Texture("badlogic.jpg"));  
		logo.setSize(30, 30);
		logo.setPosition(0, 0);
		
		// displays splash image while AssetManager is loading.
	    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Core.camera.update();
		Core.batch.setProjectionMatrix(Core.camera.combined);
		
		Core.batch.begin();
		logo.draw(Core.batch);
		Core.batch.end();
	}
// ------------------------------------------------Methods---------	
	
	@Override
	public void render(float delta) {
	    if (!Core.manager.update()) {

		} else if ( !loaded ) {
			// a few extra setup items...
			
			// unpack the resources from the asset manager
			Core.atlas = Core.manager.get("pack.atlas");
			board = Core.manager.get("board10x10.png");

			Master.line = Core.atlas.findRegion("line");
			Master.square = Core.atlas.findRegion("square");
			Master.triangle = Core.atlas.findRegion("triangle");
			Master.crossone = Core.atlas.findRegion("crossone");
			Master.crosstwo = Core.atlas.findRegion("crosstwo");
			
			Core.background = new Sprite(board);
			Core.background.setSize(30, 30);
			Core.background.setPosition(0, 0);
			
			@SuppressWarnings("unused")
			TextureRegion[][] baseTiles = TextureRegion.split(board, 51, 51);
			
			// and finally, display some prompts...
			Core.camera.update();
			Core.batch.setProjectionMatrix(Core.camera.combined);
			
			Core.batch.begin();
			Core.font.draw(Core.batch, "Loading Complete!", 6f, -3f);
			Core.font.draw(Core.batch, "Tap Screen to Play!", 5.5f, -6f);
			Core.batch.end();
			
			// we are done loading, let's move on..
			loaded = true;
			
		} else {
			// turn off continuous rendering (to save battery) 
			Gdx.graphics.setContinuousRendering(false);
			
			if (Gdx.input.isTouched()) {
				game.setScreen(new PlayScreen(game, level));
//					dispose();
			}
		}	
				// REMEMBER to request renders with:-
				//    	Gdx.graphics.requestRendering();
				// (also triggered by input events)
	}

// -------------------------------------------------Empty Methods--------
	
	@Override
	public void hide() {
	}
	
	@Override
	public void show() {
	}
	
	@Override
	public void dispose() {
		board.dispose();
	}
	
// ----------------------------------------------End-of-Class--------
}
