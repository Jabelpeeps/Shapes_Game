package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;

class CrossOne extends Shape {
	
	public CrossOne() {
		this.setRegion(Master.crossone);
		color = new Color(0f, 1f, 0f, 1f);
		type = "crossone";
	}
	
	@Override
	boolean checkMatch(int x, int y) {
		// checks for matches when swapped.
		
		boolean matchmade = false;
		
		// check for a full upright cross (possibly too hard to achieve?)
		if ( m4(x, y-1, x, y+1, x-1, y, x+1, y, this) ) matchmade = true;
		if ( m4(x, y+1, x, y+2, x-1, y+1, x+1, y+1, this) ) matchmade = true;
		if ( m4(x, y-1, x, y-2, x-1, y-1, x+1, y-1, this) ) matchmade = true;
		if ( m4(x+1, y, x+2, y, x+1, y-1, x+1, y+1, this) ) matchmade = true;
		if ( m4(x-1, y, x-2, y, x-1, y-1, x-1, y+1, this) ) matchmade = true;
		
		// check for an unfilled cross (a rhomboid diamond)
		if ( m3(x-1, y+1, x+1, y+1, x, y+2, this) ) matchmade = true;
		if ( m3(x-1, y-1, x+1, y-1, x, y-2, this) ) matchmade = true;
		if ( m3(x+1, y+1, x+1, y-1, x+2, y, this) ) matchmade = true;
		if ( m3(x-1, y+1, x-1, y-1, x-2, y, this) ) matchmade = true;
		
		return matchmade; 
	}		

	@Override		
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}

