package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

class Triangle extends Shape {
	public Triangle() {
		tile = new Sprite(triangle);
		color = new Color(1f, 0f, 1f, 1f);
	}			
	@Override
	public boolean checkMatch(int x, int y, GameBoard p) {
		// checks for a match when swapped.
		board = p;
		boolean matchmade = false;
		// calling block at centre (4 rotations, order: up, down, right, left)
		if ( m3(x-1, y, x+1, y, x, y+1) ) matchmade = true;
		if ( m3(x-1, y, x+1, y, x, y-1) ) matchmade = true;
		if ( m3(x+1, y, x, y-1, x, y+1) ) matchmade = true;
		if ( m3(x-1, y, x, y-1, x, y+1) ) matchmade = true;
		// calling block on left arm
		if ( m3(x+1, y, x+2, y, x+1, y+1) ) matchmade = true;
		if ( m3(x-1, y, x-2, y, x-1, y-1) ) matchmade = true;
		if ( m3(x, y-1, x, y-2, x+1, y-1) ) matchmade = true;
		if ( m3(x, y+1, x, y+2, x-1, y+1) ) matchmade = true;
		// calling block on right arm
		if ( m3(x-1, y, x-2, y, x-1, y+1) ) matchmade = true;
		if ( m3(x+1, y, x+2, y, x+1, y-1) ) matchmade = true;
		if ( m3(x, y+1, x, y+2, x+1, y+1) ) matchmade = true;
		if ( m3(x, y-1, x, y-2, x-1, y-1) ) matchmade = true;
		// calling block on centre arm
		if ( m3(x-1, y-1, x+1, y-1, x, y-1) ) matchmade = true;
		if ( m3(x-1, y+1, x+1, y+1, x, y+1) ) matchmade = true;
		if ( m3(x-1, y+1, x-1, y-1, x-1, y) ) matchmade = true;
		if ( m3(x+1, y+1, x+1, y-1, x+1, y) ) matchmade = true;
		return matchmade; 
	}	
					
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
