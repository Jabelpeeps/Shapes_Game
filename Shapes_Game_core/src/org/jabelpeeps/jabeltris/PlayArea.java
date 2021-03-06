package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.shapes.Blank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class PlayArea implements Serializable {

// ---------------------------------------------Fields------------
	private LevelMaster level;
	private GameLogic logic;
	private SpritePlus[][] boardTile;
	public Color baseColor;
	private Shape[][] shapeTile;
	private int x_size = 10, y_size = 10, x_offset = 0, y_offset = 0;
	
	private Array<Shape> matchList = new Array<Shape>(false, 32, Shape.class);
	private Array<Shape> newShapeList = new Array<Shape>(false, 32);
	private Array<Shape> allShapes = new Array<Shape>(false, 128, Shape.class);
	private Array<SpritePlus> allBaseTiles = new Array<SpritePlus>(false, 128, SpritePlus.class);
	
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

	public void initialise(LevelMaster lev, GameLogic logi) {
		level = lev;
		logic = logi;
		shapeTile = new Shape[x_size][y_size];
		boardTile = new SpritePlus[x_size][y_size];
		listoflists.add(bottomLeft);
		listoflists.add(bottomRight);
		listoflists.add(topLeft);
		listoflists.add(topRight);
		
		SpritePlus tmpSprite;
		for( int i = 0; i < x_size; i++ ) {
	    	for( int j = 0; j < y_size; j++ ) {
			    tmpSprite = new SpritePlus().setPlayArea( this ).setOffsets( x_offset , y_offset );
			    tmpSprite.setRegion( Core.boardBaseTiles[ i ][ ( y_size - j -1 ) % 10 ] );
			    tmpSprite.setBounds( i * 4 + x_offset , j * 4 + y_offset , 4 , 4 );
			    tmpSprite.setColor( baseColor );
			    tmpSprite.saveAlpha().setOriginCenter();
			    boardTile[i][j] = tmpSprite;
			}
	    	allBaseTiles.addAll( boardTile[i] , 0 , y_size );
		}
	}
	@Override
	public void write(Json json) {
		json.writeValue( "x_size" , x_size );
		json.writeValue( "y_size" , y_size );
		json.writeValue( "x_offset" , x_offset );
		json.writeValue( "y_offset" , y_offset );
		json.writeValue( "totalShapesCleared" , totalShapesCleared );
		json.writeValue( "totalMatches" , totalMatches );
		json.writeValue( "score" , score );
		json.writeValue( "baseColor" , baseColor.toString() );
		
		json.writeArrayStart( "Shapes" );
		for ( Shape each : allShapes ) json.writeValue( each );
		json.writeArrayEnd();
	}
	
	@Override
	public void read(Json json, JsonValue jsonData) {
		x_size = jsonData.getInt( "x_size" );
		y_size = jsonData.getInt( "y_size" );
		x_offset = jsonData.getInt( "x_offset" );
		y_offset = jsonData.getInt( "y_offset" );
		totalShapesCleared = jsonData.getInt( "totalShapesCleared" );
		totalMatches = jsonData.getFloat( "totalMatches" );
		score = jsonData.getInt( "score" );
		baseColor = Color.valueOf( jsonData.getString( "baseColor" ) );
		
		JsonValue list = jsonData.get( "Shapes" );
		try {
			for ( JsonValue each = list.child; each != null; each = each.next ) {
				Class<?> shapeclass = ClassReflection.forName( Core.PACKAGE + "shapes." + each.getString( "Shape" ) );
				Shape tmpShape = (Shape) ClassReflection.newInstance( shapeclass );
				
				tmpShape.setPlayArea( this ).setOffsets( x_offset , y_offset ).setOriginAndBounds().setNewAlpha( 1f );
				tmpShape.read( json, each );
				allShapes.add( tmpShape );
			}
		} catch (ReflectionException e) {  e.printStackTrace();	}
	}
	protected void refillShapeTile() {
		for ( Shape each : allShapes ) 
			shapeTile[ each.getXi() ][ each.getYi() ] = each;
		findHintsOnShapeTile();
	}

	protected void fillBoard() {
		for ( int i = 0; i < x_size; i++ ) {
			inner:
			for ( int j = 0; j < y_size; j++ ) {
				if 		(  shapeTile[i][j] != null 
						&& shapeTile[i][j].isBlank() ) 
					continue inner;
	    		shapeTile[i][j] = level.makeNewShape( i , j , x_offset , y_offset , this );
	    		allShapes.add( shapeTile[i][j] );
	    	} 
	    }
		shuffleShapes();        			// includes checks to exclude pre-existing matches 
													// and positions with no available moves.
		for ( int i = 0; i < x_size; i++ ) 
			for ( int j = 0; j < y_size; j++ ) 
				shapeTile[i][j].setPosition( i , j );
	}
	
	void spinShapesIntoPlace() {
		spinShapesIntoPlace(allShapes);
	}
	void spinShapesIntoPlace(Array<Shape> list) {
		for ( int a = 1; a <= 9; a++ ) {
			for ( Shape each : list ) {
				each.setScale( a * 0.1f );
				each.setRotation( 180 - ( 20 * a ) );
				each.setAlpha( 0.1f + a * 0.1f );
			}
			Gdx.graphics.requestRendering();	
			Core.delay( 50 );
		}
	}
	
	void swirlShapesIntoPlace() {
		swirlShapesIntoPlace( allShapes , true );
	}
	void swirlShapesIntoPlace(Array<Shape> list) {
		swirlShapesIntoPlace( list , false );
	}
	void swirlShapesIntoPlace(Array<Shape> list, boolean placingAllShapes) {
		long time;
		for ( Shape each : list ) 
			each.setNewXY().setScale( 1f );
	
		if ( placingAllShapes ) {
			time = 30;
			divideListByQuadrant( list );
		} else {
			time = 50;
			list.shuffle();
			oddShapes.addAll( list );
		}
		allocateOddShapes();
		
		for ( Shape each : bottomLeft ) 
			each.saveNewOrigin(x_size, y_size).setPosition(x_size + 2, y_size + 2);
		
		for ( Shape each : bottomRight ) 
			each.saveNewOrigin(0, y_size).setPosition(-2, y_size + 2);
		
		for ( Shape each : topLeft ) 
			each.saveNewOrigin(x_size, 0).setPosition(x_size + 2, -2);
		
		for ( Shape each : topRight ) 
			each.saveNewOrigin(0, 0).setPosition(-2, -2);
	
		ArrayMap<Shape, Integer> shapesInMotion = new ArrayMap<Shape, Integer>(false, 48);
		Array<Shape> shapesInPlace = new Array<Shape>(8);
		Array<Shape> next4Shapes = new Array<Shape>(8);
		
		int loopsNeeded = bottomLeft.size + 10;
		for ( int a = 0; a <= loopsNeeded ; a++ ) {
			
			for (Array<Shape> eachList : listoflists ) 
				if ( eachList.size > 0 ) 
					next4Shapes.add( eachList.pop() );
						
			for ( Shape each : next4Shapes ) {
				each.setOriginToSavedOrigin().saveXY().rotate( 260 );
				shapesInMotion.put( each , a );
			} 
			next4Shapes.clear();
			
			for ( Entry<Shape, Integer> each : shapesInMotion ) {
				each.key.setOriginToSavedOrigin().rotate( 10 );
				each.key.setAlpha( (float) ( a - each.value ) / 10 );
				
				if ( a - each.value < 8 ) 
					moveShape( each.key , each.key.getNewXY() , 1 + ( a - each.value ) );
				if ( a - each.value == 10 ) 
					shapesInPlace.add( each.key );	
			}
			Gdx.graphics.requestRendering();
			
			for ( Shape each : shapesInPlace ) {
				each.setRotation( 0 );
				each.setOriginCenter();
				each.setScale( 0.9f );
				shapesInMotion.removeKey( each );
			}
			shapesInPlace.clear();
			Core.delay( time );
		}
		shapesInMotion = null;
		shapesInPlace = null;
		next4Shapes = null;
	}
	
	void dropShapesIntoPlace() {
		
		for ( Shape each : allShapes ) 
			each.setNewXY();
		
		ArrayMap<Shape, Integer> shapesInMotion = new ArrayMap<Shape, Integer>(false, 8);
		Shape tmpShape = null;
		
		for ( int a = 0; a < allShapes.size + 8 ; a++ ) {
			if ( a < allShapes.size ) {
				tmpShape = shapeTile[ a % x_size ][ a / x_size ];
				tmpShape.setAlpha(1f);
				shapesInMotion.put(tmpShape, a);
			}
			tmpShape = null;
			for ( Entry<Shape, Integer> each : shapesInMotion ) {
				moveShape( each.key.getX() , y_size , each.key , a - each.value + 1 );
				
				if ( each.key.getNewXY().yf == each.key.getY() ) 
					tmpShape = each.key;	
			}
			if ( tmpShape != null )	
				shapesInMotion.removeKey(tmpShape);
			
			Gdx.graphics.requestRendering();
			Core.delay(18);
		}
	}
	
	void replaceMatchedShapes() {
		Shape.blink(100, 2, matchList);
		
		Array<Shape> copyMatchList = new Array<Shape>(matchList);
		
		while ( matchList.size > 0 ) {			// divides matchList into subsets of each shape type.
			Array<Shape> tmpList = new Array<Shape>(16);
			Shape tmpShape = matchList.peek();
			
			for ( Shape each : matchList ) 
				if ( each.matches(tmpShape) ) 
					tmpList.add(each); 
			
			matchList.removeAll(tmpList, false);
			setGroupOrigin(tmpList);
			tmpList.clear();
		}
		for ( int a = 1; a <= 20; a++ ) {      		// animates the removing of the Shape sprites.
			for ( Shape each : copyMatchList ) {
				each.setAnimation(false);
				each.setScale( 0.8f + a * 0.1f );
				each.rotate(10);
				if ( a > 10 ) 
					each.setAlpha( 1f - (a - 10) * 0.1f );
			}
			Gdx.graphics.requestRendering();
			Core.delay(20);
		}		
		for ( Shape each : copyMatchList ) {		// replaces matched shapes with newly generated ones.
			int x = each.getXi();
			int y = each.getYi();
			allShapes.removeValue( each , false );
			shapeTile[x][y] = level.makeNewShape( x , y , x_offset , y_offset , this );
			newShapeList.add( shapeTile[x][y] );
		}
		long time = (long) ( ( Core.rand.nextGaussian() + 1.5 ) * 150 );
		Core.delay( time );	

		allShapes.addAll( newShapeList );	
		swirlShapesIntoPlace( newShapeList );	
		
		newShapeList.clear();
	}
	private void setGroupOrigin(Array<? extends SpritePlus> list) {
		Coords total = Coords.get( 0f , 0f );
		
		for ( SpritePlus each : list ) {
			total.add( each );					
		}
		total.div( list.size );	
		
		for ( SpritePlus each : list ) 
			each.setOrigin( total.xf , total.yf );
		total.free();
	}
	
	boolean matchesFoundAndScored() {
		
		shapesClearedSinceLastMove = 0;
		matchesSinceLastMove = 0f;
		boolean matchesWereFound = false;
		
		while ( boardHasMatches(60) ) {							
			shapesClearedSinceLastMove += matchList.size;
			matchesWereFound = true; 	
			replaceMatchedShapes();
		}
		totalMatches += matchesSinceLastMove;				// add to sub-totals to scores.
		totalShapesCleared += shapesClearedSinceLastMove;
		score += Math.pow( matchesSinceLastMove , 3 ) * totalMatches * shapesClearedSinceLastMove;
		Gdx.graphics.requestRendering();
		
		return matchesWereFound;
	}

	boolean boardHasMatches(long time) {
		Core.delay( time );
		boolean matchesFound = false;
		Shape tmpShape;
		
		for ( int i = 0; i < x_size; i++ ) {
	    	for ( int j = 0; j < y_size; j++ ) {
	    		tmpShape = shapeTile[i][j];
	    		float shapeMatch = tmpShape.checkMatch( i , j );
	    		
	    		if ( shapeMatch > 0f ) {
	    				matchesSinceLastMove += shapeMatch;			// adds to a sub-total for score recording.	
	    				matchesFound = true;
	    				matchList.add( tmpShape );
	    		}
	    	}         
	    }
		return matchesFound;
	}
	
	void animateSwap(Shape one, Shape two) {
		animateSwap( one.getXi() , one.getYi() , two.getXi() , two.getYi() );
	}
	void animateSwap(int x1, int y1, int x2, int y2) {
		
		for ( int a = 1; a <= 8; a++ ) {					// animate shapes into their new positions.
			moveShape( x1 , y1 , x2 , y2 , a );
			moveShape( x2 , y2 , x1 , y1 , a );
			shapeTile[x1][y1].setRotation( 180 - 20 * a );
			shapeTile[x2][y2].setRotation( 180 - 20 * a );
			Gdx.graphics.requestRendering();
			Core.delay(25);
		}
		shapeTile[x1][y1].setRotation(0);
		shapeTile[x2][y2].setRotation(0);
		Gdx.graphics.requestRendering();	
		
		shapeTileArraySwap( x1 , y1 , x2 , y2 ); 				// update game board with new positions.
	}
	
	void shapeTileArraySwap(Shape shape1, Shape shape2) {
		shapeTileArraySwap( shape1.getXi() , shape1.getYi() , shape2.getXi() , shape2.getYi() );
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
		shapeTile[oldX][oldY].setPosition( oldX + ( newX - oldX ) * anim8 / 8f , oldY + ( newY - oldY ) * anim8 / 8f );
	}
	private void moveShape(Shape s, Coords newco, float anim8) {
		moveShape( s , newco.xf , newco.yf , anim8 );
	}
	private void moveShape(Shape s, float newX, float newY, float anim8) {
		Coords saved = s.getSavedXY();
		s.setPosition( saved.xf + ( newX - saved.xf ) * anim8 / 8 , saved.yf + ( newY - saved.yf ) * anim8 / 8 );
	}
	private void moveShape(float oldX, float oldY, Shape s, float anim8) {
		Coords newco = s.getNewXY();
		s.setPosition( oldX + ( newco.xf - oldX ) * anim8 / 8 , oldY + ( newco.yf - oldY ) * anim8 / 8 );
	}
	protected void moveShape(Shape s, float anim8) {
		Coords newco = s.getNewXY();
		moveShape( s , newco.xf , newco.yf , anim8 ) ;
	}
	
	void divideListByQuadrant(Array<Shape> list) {
		int halfX = ( x_size + 1 ) / 2;        // produce halves that round up, instead of down.
		int halfY = ( y_size + 1 ) / 2;
		float eachX, eachY;
		
		for ( Shape each : list ) {
			eachX = each.getX();
			eachY = each.getY();
			if ( eachY < y_size/2 ) {
				
					if ( eachX < x_size/2 ) 
						bottomLeft.add( each );								
					else if ( eachX >= halfX ) 
						bottomRight.add( each );								
					else 
						oddShapes.add( each );
			}
			else if ( eachY >= halfY ) {
				
					if ( eachX < x_size/2 ) 
						topLeft.add( each );							
					else if ( eachX >= halfX )
						topRight.add( each );							
					else
						oddShapes.add( each );
			} 
			else oddShapes.add( each );
		}
	}
	private void allocateOddShapes() {
		int turnCorner = 1;
		while ( oddShapes.size > 0 ) {
			switch ( turnCorner++ ) {
			case 1:
				bottomLeft.add( oddShapes.pop() );
				break;
			case 2:
				bottomRight.add( oddShapes.pop() );
				break;
			case 3:
				topLeft.add( oddShapes.pop() );
				break;
			case 4:
				topRight.add( oddShapes.pop() );
				turnCorner = 1;
				break;
			}
		}
	}
	void shuffleBoard() {
		
		shuffleShapes();
		for ( int a = 1; a <= 8; a++ ) {							// animate shapes into new positions.
			for ( int i = 0; i < x_size; i++ ) 
				for (int j = 0; j < y_size; j++ ) 
					moveShape( shapeTile[i][j] , i , j , a );
		Gdx.graphics.requestRendering();
		Core.delay( 80 );
		}
	}
	void shuffleShapes() {
		
		for ( Shape each : allShapes ) 				// saving x & y now makes the animation easier to run smoothly.
			each.saveXY();
		
		divideListByQuadrant( allShapes );
		int idealListSize = allShapes.size / 4 ;

		for ( Array<Shape> eachList : listoflists ) 
			while ( eachList.size > idealListSize ) 
				oddShapes.add( eachList.pop() );
		
		oddShapes.shuffle();

		for ( Array<Shape> eachList : listoflists ) 
			while ( eachList.size < idealListSize ) 
				eachList.add( oddShapes.pop() );
		
		Array<Shape> tmpList;
		boolean firstloop = true;
		
		do {
			if ( !firstloop ) {
				for ( Shape each : allShapes ) {
					Coords saved = each.getSavedXY();
					shapeTile[ saved.xi ][ saved.yi ] = each;
				}
			}
			firstloop = false;
			
			for ( Array<Shape> eachList : listoflists ) 
				eachList.shuffle();
			
			tmpList = new Array<Shape>(bottomLeft);
			
			for ( Shape each : topRight ) 
				shapeTileArraySwap( each , tmpList.pop() );
			
			tmpList = new Array<Shape>(bottomRight);
			
			for ( Shape each : topLeft ) 
				shapeTileArraySwap( each , tmpList.pop() );
				
			for ( Shape each : oddShapes ) 
				shapeTileArraySwap( each , shapeTile[ x_size - each.getXi() - 1 ][ y_size - each.getYi() - 1 ] );
			
		} while ( boardHasMatches(0) || findHintsOnShapeTile() <= 0 );
		
		tmpList = null;							// do some cleaning.
		oddShapes.clear();
		matchList.clear();
		for ( Array<Shape> eachList : listoflists ) 
			eachList.clear();
	}

	private int findHintsOnShapeTile() {   			// adds potential moves to hintList(s).
		long starttime = 0;
		if ( Core.LOGGING ) starttime = TimeUtils.nanoTime();
		logic.clearAllHints();
		int numberOfHints = 0;
		
		for ( int i = 0; i < x_size; i++ ) 
			for (int j = 0; j < y_size; j++ ) 
				if ( shapeTile[i][j].addHintsToList(i, j) )
					numberOfHints++;
		
		if ( Core.LOGGING ) System.out.println( "findHintsOnShapeTile execution time = " + 
								TimeUtils.nanosToMillis( TimeUtils.timeSinceNanos( starttime ) ) + " milliSecs");
		return numberOfHints;
	}
	
	int findHintsInAllshape() {   			// adds potential moves to hintList(s).
		long starttime = 0;
		if ( Core.LOGGING ) starttime = TimeUtils.nanoTime();
		logic.clearAllHints();
		int numberOfHints = 0;
		
		for ( Shape each : allShapes ) 
			if ( each.addHintsToList() )
				numberOfHints++;
		
		if ( Core.LOGGING ) System.out.println( "findHintsOnBoard execution time = " + 
				TimeUtils.nanosToMillis( TimeUtils.timeSinceNanos( starttime ) ) + " milliSecs");
		
		return numberOfHints;
	}
	/** Converts input Coordinates into PlayArea tiles, having already compensated for any 
	 * offsets in use by the current level.
	 * @param x - the x value of the input event.
	 * @param y - the y value of the input event.
	 * @param out - an instance of Coords that will be set with the output values. */
	void cameraUnproject(float x, float y, Coords out) {
		touch.set( x , y , 0 );
		Core.camera.unproject( touch );
		out.set( ( touch.x - x_offset ) / 4 , ( touch.y - y_offset ) / 4 );
	}
	private final Vector3 touch = new Vector3();
	
	private boolean outOfBounds(int x, int y) {
		return ( x < 0 || y < 0 || x >= x_size || y >= y_size );
	}
// ---------------------------------------------------------------------Getters and Setters
	
	protected Shape getShape(int x, int y) {
		return outOfBounds(x, y) ? Blank.get( this , true )
								 : shapeTile[x][y];
	}
	protected Shape getShape(Coords each) {      			
		return getShape( each.xi , each.yi );
	}
	protected void shapeTilePut(int x, int y, Shape each) {
		shapeTile[x][y] = each;
	}
	protected void shapeTilePut(Coords newPlace, Shape each) {
		shapeTilePut( newPlace.xi , newPlace.yi , each);
	}
	protected SpritePlus getBoardTile(int x, int y) {
		return boardTile[x][y];
	}
	protected SpritePlus getBoardTile(Coords each) {
		return getBoardTile( each.xi , each.yi );
	}
	public SpritePlus getBoardTile(Shape each) {
		return getBoardTile( each.getXi() , each.getYi() );
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
	protected int getMatchListSize() {						// This group of calls are used by 
		return matchList.size;							// demo GameLogic to extract the
	}													// information it needs to choose its
	protected void clearMatchList() {						// next moves, and also reset the values
		matchList.clear();								// in private fields of this class.
	}
	public boolean playAreaIsReady() {
		return readyForPlay;
	}
	protected void setPlayAreaReady() {
		readyForPlay = true;
	}
	protected Shape[] getAllShapes() {
		return allShapes.toArray();
	}
	protected SpritePlus[] getAllBoardTiles() {
		return allBaseTiles.toArray();
	}
	public void dispose() {
		readyForPlay = false;
		allBaseTiles = null;
		allShapes = null;
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
