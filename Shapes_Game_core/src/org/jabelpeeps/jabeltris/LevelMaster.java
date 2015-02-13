package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.RandomXS128;

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
	
	final Core game;
	protected static GameLogic logic;
	protected static Thread logicThread;
	
	protected static RandomXS128 rand = Core.rand;
	protected int x = 10;
	protected int y = 10;
// ---------------------------------------------Constructor(s)--------	

	protected LevelMaster(Core g) {
		game = g;
		
		// turn off continuous rendering (to save battery on android)
		Gdx.graphics.setContinuousRendering(false);
	}
// ---------------------------------------------Methods----------
	protected void setLogic() {
		// instantiates the GameBoard
		logic = new GameLogic(x, y, this);
	}
	protected void setupInput(InputProcessor sole) {
		Gdx.input.setInputProcessor(sole);
	}
	protected void setupInput(InputProcessor first, InputProcessor second) {
		// setup the actions to respond to user input.
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(first);
		multiplexer.addProcessor(second);
		Gdx.input.setInputProcessor(multiplexer);
	}
	protected void startInteractiveLogicThread() {
		logicThread = new Thread(logic);
		logicThread.start();
	}
	protected void startDemoLogicThread() {
		logicThread = new Thread(new DemoGameLogic(logic));
		logicThread.start();
	}
	protected void prepScreenAndCamera() {
		Gdx.gl.glClearColor(0, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}
	protected void renderBoard() {
		renderBoard(1f);
	}
	protected void renderBoard(float alpha) {
		int x = logic.getXsize();
		int y = logic.getYsize();
		boolean batchStarted = false;
		if ( !batch.isDrawing() ) {
			batchStarted = true;
			batch.begin();
		}
		if ( alpha == 1f ) {
				batch.disableBlending();
				for( int i = 0; i < x; i++ ) {
			    	for( int j = 0; j < y; j++ ) {
		    			try {
		    				logic.getBoardTile(i, j).draw(batch);
					    } catch (NullPointerException e) { } 
					 }           
				}
			    batch.enableBlending();
			    for( int i = 0; i < x; i++ ) {
			    	for( int j = 0; j < y; j++ ) {
				    	try {
				    		logic.getShape(i, j).draw(batch);
					    } catch (NullPointerException e) { } 
			    	}           
				}
		} else {
				Sprite tmpSprite;
				Shape tmpShape;
				for( int i = 0; i < x; i++ ) {
			    	for( int j = 0; j < y; j++ ) {
		    			try {
		    				tmpSprite = logic.getBoardTile(i, j);
		    				tmpSprite.setAlpha(alpha);
		    				tmpSprite.draw(batch);
		    				tmpSprite.setAlpha(1f);
					    } catch (NullPointerException e) { } 
					 }           
				}
			    for( int i = 0; i < x; i++ ) {
			    	for( int j = 0; j < y; j++ ) {
				    	try {
				    		tmpShape = logic.getShape(i, j);
				    		tmpShape.setAlpha(alpha);
					    	tmpShape.draw(batch);
					    	tmpShape.setAlpha(1f);
					    } catch (NullPointerException e) { } 
			    	}           
				}
		}
	    if ( batchStarted ) batch.end();
	}
	
	protected static Shape setOriginAndBounds(Shape s, int x, int y) {
		s.setOrigin(2, 2);
		s.setScale(0.9f);
		s.setBounds(x*4, y*4, 4, 4);
		return s;
	}
	protected abstract Shape makeNewShape(int i, int j);

	public abstract LevelMaster nextLevel();

	public abstract boolean IsFinished();

	@Override
	public void show() {
	}
	@Override
	public void hide() {
	}
}	
