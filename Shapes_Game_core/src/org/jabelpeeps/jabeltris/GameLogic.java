package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameLogic implements Runnable {

// ---------------------------------------------Fields------------
	private final LevelMaster level;
	private static Sprite[][] boardTile;
	private static Shape[][] shapeTile;
	private static int x_size, y_size;
	private Array<Shape> matchList = new Array<Shape>(false, 32);
	private Array<Shape> newShapeList = new Array<Shape>(false, 32);
	Array<Shape> hintList = new Array<Shape>(false, 32);
	private Array<Shape> shuffleListTopLeft = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> shuffleListTopRight = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> shuffleListBottomLeft = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> shuffleListBottomRight = new Array<Shape>(false, 32, Shape.class);
	
	private int c1x, c1y, c2x, c2y;
	private boolean candidatesAreSet = false;
	private float matchesSinceLastMove = 0f;
	private float totalMatches = 0f;
	private int totalShapesCleared = 0;
	private int shapesClearedSinceLastMove = 0;
	private int score = 0;	
	private boolean shuffleCalled = false;
	private boolean hintRequested = false;
	private boolean endlessPlayMode = false;
	private String message = "Game Initialising";
// ---------------------------------------------Constructor(s)--------
		
	GameLogic(LevelMaster l) {
		this(10, 10, l);
	}
	GameLogic(int x, int y, LevelMaster l) {
		x_size = x;
		y_size = y;
		level = l;
		shapeTile = new Shape[x][y];
		Shape.logic = this;
		boardTile = new Sprite[x][y];
		   		
   		for( int i = 0; i < x_size; i++ ) {
	    	for( int j = 0; j < y_size; j++ ) {
			    boardTile[i][j] = new Sprite(Core.boardBaseTiles[i][y_size - j -1]);
			    boardTile[i][j].setBounds(i*4, j*4, 4, 4);
			}           
		}
	}
//-----------------------------------------------------Methods-------
	
	@Override
	public void run() {
		fillBoard();
		Core.delay(60);
		while ( boardHasMatches(100) ) {		// clear the board of any pre-existing matches.
			replaceMatchedShapes();
		}
		findHintsOnBoard();
		            			       // From here, this is is the main loop of the game logic.
		do { 
			if ( hintRequested ) {
				hintList.first().select();
			}
			if ( candidatesAreSet ) {
				hintList.first().deselect();
				hintRequested = false;
				doSwapIfSwapable(c1x, c1y, c2x, c2y);
			}
			if ( shuffleCalled ) {
				shuffleBoard();
			}
			if ( hintList.size <= 0 ) {
				findHintsOnBoard();
				if ( hintList.size <= 0 && endlessPlayMode ) {
					message = "Shuffling Board";
					Core.delay(200);
					shuffleBoard();
				}
			}
			Core.delay(100);
		} while ( !level.IsFinished() );		
		clear();     								
	}							                  // The end of the main logic loop.
// -------------------------------------------------------------------------------------
	
	void findHintsOnBoard() {   			// method adds potential moves to hintList.
		for ( int i = 0; i < x_size; i++ ) {
			for ( int j = 0; j < y_size; j++ ) {
			    	shapeTile[i][j].addHintsToHintList();	      
			}         
		}
	Gdx.graphics.requestRendering();
	}
	
	void fillBoard() {			// TODO (maybe) add a method to drop Shapes from the top.
		Gdx.graphics.requestRendering();
		for ( int i = 0; i < x_size; i++ ) {
	    	for ( int j = 0; j < y_size; j++ ) {
	    		shapeTile[i][j] = level.makeNewShape(i, j);
	    	}         
	    } 
		for ( int a = 1; a <= 9; a++ ) {
			for ( int i = 0; i < x_size; i++ ) {
				for ( int j = 0; j < y_size; j++ ) {
						shapeTile[i][j].setScale(a*0.1f);
						shapeTile[i][j].setRotation(180-(20*a));
				}
			}
			Gdx.graphics.requestRendering();	
			Core.delay(50);
		}
	}
	
	boolean boardHasMatches(long time) {
		Core.delay(time);
		boolean matchesFound = false;
		for ( int i = 0; i < x_size; i++ ) {
	    	for ( int j = 0; j < y_size; j++ ) {
	    		float shapeMatch = shapeTile[i][j].checkMatch(i, j);	    		
	    		if ( shapeMatch > 0f ) {
	    				matchesSinceLastMove += shapeMatch;			// adds to a sub-total for score recording.	
	    				matchesFound = true;
	    				matchList.add(shapeTile[i][j]);
	    		}
	    	}         
	    }
		return matchesFound;
	}
	
	void replaceMatchedShapes() {
		for ( int a = 1; a <= 9; a++ ) {      		// animates the shrinking of the Shape sprites.
			for ( Shape each : matchList ) {
				each.setScale( 1f - a * 0.1f );
				each.setRotation( -20 * a );
				each.setAlpha( 0.1f + a * 0.1f );
			}
			Gdx.graphics.requestRendering();
			Core.delay(40);
		}								
		for ( Shape each : matchList ) {			// replaces matched shapes with newly generated ones.
			int x = (int) each.getX();
			int y = (int) each.getY();
			shapeTile[x][y] = level.makeNewShape(x, y);
			newShapeList.add(shapeTile[x][y]);
		}			
		for ( int a = 1; a <= 9; a++ ) {			// animates the insertion of new shapes.
			for ( Shape each : newShapeList ) {
				each.setScale( a * 0.1f );
				each.setRotation( 180 - 20 * a );
				each.setAlpha( a * 0.1f );
			}
			Gdx.graphics.requestRendering();	
			Core.delay(40);
		}
		matchList.clear();
		newShapeList.clear();								
		hintList.clear();
		message = " ";
	}	
	
	void doSwapIfSwapable(int x1, int y1, int x2, int y2) {
		boolean matchesWereFound = false;
		shapesClearedSinceLastMove = 0;
		matchesSinceLastMove = 0f;
		
		selectAndSwap(x1, y1, x2, y2);              			// Swap the shapes... 
		while ( boardHasMatches(60) ) {							// run the board...
			shapesClearedSinceLastMove += matchList.size;
			matchesWereFound = true; 	
			replaceMatchedShapes();
		}
		if ( matchesWereFound ) {		  			
			totalMatches += matchesSinceLastMove;				// add to sub-total to score.
			if ( matchesSinceLastMove > 3 ) {
				message = "Wow! " + matchesSinceLastMove + " Match Combo!"; 
			}
			totalShapesCleared += shapesClearedSinceLastMove;
			score += Math.pow(matchesSinceLastMove, 3) * totalMatches * shapesClearedSinceLastMove;
			Gdx.graphics.requestRendering();
		} else {
			Core.delay(80);
			selectAndSwap(x1, y1, x2, y2);						// or swap shapes back no match found
		}
		candidatesAreSet = false;               	// re-open input variables from graphics/input thread.
	}
	
	void selectAndSwap(int x1, int y1, int x2, int y2) {
		shapeTile[x1][y1].select();
		shapeTile[x2][y2].select();
		Gdx.graphics.requestRendering();
		
		for ( int a = 1; a <= 8; a++ ) {			// animate shapes into their new positions.
			moveShape(x1, y1, x2, y2, a);
			moveShape(x2, y2, x1, y1, a);
			Gdx.graphics.requestRendering();
			Core.delay(20);
		}
		shapeTile[x1][y1].deselect();
		shapeTile[x2][y2].deselect();
		Gdx.graphics.requestRendering();
		
		Shape tmpShape = shapeTile[x1][y1];			// update game board with new positions.
		shapeTile[x1][y1] = shapeTile[x2][y2];
		shapeTile[x2][y2] = tmpShape;
	}
	private void moveShape(int oldX, int oldY, int newX, int newY, float anim8) {
		shapeTile[oldX][oldY].setPosition( oldX + (newX-oldX)*anim8/8f, oldY + (newY-oldY)*anim8/8f );
	}
	private void moveShape(Shape s, int newX, int newY, float anim8) {
		int oldX = s.getSavedX();
		int oldY = s.getSavedY();
		s.setPosition( oldX + (newX-oldX)*anim8/8, oldY + (newY-oldY)*anim8/8 );
	}
	void shuffleBoard() {
		int halfX = (x_size+1)/2;        // produce halves that round up, instead of down.
		int halfY = (y_size+1)/2;
		for ( int i = 0; i < x_size/2; i++ ) {					// divide shapes between four Arrays, depending on their quadrant.
			for ( int j = 0; j < y_size/2; j++ ) {
				shuffleListBottomLeft.add( shapeTile[i][j] );
				shuffleListBottomRight.add( shapeTile[i + halfX][j] );
				shuffleListTopLeft.add( shapeTile[i][j + halfY] );
				shuffleListTopRight.add( shapeTile[i + halfX][j + halfY] );
			}
		}
		if ( halfX != x_size/2 ) {                              // a couple of short loops to cover odd sized boards
			for ( int j = 0; j < y_size/2; j++ ) {
				shuffleListBottomRight.add( shapeTile[x_size/2][j] );
				shuffleListTopLeft.add( shapeTile[x_size/2][j + halfY] );
			}
		}
		if ( halfY != y_size/2 ) {
			for ( int i = 0; i < x_size/2; i++ ) {
				shuffleListBottomLeft.add( shapeTile[i][y_size/2] );
				shuffleListTopRight.add( shapeTile[i + halfX][y_size/2] );
			}
		}
		for ( int i = 0; i < x_size; i++ ) {
			for (int j = 0; j < y_size; j++ ) {
				shapeTile[i][j].saveXY();
			}
		}
		do {
			shuffleListBottomLeft.shuffle();
			shuffleListBottomRight.shuffle();
			shuffleListTopLeft.shuffle();
			shuffleListTopRight.shuffle();
			int arrayIndex = 0;
			for ( int i = 0; i < x_size/2; i++ ) { 					// put shapes into new slots in shapeTile[][]
				for ( int j = 0; j < y_size/2; j++ ) {				// sprite coords of old locations are retained.
					shapeTile[i][j] = shuffleListTopRight.items[arrayIndex];
					shapeTile[i + halfX][j] = shuffleListTopLeft.items[arrayIndex];
					shapeTile[i][j + halfY] = shuffleListBottomRight.items[arrayIndex];
					shapeTile[i + halfX][j + halfY] = shuffleListBottomLeft.items[arrayIndex];
					arrayIndex++;
				}
			}
			int savedArrayIndex = arrayIndex;
			if ( halfX != x_size/2 ) {                              // a couple more short loops to cover odd sized boards
				for ( int j = 0; j < y_size/2; j++ ) {
					shapeTile[x_size/2][j] = shuffleListTopLeft.items[arrayIndex];
					shapeTile[x_size/2][j + halfY] = shuffleListBottomRight.items[arrayIndex];
					arrayIndex++;
				}
			}
			arrayIndex = savedArrayIndex;
			if ( halfY != y_size/2 ) {
				for ( int i = 0; i < x_size/2; i++ ) {
					shapeTile[i][y_size/2] = shuffleListTopRight.items[arrayIndex];
					shapeTile[i + halfX][y_size/2] = shuffleListBottomLeft.items[arrayIndex];
					arrayIndex++;
				}
			}
		} while ( boardHasMatches(0) );
		matchList.clear();
		
		for ( int a = 1; a <= 8; a++ ) {							// animate moves
			for ( int i = 0; i < x_size; i++ ) {
				for (int j = 0; j < y_size; j++ ) {
					moveShape(shapeTile[i][j], i, j, a);
				}
			}
			Gdx.graphics.requestRendering();
			Core.delay(80);
		}
		shuffleListBottomLeft.clear();								// do some clean up.
		shuffleListBottomRight.clear();
		shuffleListTopLeft.clear();
		shuffleListTopRight.clear();

		hintList.clear();		
		shuffleCalled  = false;
		message = "Searching...";
	}
	private void clear() {			
		Gdx.graphics.requestRendering();
		Core.delay(500);
		return;
	}
// ---------------------------------------------------------------------Getters and Setters
	public Sprite getBoardTile(int x, int y) {
		return boardTile[x][y];
	}
	public void setBoardTile(Sprite s, int x, int y) {
		boardTile[x][y] = s;
	}
	public Shape getShape(int x, int y) {
		return shapeTile[x][y];
	}
	public Shape getShape(Vector2 xy) {
		return shapeTile[ (int)xy.x ][ (int)xy.y ];
	}
	public int getXsize() {
		return x_size;
	}
	public int getYsize() {
		return y_size;
	}
	public void setSwapCandidates(int x1, int y1, int x2, int y2) {
		c1x = x1;
		c1y = y1;
		c2x = x2;
		c2y = y2;
		candidatesAreSet = true;
	}
	public boolean isReadyForNewCandidates() {
		return !candidatesAreSet;
	}
	public int getShapesCleared() {
		return totalShapesCleared;
	}
	public int getTotalMatches() {
		return (int) totalMatches;
	}	
	public int getScore() {
		return score;
	}
	public int getNumberOfHints() {
		return hintList.size;
	}
	public void setShuffle() {
		shuffleCalled = true;
	}
	public boolean hintHasBeenGiven() {
		return hintRequested;
	}
	public void requestHint() {
		hintRequested = true;
	}
	public void setEndlessPlayMode() {
		endlessPlayMode = true;
	}
	public void addHint(Shape s) {
		hintList.add(s);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String s) {
		message = s;
	}
//----------------------------------------------End-of-Class--------
}	

