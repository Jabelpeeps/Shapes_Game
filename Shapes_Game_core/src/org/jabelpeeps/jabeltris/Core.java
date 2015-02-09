package org.jabelpeeps.jabeltris;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.RandomXS128;

public class Core extends Game {
// --------------------------------------------------Fields------------	
	static SpriteBatch batch;
	static OrthographicCamera camera;
	static AssetManager manager;
	static BitmapFont font;
	static TextureAtlas atlas;
	final String MY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!?'.,;:()[]{}<>|/@\\^$-%+=#_&~* ¡£¦§«¬°±·»¼½¾×÷";
	static Texture boardBase;
	protected static TextureRegion[][] boardBaseTiles;
	protected static RandomXS128 rand = new RandomXS128();
		
// --------------------------------------------------Override Methods--	
	@Override
	public void create () {
		batch = new SpriteBatch(500);
		atlas = new TextureAtlas();
		manager = new AssetManager();
		
		// Queue some jobs for AssetManager
		manager.load("pack.atlas", TextureAtlas.class);
		manager.load("board10x10.jpg", Texture.class);
		Texture.setAssetManager(Core.manager);
		
		// Font Loader
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ComfortaaRegular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 18;
		parameter.characters = MY_CHARS;
		parameter.packer = null;
		parameter.genMipMaps = true;
		parameter.minFilter = TextureFilter.MipMapLinearLinear;
		parameter.magFilter = TextureFilter.Linear;
		font = generator.generateFont(parameter); 
		generator.dispose();  // don't forget to dispose to avoid memory leaks!
		font.setScale(0.2f);
		font.setUseIntegerPositions(false);
				
		// set world view as 42 units wide, and enough high to keep units square.
		// actual play area will be 40x40 with a small border at the edges.
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(42, 42*(h/w));
				
		// position camera so that 0,0 is at bottom left of the playboard position.
		// NB the coords defined in the .set() are for the centre of the view
        camera.position.set(20, 20, 0);  
        
        // calls splash screen
        this.setScreen(new Splash(this));
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
