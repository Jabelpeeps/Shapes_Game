package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectSet;

public class GameBoard implements Runnable {

// ---------------------------------------------Fields------------
	final Master level;
	private boolean levelComplete = false;
	
	private static Shape[][] playArea;
	static int x_size, y_size;
	int tdX = -10, tdY = -10, tuX = -10, tuY = -10;
	private int xSaved = -10, ySaved = -10;
	
	private ObjectSet<Shape> matchList = new ObjectSet<Shape>(64);
	private ObjectSet<Shape> newShapeList = new ObjectSet<Shape>(64);
	
// ---------------------------------------------Constructor(s)--------	
		
	public GameBoard(Master l) {
		this(10,10, l);
	}
	// two constructors allows for different sizes boards in future.
	public GameBoard(int x, int y, Master l) {
		x_size = x;
		y_size = y;
		level = l;
		playArea = new Shape[x][y];
		Shape.board = this;
	}
//-----------------------------------------------------Methods-------
	
	@Override
	public void run() {
		fillBoard();
		do {
			//if ( Gdx.input.justTouched() )	{
				if ( tuX != -10 && tuY != -10 ) {
					upTouch(tuX, tuY);
				}
				if ( tdX != -10 && tdY != -10 ) {
					downTouch(tdX, tdY);
				}
			//}
			boardHasMatches(200);
		} while ( !levelComplete );				// TODO decide how levels will be completed.
		clear();     							// TODO fill in the clear() method.
	}
	
	void fillBoard() {		// (maybe) TODO change this method to drop Shapes from the top.
		Gdx.graphics.requestRendering();
		for ( int i=0; i<=9; i++ ) {
	    	for ( int j=0; j<=9; j++ ) {
	    		playArea[i][j] = level.makeNewShape(i, j);
	    	}         
	    } 
		for ( int a=1; a<=9; a++ ) {
			for ( int i=0; i<=9; i++ ) {
				for ( int j=0; j<=9; j++ ) {
					playArea[i][j].setScale(a*0.1f);
					playArea[i][j].setRotation(180-(20*a));
				}
				Gdx.graphics.requestRendering();	
				Core.delay(5);
			}
		}
	}
	
	boolean boardHasMatches(long time) {
		Core.delay(time);
		boolean matchesfound = false;
		for ( int i=0; i<=9; i++ ) {
	    	for ( int j=0; j<=9; j++ ) {
	    		if ( playArea[i][j].checkMatch() ) {
	    				playArea[i][j].select();
	    				matchesfound = true;
	    				matchList.add(playArea[i][j]);
	    		}
	    	}         
	    }
		if ( matchesfound ) {                 			// animates the shrinking of the Shape sprites.
			for ( int i=1; i<=9; i++ ) {
				for ( Shape each : matchList ) {
					each.setScale(1-(i*0.1f));
					each.setRotation(-20*i);
				}
				Gdx.graphics.requestRendering();
				Core.delay(60);
			}											// replaces matched shapes with newly generated ones.
			for ( Shape each : matchList ) {
				each.deselect();
				int x = (int) (each.getX()/3);
				int y = (int) (each.getY()/3);
				playArea[x][y] = level.makeNewShape(x, y);
				newShapeList.add(playArea[x][y]);
			}											// animates the insertion of new shapes.
			matchList.clear();
			for ( int i=1; i<=9; i++ ) {
				for ( Shape each : newShapeList ) {
					each.setScale(i*0.1f);
					each.setRotation(180-(20*i));
				}
				Gdx.graphics.requestRendering();	
				Core.delay(60);
			}
			for ( Shape each : newShapeList ) {
				each.setScale(0.9f);
				each.setRotation(0);
			}
			newShapeList.clear();
		}
		Gdx.graphics.requestRendering();
		return matchesfound;
	}
		
