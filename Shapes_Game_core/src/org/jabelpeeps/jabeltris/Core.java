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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Core extends Game {
// --------------------------------------------------Fields------------	
	protected static SpriteBatch batch;
	protected static OrthographicCamera camera;
	static AssetManager manager;
	protected static BitmapFont font;
	static TextureAtlas atlas;
	final String MY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!?'.,;:()[]{}<>|/@\\^$-%+=#_&~* ¡£¦§«¬°±·»¼½¾×÷";
	static Texture boardBase;
	protected static TextureRegion[][] boardBaseTiles;
	protected static RandomXS128 rand = new RandomXS128();
		
	final static Pool<Vector2> vector2Pool = Pools.get(Vector2.class);
	protected static Matrix4 initialMatrix;	
	
// --------------------------------------------------Methods-----------
	@Override
	public void create () {
		batch = new SpriteBatch(500);
		atlas = new TextureAtlas();
		manager = new AssetManager();
		
		// Queue some jobs for AssetManager
		manager.load("pack.atlas", TextureAtlas.class);
		manager.load("board10x10.jpg", Texture.class);
		Texture.setAssetManager(Core.manager);
		
		// set world view as 42 units wide, and enough high to keep units square.
		// (standard play area will be 40x40 with a small border at the edges.)
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(42, 42*(h/w));
		
		// position camera so that 0,0 is at bottom left of the playboard position.
		// NB the coords defined in the .set() are for the centre of the view
        camera.position.set(20, 20, 0);  
        initialMatrix = camera.combined;
        
        // calls splash screen
        this.setScreen(new Splash(this));
	}
	
	public static Vector2 obtainVectorFromPool(int x, int y) {
		return Core.vector2Pool.obtain().set(x, y);
	}
	
	protected static void delay(long time) {
		try {  
			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		super.render();
	}	
	
	@Override
	public void dispose () {
		//this.setScreen(null);
		boardBase.dispose();
		atlas.dispose();
		manager.dispose();
		font.dispose();
		batch.dispose();
		//Gdx.app.exit();
		//System.exit(0);
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
