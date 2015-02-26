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
	private Sprite[][] boardTile;
	private Shape[][] shapeTile;
	private int x_size, y_size, x_offset, y_offset;
	
	private Array<Shape> matchList = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> newShapeList = new Array<Shape>(false, 32);
	private Array<Shape> hintList = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> allShapes = new Array<Shape>(false, 128, Shape.class);
	private Array<Sprite> allBaseTiles = new Array<Sprite>(false, 128, Sprite.class);
	
	private Array<Shape> bottomLeft = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> bottomRight = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> topLeft = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> topRight = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> oddShapes = new Array<Shape>(false, 16, Shape.class);
	
	private float matchesSinceLastMove = 0f;
	private float totalMatches = 0f;
	private int totalShapesCleared = 0;
	private int shapesClearedSinceLastMove = 0;
	private int score = 0;
	private boolean readyForPlay = false;
	private String message = "Game Initialising";
// ---------------------------------------------Constructor(s)--------
		
	public PlayArea(LevelMaster l) {
		this( 10 , 10 , 0 , 0 , l );
	}
	public PlayArea(int x, int y, LevelMaster l) {
		this( x , y , (10 - x) * 2 , (10 - y) * 2 , l );
	}
	public PlayArea(int x, int y, int x_off, int y_off, LevelMaster l) {
		level = l;
		x_size = x;
		y_size = y;
		x_offset = x_off;
		y_offset = y_off;
		shapeTile = new Shape[x][y];
		boardTile = new Sprite[x][y];
	}
