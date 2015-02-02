package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

class Square extends Shape {
	public Square() {
		tile = new Sprite(square);
		color = new Color(0f, 1f, 1f, 1f);
	}				
	@Override
	public boolean checkMatch(int x, int y, GameBoard p) {
		// checks for a match when swapped.
		board = p;
		boolean matchmade = false;
		if ( m3(x-1, y, x, y-1, x-1, y-1) ) matchmade = true;
		if ( m3(x+1, y, x, y+1, x+1, y+1) ) matchmade = true;
		if ( m3(x-1, y, x, y+1, x-1, y+1) ) matchmade = true;
		if ( m3(x+1, y, x, y-1, x+1, y-1) ) matchmade = true;
		return matchmade; 
	}	
			
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
