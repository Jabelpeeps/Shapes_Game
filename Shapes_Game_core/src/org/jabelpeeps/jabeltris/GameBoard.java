package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectSet;

public class GameBoard implements Runnable {

// ---------------------------------------------Fields------------
	final Master level;
	private boolean levelComplete = false;
	
	Sprite[][] boardTile;
	private static Shape[][] spriteTile;
	static int x_size, y_size;
	private ObjectSet<Shape> matchList = new ObjectSet<Shape>(64);
	private ObjectSet<Shape> newShapeList = new ObjectSet<Shape>(64);
	private int c1x, c1y, c2x, c2y;
	private boolean candidatesAreSet = false;
	
// ---------------------------------------------Constructor(s)--------	
		
	public GameBoard(Master l) {
		this(10,10, l);
	}
	// two constructors allows for different sizes boards in future.
	public GameBoard(int x, int y, Master l) {
		x_size = x;
		y_size = y;
		level = l;
		spriteTile = new Shape[x][y];
		Shape.board = this;
		boardTile = new Sprite[x][y];
		   		
   		for( int i=0; i<=9; i++ ) {
	    	for( int j=0; j<=9; j++ ) {
			    boardTile[i][j] = new Sprite(Core.boardBaseTiles[i][j]);
			    boardTile[i][j].setBounds(i*4, j*4, 4, 4);
			}           
		}
	}
//-----------------------------------------------------Methods-------
	
	@Override
	public void run() {
		fillBoard();
		do {
			if ( candidatesAreSet ) {
				doSwapIfSwapable(c1x, c1y, c2x, c2y);
			}
			boardHasMatches(200);
		} while ( !levelComplete );				// TODO decide how levels will be completed.
		clear();     							// TODO fill in the clear() method.
	}
	
	void fillBoard() {		// (maybe) TODO change this method to drop Shapes from the top.
		Gdx.graphics.requestRendering();
		for ( int i=0; i<=9; i++ ) {
	    	for ( int j=0; j<=9; j++ ) {
	    		spriteTile[i][j] = level.makeNewShape(i, j);
	    	}         
	    } 
		for ( int a=1; a<=9; a++ ) {
			for ( int i=0; i<=9; i++ ) {
				for ( int j=0; j<=9; j++ ) {
					spriteTile[i][j].setScale(a*0.1f);
					spriteTile[i][j].setRotation(180-(20*a));
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
	    		if ( spriteTile[i][j].checkMatch() ) {
	    				spriteTile[i][j].select();
	    				matchesfound = true;
	    				matchList.add(spriteTile[i][j]);
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
				int x = (int) each.getX();
				int y = (int) each.getY();
				spriteTile[x][y] = level.makeNewShape(x, y);
				newShapeList.add(spriteTile[x][y]);
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
		
	private void doSwapIfSwapable(int x1, int y1, int x2, int y2) {
//		System.out.println("Coords Sent:- (" + x1 + ", " + y1 + ")(" + x2 + ", " + y2 + ")");
		swap(x1, y1, x2, y2);              	// Swap shapes... 
		
		spriteTile[x1][y1].deselect();
		spriteTile[x2][y2].deselect();
		
		if ( !boardHasMatches(0) ) {		// test for matches...
				swap(x1, y1, x2, y2);		// and swap back if no match found
		}
		candidatesAreSet = false;
	}
	private void swap(int x1, int y1, int x2, int y2) {
		spriteTile[x1][y1].setCenter(x2, y2);		    // give shape sprites new centres.
		spriteTile[x2][y2].setCenter(x1, y1);
		
		Shape tmpShape = spriteTile[x1][y1];			// update game board with new positions.
		spriteTile[x1][y1] = spriteTile[x2][y2];
		spriteTile[x2][y2] = tmpShape;
	}
	public Shape getShape(int x, int y) {
		return spriteTile[x][y];
	}
	public void setSwapCandidates(int x1, int y1, int x2, int y2) {
		c1x = x1;
		c1y = y1;
		c2x = x2;
		c2y = y2;
		candidatesAreSet = true;
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