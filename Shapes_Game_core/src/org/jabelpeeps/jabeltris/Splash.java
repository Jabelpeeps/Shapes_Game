package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Splash extends Core implements Screen {
	
	// -------------------------------------------------Fields---------	
		final Core game;
		Sprite logo;
		private boolean loaded;
		Texture board;
		
	// ------------------------------------------------Constructor-----
		
		Splash(final Core g) {
			this.game = g;

	        logo = new Sprite(new Texture("badlogic.jpg"));  
			logo.setSize(30, 30);
			logo.setPosition(0, 0);
			
			loaded = false;
		}

	// ------------------------------------------------Methods---------	
		
		@Override
		public void render(float delta) {
		    if (!game.manager.update()) {
		    	
			    // displays splash image while AssetManager is loading.
			    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				game.camera.update();
				game.batch.setProjectionMatrix(game.camera.combined);
				game.batch.begin();
				logo.draw(game.batch);
				game.batch.end();
				
			} else if (!loaded) {
				// a few extra setup items...
				
				// turn off continuous rendering (to save battery) 
//				Gdx.graphics.setContinuousRendering(false);
				
				// unpack the resources from the asset manager
				game.atlas = game.manager.get("pack.atlas");
				board = game.manager.get("board10x10.png");

				Core.line = game.atlas.createSprite("line");
				Core.square = game.atlas.createSprite("square");
				Core.triangle = game.atlas.createSprite("triangle");
				Core.crossone = game.atlas.createSprite("crossone");
				Core.crosstwo = game.atlas.createSprite("crosstwo");
				
				Core.background = new Sprite(board);
				Core.background.setSize(30, 30);
				Core.background.setPosition(0, 0);
				
				// and finally, display some prompts...
				game.camera.update();
				game.batch.setProjectionMatrix(game.camera.combined);
				game.batch.begin();
				game.font.setUseIntegerPositions(false);
				game.font.setScale(0.15f);
				game.font.draw(game.batch, "Loading Complete!", 6, -3);
				game.font.draw(game.batch, "Tap Screen to Play!", 6, -6);
				game.batch.end();
				
				// we are done loading, let's move on..
				loaded = true;
			} else {
				if (Gdx.input.isTouched()) {
					game.setScreen(new GameBoard(game));
//					dispose();
				}
			}	
			// REMEMBER to request renders with:-
			//    	Gdx.graphics.requestRendering();
			// (also triggered by input events)
		}

	// -------------------------------------------------Empty Methods--------
		
		public static void main(String[] args) {
			// testing code goes here
		}
		
		@Override
		public void hide() {
		}
		
		@Override
		public void show() {
		}
		
	// ----------------------------------------------End-of-Class--------
	}
