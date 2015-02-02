package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

class CrossOne extends Shape {
	public CrossOne() {
		tile = new Sprite(crossone);
		color = new Color(0f, 1f, 0f, 1f);
	}
	@Override
	boolean checkMatch(int x, int y, GameBoard p) {
		// checks for a match when swapped.
		board = p;
		boolean matchmade = false;
// these lines check for a full upright cross (possibly too hard to achieve?)
		if ( m4(x, y-1, x, y+1, x-1, y, x+1, y) ) matchmade = true;
		if ( m4(x, y+1, x, y+2, x-1, y+1, x+1, y+1) ) matchmade = true;
		if ( m4(x, y-1, x, y-2, x-1, y-1, x+1, y-1) ) matchmade = true;
		if ( m4(x+1, y, x+2, y, x+1, y-1, x+1, y+1) ) matchmade = true;
		if ( m4(x-1, y, x-2, y, x-1, y-1, x-1, y+1) ) matchmade = true;
// the following lines check for an unfilled cross (a rhomboid diamond)
		if ( m3(x-1, y+1, x+1, y+1, x, y+2) ) matchmade = true;
		if ( m3(x-1, y-1, x+1, y-1, x, y-2) ) matchmade = true;
		if ( m3(x+1, y+1, x+1, y-1, x+2, y) ) matchmade = true;
		if ( m3(x-1, y+1, x-1, y-1, x-2, y) ) matchmade = true;
		return matchmade; 
	}		
			
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}

