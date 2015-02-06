package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

class InputEvents extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameBoard board;
		private Vector3 touch = new Vector3();
		private int dX = -10;
		private int dY = -10;
		private int uX = -10;
		private int uY = -10;
		private int xSaved = -10;
		private int ySaved = -10;
// ---------------------------------------------Constructor----------	
		InputEvents(GameBoard b) {
			board = b;
		}
// ---------------------------------------------Methods--------------	
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			touch.set(x, y, 0);
			Core.camera.unproject(touch);
			dX = (int) touch.x/4;
			dY = (int) touch.y/4;
			
			if ( outOfBounds(dX, dY) ) {                      // end event if touch was out of bounds 
					if ( xSaved != -10 && ySaved != -10 ) {
							board.getShape(xSaved, ySaved).deselect();
					}
					resetSavedTile();
				
			} else if ( xSaved == -10 && ySaved == -10 ) {      // runs when no tile selected.
					setSavedTile(dX, dY);
					 
			} else if ( xSaved == dX && ySaved == dY ) {      	// resets if this is second touch on same tile.
					board.getShape(xSaved, ySaved).deselect();
					resetSavedTile();
					 
			} else if ( !touching(xSaved, ySaved, dX, dY) ) { 	// runs if touch is not adjacent to selected tile.
					board.getShape(xSaved, ySaved).deselect();
					setSavedTile(dX, dY);
					
			} else {                                            // runs if none of the above conditions are true.
					board.getShape(dX, dY).select();
					board.setSwapCandidates(xSaved, ySaved, dX, dY);
					resetSavedTile();
			}		
			return true;
		}
		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			touch.set(x, y, 0);
			Core.camera.unproject(touch);
			uX = (int) touch.x/4;
			uY = (int) touch.y/4;
														// end event if any of these are true (leaving tile selected by touchDown):- 
			if ( ( uX == xSaved && uY == ySaved )  		// - if touch released where started
				|| ( xSaved == -10 && ySaved == -10 )  	// - if it is still the end of the first touch.
				|| ( outOfBounds(uX, uY) ) ) {            // - if touch released out of bounds
			
			} else if ( !touching(xSaved, ySaved, uX, uY) ) {    // end event if touch released too far from first tile.
					board.getShape(xSaved, ySaved).deselect();
					resetSavedTile();
			} else {
					board.getShape(uX, uY).select();
					board.setSwapCandidates(xSaved, ySaved, uX, uY);
					resetSavedTile();
			}
			return true;
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
			board.getShape(x, y).select();
			xSaved = x;
			ySaved = y;				
		}
}
