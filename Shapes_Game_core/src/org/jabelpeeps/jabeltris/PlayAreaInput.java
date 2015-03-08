package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

public class PlayAreaInput extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameLogic logic;
		private final PlayArea game;
		private Vector3 touch = new Vector3();
		private int xSaved = -10;
		private int ySaved = -10;
		private int x_size, y_size, x_offset, y_offset;
// ---------------------------------------------Constructor----------	
		
		public PlayAreaInput(PlayArea p, GameLogic l) {
				game = p;
				logic = l;
				x_size = game.getXsize();
				y_size = game.getYsize();
				x_offset = game.getXoffset();
				y_offset = game.getYoffset();
		}
		// ---------------------------------------------Methods--------------	
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			if ( logic.isReadyForNewCandidates() ) {
					
				touch.set(x, y, 0);
				Core.camera.unproject(touch);
				int dX = (int) (touch.x - x_offset)/4;
				int dY = (int) (touch.y - y_offset)/4;
				
				if ( outOfBounds(dX, dY) || tileIsBlank(dX, dY) ) {  // end event if touch was out of bounds 
						resetSavedTile();
					
				} else if ( xSaved == -10 && ySaved == -10 ) {      // runs when no tile selected.
						setSavedTile(dX, dY);
						 
				} else if ( xSaved == dX && ySaved == dY ) {      	// resets if this is second touch on same tile.
						resetSavedTile();
						 
				} else if ( !touching(xSaved, ySaved, dX, dY) ) { 	// runs if touch is not adjacent to selected tile.
						game.getShape(xSaved, ySaved).deselect();
						setSavedTile(dX, dY);
						
				} else {                                            // runs if none of the above conditions are true.
						logic.setSwapCandidates(xSaved, ySaved, dX, dY);
						resetSavedTile();
						return true;
				}
			}
			return false;
		}
		@Override
		public boolean touchDragged(int x, int y, int pointer) {
			if ( logic.isReadyForNewCandidates() ) {
				touch.set(x, y, 0);
				Core.camera.unproject(touch);
				int tX = (int) (touch.x - x_offset)/4;
				int tY = (int) (touch.y - y_offset)/4;
				if ( 	   !(tX == xSaved && tY == ySaved) 
						&& !(xSaved == -10 && ySaved == -10)
						&& !outOfBounds(tX, tY) 
						&& !tileIsBlank(tX, tY)
						&& touching(xSaved, ySaved, tX, tY) ) {
					logic.setSwapCandidates(xSaved, ySaved, tX, tY);
					resetSavedTile();
					return true;
				}
			}
			return false;
		}

		private boolean outOfBounds(int x, int y) {
			if ( x < 0 || y < 0 || x >= x_size || y >= y_size ) {
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
			if ( xSaved != -10 && ySaved != -10 ) {
				game.getShape(xSaved, ySaved).deselect();
			}
			xSaved = -10;
			ySaved = -10;
		}
		private void setSavedTile(int x, int y) {
			game.getShape(x, y).select();
			xSaved = x;
			ySaved = y;				
		}
		private boolean tileIsBlank(int x, int y) {
			return game.getShape(x, y).type == "blank";
		}
}
