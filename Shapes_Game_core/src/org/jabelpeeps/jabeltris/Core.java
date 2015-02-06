package org.jabelpeeps.jabeltris;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Core extends Game {
// --------------------------------------------------Fields------------	
	static SpriteBatch batch;
	static OrthographicCamera camera;
	static AssetManager manager;
	static BitmapFont font;
	static TextureAtlas atlas;
	
	static Texture boardBase;
	protected static TextureRegion[][] boardBaseTiles;
		
// --------------------------------------------------Override Methods--	
	@Override
	public void create () {
		batch = new SpriteBatch(500);
		font = new BitmapFont();
		atlas = new TextureAtlas();
		manager = new AssetManager();
		
//		font.setUseIntegerPositions(false);
//		font.setScale(0.2f);
				
		// set world view as 42 units wide, and enough high to keep units square.
		// actual play area will be 40x40 with a small border at the edges.
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(42, 42*(h/w));
				
		// position camera, so that 0,0 is at bottom left of the playboard position.
        camera.position.set((camera.viewportWidth/2)-1, (camera.viewportHeight-21*(h/w))/2, 0);
        
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
		boardBase.dispose();
	}

	protected static void delay(long time) {
		try {  
			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}
	
//		System.out.println("delay() called by: " + getMethodName(1) 
//										  + ", " + getMethodName(2) 
//										  + ", " + getMethodName(3) 
//										  + ", " + getMethodName(4));
	
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
