package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayAreaInput extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameLogic logic;
		private Vector3 touch = new Vector3();
		private final Vector2 DIAG = new Vector2(1, 1);
		private int xSaved = -10;
		private int ySaved = -10;
		private int x_size, y_size;
// ---------------------------------------------Constructor----------	
		public PlayAreaInput(GameLogic l) {
				this(l, 10, 10);
		}
		public PlayAreaInput(GameLogic l, int x, int y) {
				logic = l;
				x_size = x;
				y_size = y;
		}
		// ---------------------------------------------Methods--------------	
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			if ( logic.isReadyForNewCandidates() ) {
					
				touch.set(x, y, 0);
				Core.camera.unproject(touch);
				int dX = (int) touch.x/4;
				int dY = (int) touch.y/4;
				
				if ( outOfBounds(dX, dY) ) {                      // end event if touch was out of bounds 
						if ( xSaved != -10 && ySaved != -10 ) {
								logic.getShape(xSaved, ySaved).deselect();
						}
						resetSavedTile();
					
				} else if ( xSaved == -10 && ySaved == -10 ) {      // runs when no tile selected.
						setSavedTile(dX, dY);
						 
				} else if ( xSaved == dX && ySaved == dY ) {      	// resets if this is second touch on same tile.
						logic.getShape(xSaved, ySaved).deselect();
						resetSavedTile();
						 
				} else if ( !touching(xSaved, ySaved, dX, dY) ) { 	// runs if touch is not adjacent to selected tile.
						logic.getShape(xSaved, ySaved).deselect();
						setSavedTile(dX, dY);
						
				} else {                                            // runs if none of the above conditions are true.
						logic.getShape(dX, dY).select();
						logic.setSwapCandidates(xSaved, ySaved, dX, dY);
						resetSavedTile();
				}
			}
			return true;
		}
		@Override
		public boolean touchDragged(int x, int y, int pointer) {
			if ( logic.isReadyForNewCandidates() ) {
				touch.set(x, y, 0);
				Core.camera.unproject(touch);
				int tX = (int) touch.x/4;
				int tY = (int) touch.y/4;
				int dragUP = MathUtils.clamp( (tY-ySaved), -1, 1);
				int dragRIGHT = MathUtils.clamp( (tX-xSaved), -1, 1);
				float diagTest = DIAG.dot(dragRIGHT, dragUP);
				if ( 	   !(tX == xSaved && tY == ySaved) 
						&& !(xSaved == -10 && ySaved == -10)
						&& !outOfBounds(tX, tY) 
						&& diagTest*diagTest == 1 
						&& touching(xSaved, ySaved, tX, tY) ) {
					logic.setSwapCandidates(xSaved, ySaved, tX, tY);
					resetSavedTile();
					return true;
				}
			}
			return false;
		}
		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			if ( logic.isReadyForNewCandidates() ) {
				
				touch.set(x, y, 0);
				Core.camera.unproject(touch);
				int uX = (int) touch.x/4;
				int uY = (int) touch.y/4;
															
				if ( ( uX == xSaved && uY == ySaved )  		// - if touch released where started
					|| ( xSaved == -10 && ySaved == -10 )  	// - if it is still the end of the first touch.
					|| ( outOfBounds(uX, uY) ) ) {          // - if touch released out of bounds
															// Do nothing...
				} else if ( !touching(xSaved, ySaved, uX, uY) ) {    // end event if touch released too far from first tile.
						logic.getShape(xSaved, ySaved).deselect();
						resetSavedTile();
				} else {
						logic.getShape(uX, uY).select();
						logic.setSwapCandidates(xSaved, ySaved, uX, uY);
						resetSavedTile();
				}
			}
			return true;
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
			xSaved = -10;
			ySaved = -10;
		}
		private void setSavedTile(int x, int y) {
			logic.getShape(x, y).select();
			xSaved = x;
			ySaved = y;				
		}
}