	public void downTouch(int x, int y) {
		
		if ( outOfBounds(x, y) ) {                      // end event if touch was out of bounds 
				if ( xSaved != -10 && ySaved != -10 ) {
						playArea[xSaved][ySaved].deselect();
				}
				resetSavedTile();
			
		} else if ( xSaved == -10 && ySaved == -10 ) {      // runs when no tile selected.
				setSavedTile(x, y);
				 
		} else if ( xSaved == x && ySaved == y ) {      // resets if this is second touch on same tile.
				playArea[xSaved][ySaved].deselect();
				resetSavedTile();
				 
		} else if ( !touching(xSaved, ySaved, x, y) ) { // runs if touch is not adjacent to selected tile.
				playArea[xSaved][ySaved].deselect();
				setSavedTile(x, y);
				
		} else {                                            // runs if none of the above conditions are true.
				playArea[x][y].select();
				doSwapIfSwapable(xSaved, ySaved, x, y);
		}
		tdX = -10;
		tdY = -10;
		if ( tuX != -10 && tuY != -10 ) {
			upTouch(tuX, tuY);
		}
	}
	public void upTouch(int x, int y) {
		if ( tdX != -10 && tdY != -10 ) {
				downTouch(tdX, tdY);
		}	                                      	// end event if any of these are true (leaving tile selected by touchDown):- 
		if ( ( x == xSaved && y == ySaved )  		// - if touch released where started
		  || ( xSaved == -10 && ySaved == -10 )  	// - if it is still the end of the first touch.
		  || ( outOfBounds(x, y) ) ) {              // - if touch released out of bounds
				
		} else if ( !touching(xSaved, ySaved, x, y) ) {    // end event if touch released too far from first tile.
				playArea[xSaved][ySaved].deselect();
				resetSavedTile();
		} else {
				playArea[x][y].select();
				doSwapIfSwapable(xSaved, ySaved, x, y);
		}
		tuX = -10;
		tuY = -10;
	}
	private void doSwapIfSwapable(int x1, int y1, int x2, int y2) {
		resetSavedTile();			// Swaps shapes, tests for matches, and swaps the shapes back if no match found.
		swap(x1, y1, x2, y2);
		
		playArea[x1][y1].deselect();
		playArea[x2][y2].deselect();
		
		if ( !boardHasMatches(0) ) {
				swap(x1, y1, x2, y2);
		}
	}
	private void swap(int x1, int y1, int x2, int y2) {
		playArea[x1][y1].setCenter(x2, y2);		          	// give shape sprites new centres.
		playArea[x2][y2].setCenter(x1, y1);
		
		Shape tmpShape = playArea[x1][y1];					// update game board with new positions.
		playArea[x1][y1] = playArea[x2][y2];
		playArea[x2][y2] = tmpShape;
	}
	private boolean outOfBounds(int x, int y) {
		if ( x < 0 || y < 0 || x > 9 || y > 9 ) {
				return true;
		} 
		return false;
	}
	private boolean touching(int x1, int y1, int x2, int y2) {
		if ( (y1 == y2 && (x1+1 == x2 || x1-1 == x2) ) 
		  || (x1 == x2 && (y1+1 == y2 || y1-1 == y2) ) ) {
				return true;
		} 
		return false;
	}
	private void resetSavedTile() {
		xSaved = -10;
		ySaved = -10;
	}
	private void setSavedTile(int x, int y) {
		playArea[x][y].select();
		xSaved = x;
		ySaved = y;				
	}	
	public Shape getShape(int x, int y) {
		return playArea[x][y];
	}
	void drop() {			// TODO method to handle the moving of shapes into empty spaces 
	}
		
	void clear() {			// TODO method to clear board when level finished
	}
//----------------------------------------------End-of-Class--------
}	

//An attempt to animate swaps, needs further debugging... TODO
//removed for swap() for ease of code reading.
//
//if ( x1 == x2 && y1 < y2 ) {
//for ( int i=0; i==6; i++ ) {
//board.getShape(x1, y1).translateY( 0.5f );
//board.getShape(x2, y2).translateY( -0.5f );
//board.renderBoard();
//}
//} else if ( x1 == x2 && y1 > y2 ) {
//for ( int i=0; i==6; i++ ) {
//board.getShape(x1, y1).translateY( -0.5f );
//board.getShape(x2, y2).translateY( 0.5f );
//board.renderBoard();
//}
//} else if ( y1 == y2 && x1 < x2 ) {
//for ( int i=0; i==6; i++ ) {
//board.getShape(x1, y1).translateX( 0.5f );
//board.getShape(x2, y2).translateX( -0.5f );
//board.renderBoard();
//}
//} else {
//for ( int i=0; i==6; i++ ) {
//board.getShape(x1, y1).translateX( -0.5f );
//board.getShape(x2, y2).translateX( 0.5f );
//board.renderBoard();
//}
//}