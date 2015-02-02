package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;

public class Core extends Game {
	// --------------------------------------------------Fields------------	
		SpriteBatch batch;
		OrthographicCamera camera;
		AssetManager manager;
		BitmapFont font;
		TextureAtlas atlas;

		static RandomXS128 rand = new RandomXS128();
		
		static Sprite line;
		static Sprite square;
		static Sprite triangle;
		static Sprite crossone;
		static Sprite crosstwo;
		static Sprite background;
		static Sprite[][] base;
		
	// --------------------------------------------------Override Methods--	
		@Override
		public void create () {
			batch = new SpriteBatch(1500);
			font = new BitmapFont();
			atlas = new TextureAtlas();
			manager = new AssetManager();
			manager.load("pack.atlas", TextureAtlas.class);
			manager.load("board10x10.png", Texture.class);
			Texture.setAssetManager(manager);
			
			// set world view as 32 units wide, and enough high to keep units square.
			// actual play area will be 30x30 with a small border at the edges.
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			camera = new OrthographicCamera(32, 32*(h/w));
					
			// position camera, so that 0,0 is at bottom left of the playboard position.
	        camera.position.set((camera.viewportWidth/2)-1, (camera.viewportHeight-16*(h/w))/2, 0);
	        
	        // calls splash screen
	        this.setScreen(new Splash(this));
		}

		@Override
		public void render () {
			super.render();
		}	
		
		@Override
		public void dispose () {
			batch.dispose();	
			atlas.dispose();
			manager.dispose();
			font.dispose();	
		}
		
//		/**
//		 * Get the method name for a depth in call stack. <br />
//		 * Utility function
//		 * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
//		 * @return method name
//		 */
//		public static String getMethodName(int depth) {
//			StackTraceElement[] ste;
//			ste = Thread.currentThread().getStackTrace();
	//
//			return ste[ste.length - 1 - depth].getMethodName(); 
//		}
		
	}
