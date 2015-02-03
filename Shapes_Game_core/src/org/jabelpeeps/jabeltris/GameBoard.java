package org.jabelpeeps.jabeltris;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;

public class GameBoard {

// ---------------------------------------------Fields------------
	final Master level;
	
	private static Shape[][] playArea;
	static int x_size, y_size;	
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
		
		fillBoard();
	}
//-----------------------------------------------------Methods-------
	
	void fillBoard() {
		// TODO (maybe) change this method to drop Shapes from the top.
		
		do {
			for (int i=0; i<=9; i++) {
		    	for (int j=0; j<=9; j++) {
		    		if ( playArea[i][j] == null ) {
		    				playArea[i][j] = level.makeNewShape(i, j);
		    		}
		    	}         
		    } 
			renderBoard();
		} while ( boardHasMatches() );
	}
	
	private boolean boardHasMatches() {
		
		boolean matchesfound = false;
		for (int i=0; i<=9; i++) {
	    	for (int j=0; j<=9; j++) {
	    		if ( playArea[i][j].checkMatch(i, j) ) {
	    				matchesfound = true;
	    		}
	    	}         
	    }             
		clearMatched();
		return matchesfound;
	}
	
	void clearMatched() {
		for (int i=0; i<=9; i++) {
			for (int j=0; j<=9; j++) {
				if ( playArea[i][j].matched ) {
						playArea[i][j] = null;
				}
			}
		}
	}
		
	void renderBoard() {
		try {  
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	    Gdx.graphics.requestRendering();
	}
	
	Shape getShape(int x, int y) {
		return playArea[x][y];
	}
	void setShape(int x, int y, Shape s) {
		playArea[x][y] = s;
	}
	
	void drop() {
		// TODO method to handle the moving of shapes into empty spaces 
	}
	
	void clear() {
		// TODO method to clear board when level finished
	}
		
//----------------------------------------------End-of-Class--------
}		
