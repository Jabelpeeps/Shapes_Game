package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

class InputEvents extends InputAdapter {

// ---------------------------------------------Field(s)------------
	final GameBoard board;
	
	private Vector3 touch = new Vector3();
	static int firstX = -10, firstY = -10, tdX, tdY, tuX, tuY;
	
// ---------------------------------------------Constructor(s)--------	
	public InputEvents(GameBoard b) {
		board = b;
	}
// ---------------------------------------------Methods--------------	
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		touch.set(x, y, 0);
		Core.camera.unproject(touch);
		tdX = (int)(touch.x / 3);
		tdY = (int)(touch.y / 3);

		if ( outOfBounds(tdX, tdY) ) {                      // end event if touch was out of bounds 
				if ( firstX != -10 && firstY != -10 ) {
						deselect(board.getShape(firstX, firstY));
				}
				resetAndRender();
				
		} else if ( firstX == -10 && firstY == -10 ) {      // runs when no tile selected.
				select(board.getShape(tdX, tdY));
				setAndRender();
				 
		} else if ( firstX == tdX && firstY == tdY ) {      // resets if this is second touch on same tile.
				deselect(board.getShape(firstX, firstY));
				resetAndRender();
				 
		} else if ( !touching(firstX, firstY, tdX, tdY) ) { // runs if touch is not adjacent to selected tile.
				deselect(board.getShape(firstX, firstY));
				select(board.getShape(tdX, tdY));
				setAndRender();
				
		} else {                                            // runs if none of the above conditions are true.
				select(board.getShape(tdX, tdY));
				board.renderBoard();
				doSwapIfSwapable(firstX, firstY, tdX, tdY);
		}				
		return true;
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		touch.set(x, y, 0);
		Core.camera.unproject(touch);
		tuX = (int)(touch.x / 3);
		tuY = (int)(touch.y / 3);
		
		// end event if any of these are true (leaving tile selected by touchDown):- 
		if ( ( tuX == firstX && tuY == firstY )  // - if touch released where started
		  || ( firstX == -10 && firstY == -10 )  // - if it is still the end of the first touch.
		  || ( outOfBounds(tuX, tuY) ) ) {       // - if touch released out of bounds
				 return true;
		}				
		// end event if touch released too far from first tile.
		if ( !touching(firstX, firstY, tuX, tuY) ) { 
				deselect(board.getShape(firstX, firstY));
				resetAndRender();
		} else {
				doSwapIfSwapable(firstX, firstY, tuX, tuY);
		}
		return true;
	}
	
	// Swaps shapes, tests for matches, and swaps the shapes back if no match found.
	private void doSwapIfSwapable(int x1, int y1, int x2, int y2) {
		
		firstX = -10;
		firstY = -10;
		swap(x1, y1, x2, y2);
		
		boolean matchfound = false;
		
		if ( board.getShape(x1, y1).checkMatch(x1, y1) ) matchfound = true;
		if ( board.getShape(x2, y2).checkMatch(x2, y2) ) matchfound = true;
		
		if ( matchfound ) {
				deselect(board.getShape(x1, y1));
				deselect(board.getShape(x2, y2));
				board.clearMatched();
				board.fillBoard();
		} else {
				swap(x1, y1, x2, y2);
				deselect(board.getShape(x1, y1));
				deselect(board.getShape(x2, y2));
				board.renderBoard();
		}
	}
	// updates playArea[][] and provides the animation of the shapes swapping. 
	private void swap(int x1, int y1, int x2, int y2) {
		
		select(board.getShape(x1, y1));
		select(board.getShape(x2, y2));
		board.renderBoard();

		// give shape sprites new centres.
		board.getShape(x1, y1).setCenter(x2, y2);
		board.getShape(x2, y2).setCenter(x1, y1);
		
		// update game board with new positions.
		Shape tmpShape = board.getShape(x1, y1);
		board.setShape(x1, y1, board.getShape(x2, y2));
		board.setShape(x2, y2, tmpShape);
	}
	
	private boolean outOfBounds(int x, int y) {
		if ( x < 0 || y < 0 || x > 9 || y > 9 ) {
				return true;
		} else {
				return false;
		}
	}
	private boolean touching(int x1, int y1, int x2, int y2) {
		if ( (y1 == y2 && (x1+1 == x2 || x1-1 == x2) ) 
		  || (x1 == x2 && (y1+1 == y2 || y1-1 == y2) ) ) {
				return true;
		} else return false;
	}
	private void select(Shape s) {
		s.setColor(s.color);
	}
	private void deselect(Shape s) {
		s.setColor(1f, 1f, 1f, 1f);
	}
	private void resetAndRender() {
		firstX = -10;
		firstY = -10;
		board.renderBoard();
	}
	private void setAndRender() {
		firstX = tdX;
		firstY = tdY;
		board.renderBoard();				
	}
}

//An attempt to animate swaps, needs further debugging... TODO
//             removed for swap() for ease of code reading.
//
//	if ( x1 == x2 && y1 < y2 ) {
//		for ( int i=0; i==6; i++ ) {
//			board.getShape(x1, y1).translateY( 0.5f );
//			board.getShape(x2, y2).translateY( -0.5f );
//			board.renderBoard();
//		}
//	} else if ( x1 == x2 && y1 > y2 ) {
//		for ( int i=0; i==6; i++ ) {
//			board.getShape(x1, y1).translateY( -0.5f );
//			board.getShape(x2, y2).translateY( 0.5f );
//			board.renderBoard();
//		}
//	} else if ( y1 == y2 && x1 < x2 ) {
//		for ( int i=0; i==6; i++ ) {
//			board.getShape(x1, y1).translateX( 0.5f );
//			board.getShape(x2, y2).translateX( -0.5f );
//			board.renderBoard();
//		}
//	} else {
//		for ( int i=0; i==6; i++ ) {
//			board.getShape(x1, y1).translateX( -0.5f );
//			board.getShape(x2, y2).translateX( 0.5f );
//			board.renderBoard();
//		}
//	}