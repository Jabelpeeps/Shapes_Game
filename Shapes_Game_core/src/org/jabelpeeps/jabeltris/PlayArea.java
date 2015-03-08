package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.shapes.Blank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class PlayArea implements Serializable {

// ---------------------------------------------Fields------------
	LevelMaster level;
	private Sprite[][] boardTile;
	private Shape[][] shapeTile;
	private int x_size = 10, y_size = 10, x_offset = 0, y_offset = 0;
	
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
	private Array<Array<Shape>> listoflists = new Array<Array<Shape>>(4);
	
	private float matchesSinceLastMove = 0f;
	private float totalMatches = 0f;
	private int totalShapesCleared = 0;
	private int shapesClearedSinceLastMove = 0;
	private int score = 0;
	private boolean readyForPlay = false;
	private String message = "Game Initialising";
// ---------------------------------------------Constructor(s)--------
	public PlayArea() {
	}
	public PlayArea(int x, int y) {
		this( x , y , (10 - x) * 2 , (10 - y) * 2 );
	}
	public PlayArea(int x, int y, int x_off, int y_off) {
		x_size = x;
		y_size = y;
		x_offset = x_off;
		y_offset = y_off;
	}
//-----------------------------------------------------Methods-------

	public void initialise(LevelMaster l) {
		level = l;
		shapeTile = new Shape[x_size][y_size];
		boardTile = new Sprite[x_size][y_size];
		listoflists.add(bottomLeft);
		listoflists.add(bottomRight);
		listoflists.add(topLeft);
		listoflists.add(topRight);
	}
	
	@Override
	public void write(Json json) {
		json.writeValue("PlayArea", getClass().getName());
		json.writeValue("x_size", x_size);
		json.writeValue("y_size", y_size);
		json.writeValue("x_offset", x_offset);
		json.writeValue("y_offset", y_offset);
		
		json.writeArrayStart("Shapes");
		for ( Shape each : allShapes ) json.writeValue(each);
		json.writeArrayEnd();
	}
	
	@Override
	public void read(Json json, JsonValue jsonData) {
		x_size = jsonData.getInt("x_size");
		y_size = jsonData.getInt("y_size");
		x_offset = jsonData.getInt("x_offset");
		y_offset = jsonData.getInt("y_offset");
		
		JsonValue list = jsonData.get("Shapes");
		try {
			for ( JsonValue each = list.child; each != null; each = each.next ) {
				
				Class<?> shapeclass = ClassReflection.forName( each.getString("Shape") );
				Shape tmpShape = (Shape) ClassReflection.newInstance( shapeclass );

				tmpShape.setPlayArea(this).setOffsets().setOriginAndBounds().read( json , each );
				allShapes.add(tmpShape);
			}
			
		} catch (ReflectionException e) {  e.printStackTrace();	}
	}
	
	public void setupShapeTile() {
		for ( Shape each : allShapes ) {
			shapeTile[ (int)each.getX() ][ (int)each.getY() ] = each;
		}
	}

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
		shuffleShapes();        			// includes checks to exclude pre-existing matches 
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
		spinShapesIntoPlace(allShapes);
	}
	void spinShapesIntoPlace(Array<Shape> list) {
		for ( int a = 1; a <= 9; a++ ) {
			for ( Shape each : list ) {
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
		for (Shape each : list ) {
			each.setNewXY();
			each.setScale(1f);
		}
		if ( placingAllShapes ) {
				time = 40;
				divideListByQuadrant(list);
		} else {
				time = 50;
				oddShapes.addAll(list);
		}
		allocateOddShapes();
		
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
			
			for (Array<Shape> eachList : listoflists ) {
				if ( eachList.size > 0 ) next4Shapes.add(eachList.pop());
			}			
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

	void replaceMatchedShapes() {
		blinkList(100, 2, matchList);
		
		Array<Shape> copyMatchList = new Array<Shape>(matchList);
		
		while ( matchList.size > 0 ) {			// divides matchList into subset of each shape type.
			Array<Shape> tmpList = new Array<Shape>(16);
			
			Shape tmpShape = matchList.peek();
			for ( Shape each : matchList ) {
				if ( each.type.equals(tmpShape.type) ) {
					tmpList.add(each); 
				}
			}
			matchList.removeAll(tmpList, false);
			
			float totX = 0, totY = 0;			// sets the origin for each type to the average position
			for ( Shape each : tmpList ) {		// of the group.
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
				each.setAnimation(false);
				each.setScale( 0.8f + a * 0.1f );
				each.rotate(10);
				if ( a > 10 ) each.setAlpha( 1f - (a - 10) * 0.1f );
			}
			Gdx.graphics.requestRendering();
			Core.delay(20);
		}
		
		for ( Shape each : copyMatchList ) {		// replaces matched shapes with newly generated ones.
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
	
	void shapeTileArraySwap(float x1, float y1, float x2, float y2) {
		shapeTileArraySwap((int)x1, (int)y1, (int)x2, (int)y2);
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
	
	void divideListByQuadrant(Array<Shape> list) {
		int halfX = (x_size+1)/2;        // produce halves that round up, instead of down.
		int halfY = (y_size+1)/2;
		float eachX, eachY;
		
		for ( Shape each : list ) {
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
	}
	
	void allocateOddShapes() {
		
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
	}
	
	void shuffleBoard() {
		message = "No Moves Left, Shuffling";
		Gdx.graphics.requestRendering();
		
		shuffleShapes();

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
	
	void shuffleShapes() {
		
		for ( Shape each : allShapes ) {				// saving x & y now makes the animation easier to run smoothly.
			each.saveXY();
		}
		divideListByQuadrant(allShapes);
		
		int idealListSize = (allShapes.size - allShapes.size % 4) / 4 ;
		
		for ( Array<Shape> eachList : listoflists ) {
			while ( eachList.size > idealListSize ) {
				oddShapes.add(eachList.pop());
			}
			while ( eachList.size < idealListSize ) {
				eachList.add(oddShapes.pop());
			}
		}
		
		allocateOddShapes();
		
		Array<Shape> tmpList;
		boolean firstloop = true;
		
		do {
			if ( !firstloop ) {
				for ( Shape each : allShapes ) {
					shapeTile[ (int)each.getSavedX() ][ (int)each.getSavedY() ] = each;
				}
			}
			firstloop = false;
			
			for ( Array<Shape> eachList : listoflists ) {
				eachList.shuffle();
			}
			
			tmpList = new Array<Shape>(bottomLeft);
			for ( Shape each : topRight ) {
				Shape tmpShape = tmpList.pop();
				shapeTileArraySwap(each.getX(), each.getY(), tmpShape.getX(), tmpShape.getY());
			}
			
			tmpList = new Array<Shape>(bottomRight);
			for ( Shape each : topLeft ) {
				Shape tmpShape = tmpList.pop();
				shapeTileArraySwap(each.getX(), each.getY(), tmpShape.getX(), tmpShape.getY());
			}
			
		} while ( boardHasMatches(0) || findHintsOnShapeTile() <= 0 );
		
		tmpList = null;
		matchList.clear();							// do some cleaning.
		for ( Array<Shape> eachList : listoflists ) {
			eachList.clear();
		}
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
	public void setBlanks(int[][] blanks) {
		for (int[] each : blanks ) {
			int x = each[0];
			int y = each[1];
			allBaseTiles.removeValue(boardTile[x][y], false);
			allShapes.removeValue(shapeTile[x][y], false);
			shapeTile[x][y] = new Blank(this);
		}
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
		listoflists = null;
		bottomLeft = null;
		bottomRight = null;
		topLeft = null;
		topRight = null;
		oddShapes = null;
	}
//----------------------------------------------End-of-Class--------
}	
