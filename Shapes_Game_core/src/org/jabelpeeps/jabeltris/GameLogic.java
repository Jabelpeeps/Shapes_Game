package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator;

public class GameLogic implements Runnable {

// ---------------------------------------------Fields------------
	private final Master level;
	private boolean levelComplete = false;
	
	Sprite[][] boardTile;
	private static Shape[][] shapeTile;
	private static int x_size, y_size;
	private ObjectSet<Shape> matchList = new ObjectSet<Shape>(64);
	private ObjectSet<Shape> newShapeList = new ObjectSet<Shape>(64);
	protected ObjectSet<Shape> hintList = new ObjectSet<Shape>(32);
	private ObjectSet<Shape> shuffleListTopLeft = new ObjectSet<Shape>(32);
	private ObjectSet<Shape> shuffleListTopRight = new ObjectSet<Shape>(32);
	private ObjectSet<Shape> shuffleListBottomLeft = new ObjectSet<Shape>(32);
	private ObjectSet<Shape> shuffleListBottomRight = new ObjectSet<Shape>(32);
	
	private int c1x, c1y, c2x, c2y;
	private boolean candidatesAreSet = false;
	private float matchesSinceLastMove = 0f;
	private float totalMatches = 0f;
	@SuppressWarnings("unused")
	private int totalShapesCleared = 0;
	private int shapesClearedSinceLastMove = 0;
	private int score = 0;	
	private boolean shuffleCalled = false;
// ---------------------------------------------Constructor(s)--------
		
	GameLogic(Master l) {
		this(10,10, l);
	}
	// two constructors allows for different sizes boards in future.
	GameLogic(int x, int y, Master l) {
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
		while ( boardHasMatches(60) ) {				// clear the board of any pre-existing matches.
			replaceMatchedShapes();
			matchList.clear();
		}
		findHintsOnBoard();
		
		do {             							// From here, this is is the main loop of the game logic.
			if ( candidatesAreSet ) {
				doSwapIfSwapable(c1x, c1y, c2x, c2y);
			}
			if ( shuffleCalled ) {
				shuffleBoard();
				while ( boardHasMatches(100) ) {		// clear the board of any newly formed matches.
					replaceMatchedShapes();
					matchList.clear();
				}
				hintList.clear();
			}
			if ( hintList.size <= 0 ) {
				findHintsOnBoard();
				if ( hintList.size < 1 ) levelComplete = true;
			}
		} while ( !levelComplete );					// TODO decide how levels will be completed.
		clear();     								// TODO fill in the clear() method.
	}										// The end of the main logic loop.
	
	private void findHintsOnBoard() {   			// method adds potential moves to hintList.
		for ( int i = 0; i < x_size; i++ ) {
			for ( int j = 0; j < y_size; j++ ) {
			    	shapeTile[i][j].findHint();	      
			}         
		}
	Gdx.graphics.requestRendering();
	}
	
