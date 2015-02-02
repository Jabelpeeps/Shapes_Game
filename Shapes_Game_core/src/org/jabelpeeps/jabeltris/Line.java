package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;


class Line extends Shape {
	
	public Line() {
		tile = new Sprite(line);
		color = new Color(1f, 0f, 0f, 1f);
	}
	@Override
	public boolean checkMatch(int x, int y, GameBoard p) {
		// checks for a match when swapped.
		board = p;
		boolean matchmade = false;
		if ( m2(x-1, y, x+1, y) ) matchmade = true;
		if ( m2(x-1, y, x-2, y) ) matchmade = true;
		if ( m2(x+1, y, x+2, y) ) matchmade = true;
		if ( m2(x, y-1, x, y+1) ) matchmade = true;
		if ( m2(x, y-1, x, y-2) ) matchmade = true;
		if ( m2(x, y+1, x, y+2) ) matchmade = true;
		return matchmade;
	}			
	
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