//-----------------------------------------------------Methods-------

	void setupBoard() {
		Sprite tmpSprite;
		for( int i = 0; i < x_size; i++ ) {
	    	for( int j = 0; j < y_size; j++ ) {
			    tmpSprite = new Sprite( Core.boardBaseTiles[ i ][ ( y_size - j -1 ) % 10 ] );
			    tmpSprite.setBounds( i * 4 + x_offset , j * 4 + y_offset , 4 , 4 );
			    tmpSprite.setColor(level.baseColor);
			    boardTile[i][j] = tmpSprite;
			}
	    	allBaseTiles.addAll( boardTile[i] , 0 , y_size );
		}
	}
	
	void fillBoard() {
		for ( int i = 0; i < x_size; i++ ) {
			for ( int j = 0; j < y_size; j++ ) {
	    		shapeTile[i][j] = level.makeNewShape(i, j, x_offset, y_offset, this);
	    	} 
			allShapes.addAll(shapeTile[i], 0, y_size);
	    }
		shuffleShapeTileArray();        			// includes checks to exclude pre-existing matches 
													// and positions with no available moves.
		for ( int i = 0; i < x_size; i++ ) {
			for (int j = 0; j < y_size; j++ ) {
				shapeTile[i][j].setPosition(i, j);
			}
		}
	}
	
	int findHintsOnBoard() {   			// adds potential moves to hintList.
		hintList.clear();
		for ( Shape each : allShapes ) {
			each.addHintsToList();
		}
		hintList.shuffle();
		Gdx.graphics.requestRendering();
		return hintList.size;
	}
	
	void spinShapesIntoPlace() {
		for ( int a = 1; a <= 9; a++ ) {
			for ( Shape each : allShapes ) {
						each.setScale( a * 0.1f );
						each.setRotation( 180 - (20 * a) );
						each.setAlpha( 0.1f + a * 0.1f );
			}
			Gdx.graphics.requestRendering();	
			Core.delay(50);
		}
	}
	
	void swirlShapesIntoPlace() {
		swirlShapesIntoPlace(allShapes, true);
	}
	void swirlShapesIntoPlace(Array<Shape> list) {
		swirlShapesIntoPlace(list, false);
	}
	void swirlShapesIntoPlace(Array<Shape> list, boolean placingAllShapes) {
		list.shuffle();
		long time;
		
		if ( placingAllShapes ) {
				int halfX = (x_size+1)/2;        // produce halves that round up, instead of down.
				int halfY = (y_size+1)/2;
				float eachX, eachY;
				time = 40;
				
				for ( Shape each : list ) {
					each.setScale(1f);
					each.setNewXY();
					eachX = each.getX();
					eachY = each.getY();
					if ( eachY < y_size/2 ) {
							if ( eachX < x_size/2 ) {
								bottomLeft.add(each);								
							} else if ( eachX >= halfX ) {
								bottomRight.add(each);								
							} else {
								oddShapes.add(each);
							}
					} else if ( eachY >= halfY ){
							if ( eachX < x_size/2 ) {
								topLeft.add(each);							
							} else if ( eachX >= halfX ) {
								topRight.add(each);							
							} else {
								oddShapes.add(each);
							}
					} else {
						oddShapes.add(each);
					}
				}
		} else {
				time = 50;
				oddShapes.addAll(list);
				for ( Shape each : list ) {
					each.setScale(1f);
					each.setNewXY();
				}
		}
		int turnCorner = 1;
		while ( oddShapes.size > 0 ) {
			switch ( turnCorner++ ) {
			case 1:
				bottomLeft.add(oddShapes.pop());
				break;
			case 2:
				bottomRight.add(oddShapes.pop());
				break;
			case 3:
				topLeft.add(oddShapes.pop());
				break;
			case 4:
				topRight.add(oddShapes.pop());
				turnCorner = 1;
				break;
			}
		}
		for ( Shape each : bottomLeft ) {
			each.saveOrigin( x_size , y_size );
			each.setPosition( x_size + 2 , y_size + 2 );
		}
		for ( Shape each : bottomRight ) {
			each.saveOrigin( 0 , y_size );
			each.setPosition( -2 , y_size + 2 );
		}
		for ( Shape each : topLeft ) {
			each.saveOrigin( x_size , 0 );	
			each.setPosition( x_size + 2 , -2 );
		}
		for ( Shape each : topRight ) {
			each.saveOrigin( 0 , 0 );
			each.setPosition( -2 , -2 );		
		}
		ArrayMap<Shape, Integer> shapesInMotion = new ArrayMap<Shape, Integer>(false, 48);
		Array<Shape> shapesInPlace = new Array<Shape>(8);
		Array<Shape> next4Shapes = new Array<Shape>(8);
		
		int listSize = bottomLeft.size;
		for ( int a = 0; a <= listSize + 10; a++ ) {
			if ( bottomLeft.size > 0 ) next4Shapes.add(bottomLeft.pop());
			if ( bottomRight.size > 0 ) next4Shapes.add(bottomRight.pop());
			if ( topLeft.size > 0 ) next4Shapes.add(topLeft.pop());
			if ( topRight.size > 0 ) next4Shapes.add(topRight.pop());
			
			for ( Shape each : next4Shapes ) {
				each.setOrigin(each.getSavedOriginX(), each.getSavedOriginY());
				each.saveXY();
				each.rotate(260);
				shapesInMotion.put(each, a);
			}
			next4Shapes.clear();
			
			for ( Entry<Shape, Integer> each : shapesInMotion ) {
				each.key.setOrigin(each.key.getSavedOriginX(), each.key.getSavedOriginY());
				each.key.rotate(10);
				each.key.setAlpha( (float) (a - each.value ) / 10 );
				if ( a - each.value < 8 ) moveShape( each.key , each.key.getNewX() , each.key.getNewY() , 1 + (a - each.value) );
				if ( a - each.value == 10 ) shapesInPlace.add(each.key);	
			}
			Gdx.graphics.requestRendering();
			
			for ( Shape each : shapesInPlace ) {
				each.setRotation(0);
				each.setOriginCenter();
				each.setScale(0.9f);
				shapesInMotion.removeKey(each);
			}
			shapesInPlace.clear();
			Core.delay(time);
		}
		shapesInMotion = null;
		shapesInPlace = null;
		next4Shapes = null;
	}
	
	void dropShapesIntoPlace() {
		for ( Shape each : allShapes ) {
			each.setNewXY();
		}
		ArrayMap<Shape, Integer> tmpList = new ArrayMap<Shape, Integer>(false, x_size);
		Shape tmpShape = null;
		for ( int a = 0; a < allShapes.size + 8 ; a++ ) {
			if ( a < allShapes.size ) {
				allShapes.items[a].setAlpha(1f);
				tmpList.put(allShapes.items[a], a);
			}
			for ( Entry<Shape, Integer> each : tmpList ) {
				moveShape( each.value % 10 , y_size , each.key , ( (a - each.value) % 8) + 1 );
				if ( each.key.getNewY() == each.key.getY() ) {
						tmpShape = each.key;	
				}	
			}
			if ( tmpShape != null )	tmpList.removeKey(tmpShape);
			Gdx.graphics.requestRendering();
			Core.delay(18);
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
		blinkList(time, repeats, list.toArray());
	}
	void blinkList(long time, int repeats, Shape[] list) {
		for ( int i = 1; i<= repeats; i++ ) {
			for ( Shape each : list ) each.select();
			Gdx.graphics.requestRendering();
			Core.delay(time);
			for ( Shape each : list ) each.deselect();
			Gdx.graphics.requestRendering();
			Core.delay(time);
		}
	}
//	void replaceMatchedShapes() {
//		blinkList(100, 3, matchList);
//		for ( int a = 1; a <= 9; a++ ) {      		// animates the shrinking of the Shape sprites.
//			for ( Shape each : matchList ) {
//				each.setScale( 1f - a * 0.1f );
//				each.setRotation( -20 * a );
//				each.setAlpha( 0.1f + a * 0.1f );
//			}
//			Gdx.graphics.requestRendering();
//			Core.delay(40);
//		}								
//		for ( Shape each : matchList ) {			// replaces matched shapes with newly generated ones.
//			int x = (int) each.getX();
//			int y = (int) each.getY();
//			allShapes.removeValue(each, false);
//			shapeTile[x][y] = level.makeNewShape(x, y);
//			newShapeList.add(shapeTile[x][y]);
//		}
//		matchList.clear();
//		allShapes.addAll(newShapeList);			
//		for ( int a = 1; a <= 9; a++ ) {			// animates the insertion of new shapes.
//			for ( Shape each : newShapeList ) {
//				each.setScale( a * 0.1f );
//				each.setRotation( 180 - 20 * a );
//				each.setAlpha( a * 0.1f );
//			}
//			Gdx.graphics.requestRendering();	
//			Core.delay(40);
//		}
//		newShapeList.clear();
//		message = " ";
//	}
//	
	void replaceMatchedShapes() {
		blinkList(100, 2, matchList);
		
		Array<Shape> copyMatchList = new Array<Shape>(matchList);
		
		while ( matchList.size > 0 ) {
			Array<Shape> tmpList = new Array<Shape>(16);
			
			Shape tmpShape = matchList.peek();
			for ( Shape each : matchList ) {
				if ( each.type == tmpShape.type ) {
					tmpList.add(each); 
				}
			}
			matchList.removeAll(tmpList, false);
			
			float totX = 0, totY = 0;
			for ( Shape each : tmpList ) {
				totX += each.getX();
				totY += each.getY();
			}
			float avX = totX/tmpList.size;
			float avY = totY/tmpList.size;
			for ( Shape each : tmpList ) {
				each.setOrigin( avX , avY );
			}
			tmpList.clear();
		}
		
		for ( int a = 1; a <= 20; a++ ) {      		// animates the removing of the Shape sprites.
			for ( Shape each : copyMatchList ) {
				each.setScale( 0.8f + a * 0.1f );
				each.rotate(10);
				if ( a > 10 ) each.setAlpha( 1f - (a - 10) * 0.1f );
			}
			Gdx.graphics.requestRendering();
			Core.delay(20);
		}
		
		for ( Shape each : copyMatchList ) {			// replaces matched shapes with newly generated ones.
			int x = (int) each.getX();
			int y = (int) each.getY();
			allShapes.removeValue(each, false);
			shapeTile[x][y] = level.makeNewShape(x, y, x_offset, y_offset, this);
			newShapeList.add(shapeTile[x][y]);
		}
		
		long time = (long) (( Core.rand.nextGaussian() + 1.5) * 150);
		Core.delay(time);		
		allShapes.addAll(newShapeList);	
		swirlShapesIntoPlace(newShapeList);			
		newShapeList.clear();
		message = "";
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
		
		for ( int a = 1; a <= 8; a++ ) {					// animate shapes into their new positions.
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
		shapeTileArraySwap(x1, y1, x2, y2); 				// update game board with new positions.
	}
	void shapeTileArraySwap(int x1, int y1, int x2, int y2) {
		Shape tmpShape = shapeTile[x1][y1];			
		shapeTile[x1][y1] = shapeTile[x2][y2];
		shapeTile[x2][y2] = tmpShape;
	}
	
	private void moveShape(int oldX, int oldY, int newX, int newY, float anim8) {
		shapeTile[oldX][oldY].setPosition( oldX + (newX-oldX)*anim8/8f, oldY + (newY-oldY)*anim8/8f );
	}
	private void moveShape(Shape s, float newX, float newY, float anim8) {
		float oldX = s.getSavedX();
		float oldY = s.getSavedY();
		s.setPosition( oldX + (newX-oldX)*anim8/8, oldY + (newY-oldY)*anim8/8 );
	}
	private void moveShape(float oldX, float oldY, Shape s, float anim8) {
		float newX = s.getNewX();
		float newY = s.getNewY();
		s.setPosition( oldX + (newX-oldX)*anim8/8, oldY + (newY-oldY)*anim8/8 );
	}
	
	void shuffleBoard() {
		message = "No Moves Left, Shuffling";
		Gdx.graphics.requestRendering();
		
		shuffleShapeTileArray();

		for ( int a = 1; a <= 8; a++ ) {							// animate shapes into new positions.
			for ( int i = 0; i < x_size; i++ ) {
				for (int j = 0; j < y_size; j++ ) {
					moveShape(shapeTile[i][j], i, j, a);
				}
			}
			Gdx.graphics.requestRendering();
			Core.delay(80);
		}
		message = "";
	}	
	void shuffleShapeTileArray() {	
		int halfX = (x_size+1)/2;        						// produce halves that round up, instead of down.
		int halfY = (y_size+1)/2;
		for ( int i = 0; i < x_size/2; i++ ) {					// divide shapes between four Arrays, depending on their quadrant.
			for ( int j = 0; j < y_size/2; j++ ) {
				bottomLeft.add( shapeTile[i][j] );
				bottomRight.add( shapeTile[i + halfX][j] );
				topLeft.add( shapeTile[i][j + halfY] );
				topRight.add( shapeTile[i + halfX][j + halfY] );
			}
		}
		if ( halfX != x_size/2 ) {                              // a couple of short loops to cover odd sized boards
			for ( int j = 0; j < y_size/2; j++ ) {
				bottomRight.add( shapeTile[x_size/2][j] );
				topLeft.add( shapeTile[x_size/2][j + halfY] );
			}
		}
		if ( halfY != y_size/2 ) {
			for ( int i = 0; i < x_size/2; i++ ) {
				bottomLeft.add( shapeTile[i][y_size/2] );
				topRight.add( shapeTile[i + halfX][y_size/2] );
			}
		}
		for ( Shape each : allShapes ) {					// saving x & y now makes the animation easier to run smoothly.
			each.saveXY();
		}
		do {
			bottomLeft.shuffle();
			bottomRight.shuffle();
			topLeft.shuffle();
			topRight.shuffle();
			int arrayIndex = 0;
			for ( int i = 0; i < x_size/2; i++ ) { 					// put shapes into new slots in shapeTile[][]
				for ( int j = 0; j < y_size/2; j++ ) {				// sprite coords of old locations are retained.
					shapeTile[i][j] = topRight.items[arrayIndex];
					shapeTile[i + halfX][j] = topLeft.items[arrayIndex];
					shapeTile[i][j + halfY] = bottomRight.items[arrayIndex];
					shapeTile[i + halfX][j + halfY] = bottomLeft.items[arrayIndex];
					arrayIndex++;
				}
			}
			int savedArrayIndex = arrayIndex;
			if ( halfX != x_size/2 ) {                             	// a couple more short loops for odd sized boards
				for ( int j = 0; j < y_size/2; j++ ) {
					shapeTile[x_size/2][j] = topLeft.items[arrayIndex];
					shapeTile[x_size/2][j + halfY] = bottomRight.items[arrayIndex];
					arrayIndex++;
				}
			}
			arrayIndex = savedArrayIndex;
			if ( halfY != y_size/2 ) {
				for ( int i = 0; i < x_size/2; i++ ) {
					shapeTile[i][y_size/2] = topRight.items[arrayIndex];
					shapeTile[i + halfX][y_size/2] = bottomLeft.items[arrayIndex];
					arrayIndex++;
				}
			}
		} while ( boardHasMatches(0) || findHintsOnShapeTile() <= 0 );    // repeat shuffle until position found without any matches.
		
		matchList.clear();							// do some cleaning.
		bottomLeft.clear();
		bottomRight.clear();
		topLeft.clear();
		topRight.clear();
	}
	
	int findHintsOnShapeTile() {   			// adds potential moves to hintList.
		hintList.clear();
		for ( int i = 0; i < x_size; i++ ) {
			for (int j = 0; j < y_size; j++ ) {
				shapeTile[i][j].addHintsToList(i, j);
			}
		}
		hintList.shuffle();
		return hintList.size;
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
	public int getXsize() {								// used in CrossOne 
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
	public Shape[] getHintList() {						//
		return hintList.toArray();						//
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
	public boolean playAreaIsReady() {
		return readyForPlay;
	}
	public void setPlayAreaReady() {
		readyForPlay = true;
	}
	public Shape[] getAllShapes() {
		return allShapes.toArray();
	}
	public Sprite[] getAllBoardTiles() {
		return allBaseTiles.toArray();
	}
	public void dispose() {
		readyForPlay = false;
		allBaseTiles = null;
		allShapes = null;
		hintList = null;
		matchList = null;
		newShapeList = null;
		shapeTile = null;
		boardTile = null;
		bottomLeft = null;
		bottomRight = null;
		topLeft = null;
		topRight = null;
		oddShapes = null;
	}
//----------------------------------------------End-of-Class--------
}	
