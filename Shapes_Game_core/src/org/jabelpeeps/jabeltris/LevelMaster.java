package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.shapes.Blank;
import org.jabelpeeps.jabeltris.shapes.BlankMobile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public abstract class LevelMaster implements Screen, Serializable {

// ---------------------------------------------Field(s)------------
	// This Class is the root of the tree of level classes, is 
	// therefore a convenient place to store references
	// to the textures for all the shape types. 
	
	public static AtlasRegion line;
	public static AtlasRegion square;
	public static AtlasRegion triangle;
	public static AtlasRegion crossone;
	public static AtlasRegion crosstwo;
	public static AtlasRegion invcrone;
	public static AtlasRegion invcrtwo;
	public static AtlasRegion invline;
	public static AtlasRegion invsqr;
	public static AtlasRegion invtri;
	public static AtlasRegion greysqr;
	public static AtlasRegion greyline;
	public static AtlasRegion greytri;
	public static AtlasRegion greycrone;
	public static AtlasRegion greycrtwo;
	public static AtlasRegion horizgreyline;
	public static AtlasRegion fourbar;
	public static AtlasRegion mirrorell;
	public static AtlasRegion realell;
	public static AtlasRegion realsquare;
	public static AtlasRegion realt;
	public static AtlasRegion zigleft;
	public static AtlasRegion zigright;
	public static AtlasRegion horizline;
	public static AtlasRegion horizinvline;
	
	protected static Core core;
	protected PlayArea game;
	protected GameLogic logic;
	
	protected SpriteBatch batch;
	protected Camera camera;
	protected Preferences prefs;
	protected RandomXS128 rand;
	
	protected int levelStage = 1;
	protected float alpha = 0f;
	protected boolean playOn = true;
	protected boolean playingLearningLevels = false;
	protected boolean levelIsFinished = false;
	protected int score = 0, matches = 0;
	protected String title;
	protected String firstMessage;
	
	protected Vector3 touch = new Vector3();
// ---------------------------------------------Constructor--------	
	protected LevelMaster() {
		Gdx.graphics.setContinuousRendering( false );
		this.batch = Core.batch;
		this.camera = Core.camera;
		this.prefs = Core.prefs;
		this.rand  = Core.rand;
		GestureMultiplexer.getInstance().clear();
		Shape.clearHintVisitorList();
	}
// ---------------------------------------------Methods----------
	protected void setBlanks(int[][] blanks) {
		setBlanks( blanks , false );
	}
	protected void setBlanks(int[][] blanks, boolean mobile) {
		for ( int[] each : blanks ) 
			setBlanks( each[0] , each[1] , mobile );
	}
	protected void setBlanks(int x, int y, boolean mobile) {
		game.getBoardTile( x , y ).setAlpha( 0f );
		game.shapeTilePut( x , y , mobile ? new BlankMobile( game ) 
										  : Blank.get( game , false ) );
	}
	protected void setupInput(InputProcessor...list) {
		InputMultiplexer multi = (list == null) ? new InputMultiplexer()
												: new InputMultiplexer( list );
		multi.addProcessor( 0 , new KeyBoardInput( game, logic ) );
		multi.addProcessor( 1 , GestureMultiplexer.getInstance() );
		Gdx.input.setInputProcessor( multi );
		Gdx.input.setCatchBackKey( true );
	}
	protected void prepScreenAndCamera() {
		Gdx.gl.glClearColor( 0, 0, 0.3f, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}
	protected void cameraUnproject() {
		touch.set( Gdx.input.getX(), Gdx.input.getY(), 0 );
		camera.unproject(touch);
	}
	
	protected void renderBoard() {
		renderBoard(1f, game);
	}
	protected void renderBoard(float alpha) {
		renderBoard(alpha, game);
	}
	protected void renderBoard(PlayArea game) {
		renderBoard(1f, game);
	}
	protected void renderBoard(float alpha, PlayArea game) {
		if ( !game.playAreaIsReady() ) return;
		
		boolean batchStarted = false;
		if ( !batch.isDrawing() ) {
			batchStarted = true;
			batch.begin();
		}
		for ( Sprite each : game.getAllBoardTiles() ) 
			each.draw(batch, alpha);
		
		for ( Shape each : game.getAllShapes() ) 
	    	each.draw(batch, alpha);
	   
	    if ( batchStarted ) 
	    	batch.end();
	}
	
	Shape makeNewShape(int x, int y, int x_offset, int y_offset, PlayArea p) {
		return (Shape) getNewShape().setPlayArea( p ).setOffsets( x_offset , y_offset ).setOriginAndBounds( x , y );
	}
	protected abstract Shape getNewShape();				
	
	protected void recordCompleted() {
		prefs.putString( this.getClass().getSimpleName() , "Completed" );
		prefs.flush();
	}
	protected void levelNeedsFinishing() {
		
		if ( Gdx.input.justTouched() ) {
			cameraUnproject();
			
			if ( touch.y < 0 || logic.getTotalHints() == 0 ) {
				levelIsFinished = true;
				logic.shutDown();
			} 
	    }
	}
	protected void fadeOutAndReturnToMenu() {
		
		if ( !logic.isAlive() && alpha <= 0f ) {
			menuScreen();
			dispose();
		} 
		if ( levelStage != 0 ) {
			pause();
			logic.shutDown();
			levelStage = 0;
		} 
		alpha = (alpha > 0f) ? (alpha - 0.02f) : 0f ;
		prepScreenAndCamera();
		renderBoard( alpha );
		Gdx.graphics.requestRendering();
	}
	protected abstract void menuScreen();
	
	protected abstract void nextLevel();
	
	@Override
	public void hide() {
		if ( Core.LOGGING ) {
			System.out.println("mCalls = " + Shape.mCalls);
		}
		if ( !levelIsFinished && levelStage != 0 ) {
			levelStage = 0;
			pause();
			logic.shutDown();
			menuScreen();   
			dispose();   
		}
	}
	@Override
	public void dispose() {
		game.dispose();
	}
	@Override
	public void show() {
	}
	@Override
	public void resize(int width, int height) {
	}
	@Override
	public void resume() {
	}
	@Override
	public void pause() {
		if ( core.getScreen() instanceof WallPaperMode ) return;
		
		Json json = new Json();
		String savegame = json.prettyPrint( this );
		
		if ( Gdx.files.isLocalStorageAvailable() ) {
			FileHandle handle = Gdx.files.local( "savedLevel.sav" );
			handle.writeString( savegame, false );
		}
	}
	@Override
	public void write(Json json) {
		synchronized( logic ) {
			json.writeValue( "level", getClass().getSimpleName() );
			json.writeValue( "game", game );
			json.writeValue( "logic", logic );
			json.writeValue( "levelStage", levelStage );
			json.writeValue( "alpha", alpha );
			json.writeValue( "playOn", playOn );
			json.writeValue( "playingLearningLevels", playingLearningLevels );
		}
	}
	@Override
	public void read(Json json, JsonValue jsonData) {
		levelStage = jsonData.getInt( "levelStage" );
		alpha = jsonData.getFloat( "alpha" );
		playOn = jsonData.getBoolean( "playOn" );
		playingLearningLevels = jsonData.getBoolean( "playingLearningLevels" );
		
		game = new PlayArea();
		game.read( json, jsonData.get( "game" ) );
		logic = new GameLogic(game, this);
		logic.read( json, jsonData.get( "logic" ) );
		
		game.initialise( this, logic );
		game.refillShapeTile();
		
		try {
			blanksearch:
			for ( Field each : ClassReflection.getDeclaredFields( this.getClass() ) ) {
				
				if ( each.getName().equals( "blanks" ) ) {
					Field blanksref = ClassReflection.getDeclaredField( this.getClass() , "blanks" );
					blanksref.setAccessible( true );
					int[][] blanksfound = (int[][]) blanksref.get( this );
					setBlanks( blanksfound );
					break blanksearch;
				}
			}
		} catch ( ReflectionException e ) {  e.printStackTrace(); }
		
		game.setPlayAreaReady();
		setupInput();
		logic.start();
    }
}	
