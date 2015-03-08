package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public abstract class LevelMaster extends Core implements Screen, Serializable {

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

	protected int levelStage = 1;
	protected float alpha = 0f;
	protected boolean playOn = true;
	protected boolean playingLearningLevels = false;
	protected boolean levelIsFinished = false;
	protected int score = 0, matches = 0;
	protected String title;
	protected String firstMessage;
	
	protected static RandomXS128 rand = Core.rand;
	protected Vector3 touch = new Vector3();
	public Color baseColor;
// ---------------------------------------------Constructor--------	
	protected LevelMaster() {
		// turn off continuous rendering (to save battery on android)
		Gdx.graphics.setContinuousRendering(false);
	}
// ---------------------------------------------Methods----------
	protected void setupInput(InputProcessor sole) {
		Gdx.input.setInputProcessor(sole);
		Gdx.input.setCatchBackKey(true);
	}
	protected void setupInput(InputProcessor...list) {
		setupInput(new Array<InputProcessor>(list));
	}
	protected void setupInput(Array<InputProcessor> list) { 
		InputMultiplexer multiplexer = new InputMultiplexer();
		for ( InputProcessor each : list) {
			multiplexer.addProcessor(each);
		}
		Gdx.input.setInputProcessor(multiplexer);
		Gdx.input.setCatchBackKey(true);
	}
	protected void prepScreenAndCamera() {
		Gdx.gl.glClearColor(0, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}
	
	protected void renderBoard() {
		renderBoard(1f, game);
	}
	protected void renderBoard(float alpha) {
		renderBoard(alpha, game);
	}
	protected void renderBoard(PlayArea p) {
		renderBoard(1f, p);
	}
	protected void renderBoard(float alpha, PlayArea p) {
		if (!p.playAreaIsReady() ) return;
		
		boolean batchStarted = false;
		if ( !batch.isDrawing() ) {
				batchStarted = true;
				batch.begin();
		}
		Sprite[] boardTiles = p.getAllBoardTiles();
		for ( Sprite each : boardTiles ) {
			each.draw(batch, alpha);
		}
		Shape[] gameShapes = p.getAllShapes();
		for ( Shape each : gameShapes ) {
	    	each.draw(batch, alpha);
	    }
	    if ( batchStarted ) batch.end();
	}
	
	Shape makeNewShape(int x, int y, int x_offset, int y_offset, PlayArea p) {
		return makeNewShape(x, y).setPlayArea(p).setOffsets(x_offset, y_offset).setOriginAndBounds(x , y);
	}
	
	protected abstract Shape makeNewShape(int x, int y);
	
	public boolean IsFinished() {
		return false;
	}
	@Override
	public void write(Json json) {
		json.writeValue("level", getClass().getName());
		json.writeValue("game", game);
		json.writeValue("levelStage", levelStage);
		json.writeValue("alpha", alpha);
		json.writeValue("playOn", playOn);
		json.writeValue("playingLearningLevels", playingLearningLevels);
		json.writeValue("logic", logic.getClass().getName());
		json.writeValue("endlessPlayMode", logic.getEndlessPlayMode());
		
		if ( Gdx.input.getInputProcessor() instanceof InputMultiplexer ) {
			Array<InputProcessor> inputs = ((InputMultiplexer)Gdx.input.getInputProcessor()).getProcessors();
			json.writeArrayStart("input");
			for ( InputProcessor each : inputs ) {
				json.writeValue(each.getClass().getName());
			}
			json.writeArrayEnd();
		} else {
			json.writeValue("input", Gdx.input.getInputProcessor().getClass().getName());
		}
	}
	@Override
	public void read(Json json, JsonValue jsonData) {
		levelStage = jsonData.getInt("levelStage");
		alpha = jsonData.getFloat("alpha");
		playOn = jsonData.getBoolean("playOn");
		playingLearningLevels = jsonData.getBoolean("playingLearningLevels");
		
		game = new PlayArea();
		game.read(json, jsonData.get("game"));
		game.initialise(this);
		game.setupBoard();
		game.setupShapeTile();
		game.setPlayAreaReady();
		
		try {
			if ( ClassReflection.getDeclaredField(this.getClass(), "blanks") != null ) {
				Field blanksref = ClassReflection.getDeclaredField(this.getClass(), "blanks");
				blanksref.setAccessible(true);
				int[][] blanks = (int[][]) blanksref.get(this);
				game.setBlanks(blanks);
			}
			
			Class<?> logicclass = ClassReflection.forName(jsonData.getString("logic"));
			Constructor logicconstructor = ClassReflection.getConstructor(logicclass, PlayArea.class);
			logic = (GameLogic) logicconstructor.newInstance(game);
						
			if ( jsonData.get("input").isArray() ) {
				String[] inputs = jsonData.get("input").asStringArray();
				Array<InputProcessor> listofinputs = new Array<InputProcessor>(2);
				
				for (String each : inputs) {
					Class<?> inputclass = ClassReflection.forName(each);
					Constructor inputconstructor = ClassReflection.getConstructor(inputclass, PlayArea.class, GameLogic.class);
					
					listofinputs.add((InputProcessor) inputconstructor.newInstance(game, logic));
				}
				setupInput( listofinputs );
				
			} else {
				Class<?> inputclass = ClassReflection.forName(jsonData.getString("input")); 
				Constructor inputconstructor = ClassReflection.getConstructor(inputclass, PlayArea.class, GameLogic.class);
				
				setupInput((InputProcessor) inputconstructor.newInstance(game, logic));
			}	
			
		} catch (ReflectionException e) {  e.printStackTrace();	}
		
		game.findHintsOnBoard();
		game.spinShapesIntoPlace();
		
		logic.setEndlessPlayMode(jsonData.getBoolean("endlessPlayMode"));
		logic.handOverBoard = true;
		logic.start();
	}
	@Override
	public void show() {
//		System.out.println("show() in LevelMaster called");
	}
	@Override
	public void hide() {
//		System.out.println("hide() in LevelMaster called");
	}
	@Override
	public void dispose() {
//		System.out.println("dispose() in LevelMaster called");
		game.dispose();
	}
}	