	private void fillBoard() {			// TODO (maybe) add a method to drop Shapes from the top.
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
	
	private boolean boardHasMatches(long time) {
		Core.delay(time);
		boolean matchesFound = false;
		for ( int i = 0; i < x_size; i++ ) {
	    	for ( int j = 0; j < y_size; j++ ) {
	    		float shapeMatch = shapeTile[i][j].checkMatch();	    		
	    		if ( shapeMatch > 0f ) {
	    				matchesSinceLastMove += shapeMatch;			// adds to a sub-total for score recording.	
	    				shapeTile[i][j].select();
	    				matchesFound = true;
	    				matchList.add(shapeTile[i][j]);
	    		}
	    	}         
	    }
		return matchesFound;
	}
	
	private void replaceMatchedShapes() {
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
		newShapeList.clear();								
		hintList.clear();
	}	
	
	private void doSwapIfSwapable(int x1, int y1, int x2, int y2) {
		boolean matchesWereFound = false;
		shapesClearedSinceLastMove = 0;
		matchesSinceLastMove = 0f;
		
		selectAndSwap(x1, y1, x2, y2);              			// Swap the shapes... 
		while ( boardHasMatches(60) ) { 						// run the board...
			replaceMatchedShapes();
			shapesClearedSinceLastMove += matchList.size;					
			matchList.clear();
			matchesWereFound = true;
		}
		if ( matchesWereFound ) {		  			
			totalMatches += matchesSinceLastMove;				// add to sub-total to score.
			totalShapesCleared += shapesClearedSinceLastMove;
			score += Math.pow(matchesSinceLastMove, 3) * totalMatches * shapesClearedSinceLastMove;
			Gdx.graphics.requestRendering();
		} else {
			Core.delay(80);
			selectAndSwap(x1, y1, x2, y2);						// or swap shapes back no match found
		}
		candidatesAreSet = false;               	// re-open input variables from graphics/input thread.
	}
	
	private void selectAndSwap(int x1, int y1, int x2, int y2) {
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
	private void moveShape(int nX, int nY, int oX, int oY, int anim8) {
		shapeTile[nX][nY].setPosition(nX + ((oX-nX)*anim8/8f), nY + ((oY-nY)*anim8/8f));
	}
	
	private void shuffleBoard() {									// divide shapes between four ObjectSets, depending on their quadrant.
		for ( int i = 0; i < x_size / 2; i++ ) {
			for ( int j = 0; j < y_size / 2; j++ ) {
				shuffleListBottomLeft.add(shapeTile[i][j]);
				shuffleListBottomRight.add(shapeTile[i + x_size/2][j]);
				shuffleListTopLeft.add(shapeTile[i][j + y_size/2]);
				shuffleListTopRight.add(shapeTile[i + x_size/2][j + y_size/2]);
			}
		}
		ObjectSetIterator<Shape> shuffleBottomLeft = new ObjectSet.ObjectSetIterator<Shape>(shuffleListBottomLeft);
		ObjectSetIterator<Shape> shuffleBottomRight = new ObjectSet.ObjectSetIterator<Shape>(shuffleListBottomRight);
		ObjectSetIterator<Shape> shuffleTopLeft = new ObjectSet.ObjectSetIterator<Shape>(shuffleListTopLeft);
		ObjectSetIterator<Shape> shuffleTopRight = new ObjectSet.ObjectSetIterator<Shape>(shuffleListTopRight);
		
		for ( int i = 0; i < x_size / 2; i++ ) {               		// put shapes into new slots in shapeTile[][]
			for ( int j = 0; j < y_size / 2; j++ ) {				// sprite coords of old locations are retained.
				shapeTile[i][j] = shuffleTopRight.next();
				shapeTile[i + x_size/2][j] = shuffleTopLeft.next();
				shapeTile[i][j + y_size/2] = shuffleBottomRight.next();
				shapeTile[i + x_size/2][j + y_size/2] = shuffleBottomLeft.next();
			}
		}
		Integer[][] oldXs = new Integer[x_size][y_size];
		Integer[][] oldYs = new Integer[x_size][y_size];
		
		for ( int i = 0; i < x_size; i++ ) {                    	// get old coords for each shape by accessing the sprite coords,  
			for (int j = 0; j < y_size; j++ ) {						// and save in arrays that correspond to their new locations.
					oldXs[i][j] = (int)shapeTile[i][j].getX();
					oldYs[i][j] = (int)shapeTile[i][j].getY();
			}
		}
		for ( int a = 1; a <= 8; a++ ) {							// animate moves
			for ( int i = 0; i < x_size; i++ ) {
				for (int j = 0; j < y_size; j++ ) {
					moveShape(i, j, oldXs[i][j], oldYs[i][j], a);
				}
			}
			Gdx.graphics.requestRendering();
			Core.delay(80);
		}
		for ( int i = 0; i < x_size; i++ ) {                    	// save old coords for each shape in arrays that 
			for (int j = 0; j < y_size; j++ ) {						// correspond to their new locations.
					shapeTile[i][j].setPosition(i, j);
			}
		}
		Gdx.graphics.requestRendering();
		
		shuffleListBottomLeft.clear();								// do some clean up.
		shuffleListBottomRight.clear();
		shuffleListTopLeft.clear();
		shuffleListTopRight.clear();
		shuffleBottomLeft.reset();
		shuffleBottomRight.reset();
		shuffleTopLeft.reset();
		shuffleTopRight.reset();
		
		shuffleCalled  = false;
	}
		
	private void clear() {			// TODO method to clear board when level finished
//		System.out.println("---------------------------------");
//		System.out.println("No more possible moves Found!");
//		System.out.println("Total Matches:- " + (int)totalMatches);
//		System.out.println("Total Shapes Cleared:- " + totalShapesCleared);
//		System.out.println("---------------------------------");
//		System.out.println("Test Score = " + score);
//		System.out.println("---------------------------------");
		return;
	}
// ---------------------------------------------------------------------Getters and Setters
	public Shape getShape(int x, int y) {
		return shapeTile[x][y];
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
	public int getScore() {
		return score;
	}
	public int getNumberOfHints() {
		return hintList.size;
	}
	public void setShuffle() {
		shuffleCalled = true;
	}
//----------------------------------------------End-of-Class--------
}	


