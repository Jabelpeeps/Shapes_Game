package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

class CrossTwo extends Shape {
	public CrossTwo() {
		tile = new Sprite(crosstwo);
		color = new Color(0f, 0f, 1f, 1f);
	}
	@Override
	boolean checkMatch(int x, int y, GameBoard p) {
		// checks for a match when swapped.
		board = p;
		boolean matchmade = false;
		if ( m4(x-1, y-1, x+1, y+1, x-1, y+1, x+1, y-1) ) matchmade = true;
		if ( m4(x+1, y+1, x+2, y+2, x, y+2, x+2, y) ) matchmade = true;
		if ( m4(x-1, y-1, x-2, y-2, x, y-2, x-2, y) ) matchmade = true;
		if ( m4(x+1, y-1, x+2, y-2, x, y-2, x+2, y) ) matchmade = true;
		if ( m4(x-1, y+1, x-2, y+2, x, y+2, x-2, y) ) matchmade = true;
		return matchmade; 
	}		
				
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
