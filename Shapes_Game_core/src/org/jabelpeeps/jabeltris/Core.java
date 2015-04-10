package org.jabelpeeps.jabeltris;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
 
public class Core extends Game {
// --------------------------------------------------Fields------------	
	protected final static String PACKAGE = "org.jabelpeeps.jabeltris.";
	public final static boolean LOGGING = false;
	public final static int[][] LEFT_UP_RIGHT_DOWN = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
	
	protected static SpriteBatch batch;
	protected static OrthographicCamera camera;
	protected static AssetManager manager;
	protected static BitmapFont font;
	protected static TextureAtlas atlas;
	protected final static String MY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
								+ "1234567890\"!?'.,;:()[]{}<>|/@\\^$-%+=#_&~* ¡£¦§«¬°±·»¼½¾×÷";
	protected static Texture boardBase;
	protected static TextureRegion[][] boardBaseTiles;
	protected static Preferences prefs;
	public static RandomXS128 rand = new RandomXS128();
	public static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
	protected static boolean previewMode;
	public static float bottomEdge, topEdge;
	
// --------------------------------------------------Methods-----------
	@Override
	public void create () {
		
		MyColors.setupColors();
		
		batch = new SpriteBatch(500);
		atlas = new TextureAtlas();
		manager = new AssetManager();
		prefs = Gdx.app.getPreferences("JabelTris.JabelPrefs.prefs");
		previewMode = prefs.getBoolean("previewMode");
		
		// Queue some jobs for AssetManager
		manager.load("pack.atlas", TextureAtlas.class);
		manager.load("board10x10.jpg", Texture.class);
		Texture.setAssetManager(manager);
		
		// set world view as 42 units wide, and enough high to keep units square.
		// (standard play area will be 40x40 with a small border at the edges.)
		float h = Gdx.graphics.getHeight();
		float w = Gdx.graphics.getWidth();
		
		float adjHeight = 42f * ( h / w );
		camera = new OrthographicCamera( 42 , adjHeight );
		bottomEdge = (adjHeight - 42) / -2;
		topEdge = (42 + (adjHeight - 42) / 2);		
		
		// position camera so that 0,0 is at bottom left of the playboard position.
		// NB the coords defined in the .set() are for the centre of the view
        camera.position.set(20, 20, 0); 
        
        // calls splash screen
        this.setScreen(new Splash(this));
	}
	public static void textCentre(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.CENTER);
	}
	public static void textLeft(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.LEFT);
	}
	public static void textRight(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.RIGHT);
	}
	public static void textInBounds(String message, float yMin, float yMax) {
		TextBounds text;
		float scale = 0.21f;
	
		do {
			scale -= 0.01f;
			font.setScale(scale);
			text = font.getWrappedBounds(message, 40);
		} while ( text.height > yMax - yMin );
		
		font.drawWrapped(batch, message, 0, yMax, 40, BitmapFont.HAlignment.CENTER);
		
		font.setScale(0.2f);
	}
	public static void textHeading(String message, float y) {
		font.setScale(0.25f);
		float textwidth = font.getBounds(message).width;
		
		String underline = "";
		TextBounds line;
		do  {
			underline = underline + "_";
			line = font.getBounds(underline);
		} while ( line.width < textwidth );

		textCentre(message, y);
		textCentre(underline, y - 1 );
		textCentre(message, y);
		font.setScale(0.2f);
	}
	public static boolean levelCompleted(String level) {
		return previewMode || prefs.getString(level).equals("Completed");
	}
	
	public static void delay(long time) {
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
	protected void exit() {
		Gdx.app.exit();
		dispose();
	}
	@Override
	public void dispose () {
		threadPool.shutdownNow();
		boardBase.dispose();
		atlas.dispose();
		manager.dispose();
		font.dispose();
		batch.dispose();
		screen.dispose();
	}
}
