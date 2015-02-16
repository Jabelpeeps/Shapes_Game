package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class PlayArea {

// ---------------------------------------------Fields------------
	final LevelMaster level;
	private static Sprite[][] boardTile;
	private static Shape[][] shapeTile;
	private static int x_size, y_size, x_offset, y_offset;
	private Array<Shape> matchList = new Array<Shape>(false, 32);
	private Array<Shape> newShapeList = new Array<Shape>(false, 32);
	private Array<Shape> hintList = new Array<Shape>(false, 32);
	private Array<Shape> allShapes = new Array<Shape>(false, 128, Shape.class);
	private Array<Sprite> allBaseTiles = new Array<Sprite>(false, 128, Sprite.class);
	
	private float matchesSinceLastMove = 0f;
	private float totalMatches = 0f;
	private int totalShapesCleared = 0;
	private int shapesClearedSinceLastMove = 0;
	private int score = 0;
	private boolean boardIsSetForPlay = false;
	private String message = "Game Initialising";
// ---------------------------------------------Constructor(s)--------
		
	PlayArea(LevelMaster l) {
		this(10, 10, l);
	}
	PlayArea(int x, int y, LevelMaster l) {
		x_size = x;
		y_size = y;
		level = l;
		shapeTile = new Shape[x][y];
		Shape.game = this;
		boardTile = new Sprite[x][y];
		x_offset = (10-x_size)*2;
		y_offset = (10-y_size)*2;
	}
//-----------------------------------------------------Methods-------
	
	void setupBoard() {
		Sprite tmpSprite;
		for( int i = 0; i < x_size; i++ ) {
	    	for( int j = 0; j < y_size; j++ ) {
			    tmpSprite = new Sprite(Core.boardBaseTiles[i][(y_size-j-1)%10]);
			    tmpSprite.setBounds(i*4 + x_offset, j*4 + y_offset, 4, 4);
			    tmpSprite.setColor(level.baseColor);
			    allBaseTiles.add(tmpSprite);
			    boardTile[i][j] = tmpSprite;
			}           
		}
	}
	void findHintsOnBoard() {   			// adds potential moves to hintList.
		hintList.clear();
		for ( Shape each : allShapes ) {
			each.addHintsToHintList();
		}
	Gdx.graphics.requestRendering();
	}
	
	void spinShapesIntoPlace() {
		for ( int a = 1; a <= 9; a++ ) {
			for ( Shape each : allShapes ) {
						each.setScale(a*0.1f);
						each.setRotation(180-(20*a));
			}
			Gdx.graphics.requestRendering();	
			Core.delay(50);
		}
	}
	void sprayShapesIntoPlace() {
		Array<Shape> bottomLeft = new Array<Shape>(false, 32, Shape.class);
		Array<Shape> bottomRight = new Array<Shape>(false, 32, Shape.class);
		Array<Shape> topLeft = new Array<Shape>(false, 32, Shape.class);
		Array<Shape> topRight = new Array<Shape>(false, 32, Shape.class);
		
		for ( Shape each : allShapes ) {
			each.setAlpha(0f);
			each.saveXY();
			if ( each.getY() < y_size/2 ) {
					if ( each.getX() < x_size/2 ) {
						bottomLeft.add(each);
						each.setNewXY(x_size+2, y_size+2);
						//each.setNewXY(-5, -5);
					} else {
						bottomRight.add(each);
						each.setNewXY(-2, y_size+2);
						//each.setNewXY(x_size+5, -5);
					}
			} else {
					if ( each.getX() < x_size/2 ) {
						topLeft.add(each);
						each.setNewXY(x_size+2, -2);
						//each.setNewXY(-5, y_size+5);
					} else {
						topRight.add(each);
						each.setNewXY(-2, -2);
						//each.setNewXY(x_size+5, y_size+5);
					}
			}
		}
		bottomLeft.shuffle();
		bottomRight.shuffle();
		topLeft.shuffle();
		topRight.shuffle();
		ArrayMap<Shape, Integer> shapesInMotion = new ArrayMap<Shape, Integer>(false, x_size*4);
		Array<Shape> shapesInPlace = new Array<Shape>();
		Array<Shape> next4Shapes = new Array<Shape>();
		int a = 0;
		for ( a = 0; a < allShapes.size/4 + 8 ; a++ ) {
			if ( a*4 < allShapes.size ) {
				next4Shapes.addAll(bottomLeft.items[a], bottomRight.items[a], topLeft.items[a], topRight.items[a]);
				for ( Shape each : next4Shapes ) {
					each.setAlpha(1f);
					shapesInMotion.put(each, a);
				}
				next4Shapes.clear();
			}
			for ( Entry<Shape, Integer> each : shapesInMotion ) {
				each.key.setRotation(180-(20*(1 + (a - each.value)%8)));
				moveShape(each.key.getNewX(), each.key.getNewY(), each.key, (1 + (a - each.value)%8) );
				if ( each.key.getSavedY() == each.key.getY() ) {
						shapesInPlace.add(each.key);	
				}	
			}
			Gdx.graphics.requestRendering();
			for ( Shape each : shapesInPlace ) {
				each.setRotation(0);
				shapesInMotion.removeKey(each);
			}
			Core.delay(50);
		}
	}
	void dropShapesIntoPlace() {
		for ( Shape each : allShapes ) {
			each.setAlpha(0f);
			each.saveXY();
		}
		ArrayMap<Shape, Integer> tmpList = new ArrayMap<Shape, Integer>(false, x_size);
		Shape tmpShape = null;
		int a = 0;
		for ( a = 0; a < allShapes.size + 8 ; a++ ) {
			if ( a < allShapes.size ) {
				allShapes.items[a].setAlpha(1f);
				tmpList.put(allShapes.items[a], a);
			}
			for ( Entry<Shape, Integer> each : tmpList ) {
				moveShape(each.value % 10, y_size, each.key, ((a - each.value) % 8) + 1 );
				if ( each.key.getSavedY() == each.key.getY() ) {
						tmpShape = each.key;	
				}	
			}
			if ( tmpShape != null )	tmpList.removeKey(tmpShape);
			Gdx.graphics.requestRendering();
			Core.delay(18);
		}
	}
	void fillBoard() {
		for ( int j = 0; j < y_size; j++ ) {
			for ( int i = 0; i < x_size; i++ ) {
	    		shapeTile[i][j] = level.makeNewShape(i, j);
	    		allShapes.add(shapeTile[i][j]);
	    	}         
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
	void blinkList(long time, int repeats, Array<Shape> list) {
		for ( int i = 1; i<= repeats; i++ ) {
			for ( Shape each : matchList ) each.select();
			Gdx.graphics.requestRendering();
			Core.delay(time);
			for ( Shape each : matchList ) each.deselect();
			Gdx.graphics.requestRendering();
			Core.delay(time);
		}
	}
	void replaceMatchedShapes() {
		blinkList(100, 2, matchList);
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
			allShapes.removeValue(each, false);
			shapeTile[x][y] = level.makeNewShape(x, y);
			newShapeList.add(shapeTile[x][y]);
		}
		matchList.clear();
		allShapes.addAll(newShapeList);			
		for ( int a = 1; a <= 9; a++ ) {			// animates the insertion of new shapes.
			for ( Shape each : newShapeList ) {
				each.setScale( a * 0.1f );
				each.setRotation( 180 - 20 * a );
				each.setAlpha( a * 0.1f );
			}
			Gdx.graphics.requestRendering();	
			Core.delay(40);
		}
		newShapeList.clear();
		message = " ";
	}	
	
	void doSwapIfSwapable(int x1, int y1, int x2, int y2) {
		boolean matchesWereFound = false;
		shapesClearedSinceLastMove = 0;
		matchesSinceLastMove = 0f;

		shapeTile[x1][y1].deselect();
		shapeTile[x2][y2].deselect();
		Core.delay(20);
		Gdx.graphics.requestRendering();
		
		animateSwap(x1, y1, x2, y2);              				// Swap the shapes... 
		
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
			findHintsOnBoard();
		} else {
			Core.delay(80);
			animateSwap(x1, y1, x2, y2);						// or swap shapes back no match found
		}
	}
	void animateSwap(int x1, int y1, int x2, int y2) {
		
		for ( int a = 1; a <= 8; a++ ) {	// animate shapes into their new positions.
			moveShape(x1, y1, x2, y2, a);
			moveShape(x2, y2, x1, y1, a);
			shapeTile[x1][y1].setRotation( 180 - 20 * a );
			shapeTile[x2][y2].setRotation( 180 - 20 * a );
			Gdx.graphics.requestRendering();
			Core.delay(25);
		}
		shapeTile[x1][y1].setRotation(0);
		shapeTile[x2][y2].setRotation(0);
		Gdx.graphics.requestRendering();		
		shapeTileArraySwap(x1, y1, x2, y2); // update game board with new positions.
	}
	void shapeTileArraySwap(int x1, int y1, int x2, int y2) {
		Shape tmpShape = shapeTile[x1][y1];			
		shapeTile[x1][y1] = shapeTile[x2][y2];
		shapeTile[x2][y2] = tmpShape;
	}
	private void moveShape(int oldX, int oldY, int newX, int newY, float anim8) {
		shapeTile[oldX][oldY].setPosition( oldX + (newX-oldX)*anim8/8f, oldY + (newY-oldY)*anim8/8f );
	}
	
	private void moveShape(Shape s, int newX, int newY, float anim8) {
		float oldX = s.getSavedX();
		float oldY = s.getSavedY();
		s.setPosition( oldX + (newX-oldX)*anim8/8, oldY + (newY-oldY)*anim8/8 );
	}
	private void moveShape(float oldX, float oldY, Shape s, float anim8) {
		float newX = s.getSavedX();
		float newY = s.getSavedY();
		s.setPosition( oldX + (newX-oldX)*anim8/8, oldY + (newY-oldY)*anim8/8 );
	}
	
	void shuffleBoard() {
		message = "Shuffling Board";
		Array<Shape> shuffleListTopLeft = new Array<Shape>(false, 32, Shape.class);
		Array<Shape> shuffleListTopRight = new Array<Shape>(false, 32, Shape.class);
		Array<Shape> shuffleListBottomLeft = new Array<Shape>(false, 32, Shape.class);
		Array<Shape> shuffleListBottomRight = new Array<Shape>(false, 32, Shape.class);
		Gdx.graphics.requestRendering();
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
		for ( int i = 0; i < x_size; i++ ) {			// saving x & y now makes the animation easier to run smoothly.
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
			if ( halfX != x_size/2 ) {                             	// a couple more short loops for odd sized boards
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
		} while ( boardHasMatches(0) );    // repeat shuffle without animation until position found without any matches.
		matchList.clear();
		
		for ( int a = 1; a <= 8; a++ ) {							// animate shapes into new positions.
			for ( int i = 0; i < x_size; i++ ) {
				for (int j = 0; j < y_size; j++ ) {
					moveShape(shapeTile[i][j], i, j, a);
				}
			}
			Gdx.graphics.requestRendering();
			Core.delay(80);
		}
		message = "Searching...";
		findHintsOnBoard();	
				
		shuffleListBottomLeft.clear();							// do some cleaning.
		shuffleListBottomRight.clear();
		shuffleListTopLeft.clear();
		shuffleListTopRight.clear();
	}
// ---------------------------------------------------------------------Getters and Setters
	public Sprite getBoardTile(int x, int y) {   		// currently unused
		return boardTile[x][y];
	}
	public void setBoardTile(Sprite s, int x, int y) {  // currently unused
		boardTile[x][y] = s;
	}
	public Shape getShape(int x, int y) {    			// used by input processing 
		return shapeTile[x][y];
	}
	public Shape getShape(Vector2 xy) {      			// used by match checking in Shape
		return shapeTile[ (int)xy.x ][ (int)xy.y ];
	}
	public int getXsize() {								// used CrossOne 
		return x_size;									// match checking (in CrossOneAbstract)
	}													//
	public int getYsize() {								// 
		return y_size;									//
	}
	public int getXoffset() {
		return x_offset;
	}
	public int getYoffset() {
		return y_offset;
	}
	public int getShapesCleared() {						// used to report various stats for 
		return totalShapesCleared;						// display purposes (atm).
	}													//
	public int getTotalMatches() {						// 
		return (int) totalMatches;						//		
	}													//	
	public int getScore() {								// 
		return score;									//
	}
	public int getHintListSize() {						// This group of calls are used by 
		return hintList.size;							// DemoGameLogic to extract the
	}													// information it needs to choose its
	public int getMatchListSize() {						// next moves, and also reset the values
		return matchList.size;							// in private fields of this class.
	}													//
	public Array<Shape> getHintList() {					//
		return hintList;								//
	}													//
	public void clearMatchList() {						//
		matchList.clear();								//
	}
	public void addHint(Shape s) {						// used by addHintToList() in Shape
		hintList.add(s);
	}
	public String getMessage() {						// used to get status message string.
		return message;
	}
	public void setMessage(String s) {                	// made for DemoGameLogic, but seem to be
		message = s;									// currently unused.
	}
	public boolean boardIsAlreadySet() {
		return boardIsSetForPlay;
	}
	public void setBoardReadyForPlay() {
		boardIsSetForPlay = true;
	}
	public Shape[] getAllShapes(){
		return allShapes.toArray();
	}
	public Sprite[] getAllBoardTiles() {
		return allBaseTiles.toArray();
	}
	
//----------------------------------------------End-of-Class--------
}	
