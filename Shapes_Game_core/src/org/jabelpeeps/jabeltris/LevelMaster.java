package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;

public abstract class LevelMaster extends Core implements Screen {

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
	
	protected final Core core;
	protected PlayArea game;
	protected GameLogic logic;
	
	protected static RandomXS128 rand = Core.rand;
	protected Vector3 touch = new Vector3();
	public Color baseColor;
// ---------------------------------------------Constructor(s)--------	

	protected LevelMaster(Core c) {
		core = c;
		
		// turn off continuous rendering (to save battery on android)
		Gdx.graphics.setContinuousRendering(false);
	}
// ---------------------------------------------Methods----------
	protected void setupInput(InputProcessor sole) {
		Gdx.input.setInputProcessor(sole);
		Gdx.input.setCatchBackKey(true);
	}
	protected void setupInput(InputProcessor...list) {
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
	protected void messageCentered(String message, int y) {
		font.drawWrapped(batch, message, 1, y, 38, BitmapFont.HAlignment.CENTER);
	}
	protected void messageLeft(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.LEFT);
	}
	protected void messageRight(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.RIGHT);
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
		Shape tmpShape = makeNewShape(x, y);
		tmpShape.setPlayArea(p);
		tmpShape.setOffsets(x_offset, y_offset);
		tmpShape.setOriginAndBounds(x , y);
		return tmpShape;
	}
	protected abstract Shape makeNewShape(int x, int y);
	
	public boolean IsFinished() {
		return false;
	}
	@Override
	public void show() {
	}
	@Override
	public void hide() {
	}
	@Override
	public void dispose() {
		game.dispose();
	}
}	
