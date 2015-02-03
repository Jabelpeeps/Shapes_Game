package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;

class CrossTwo extends Shape {
	
	public CrossTwo() {
		this.setRegion(Master.crosstwo);
		color = new Color(0f, 0f, 1f, 1f);
		type = "crosstwo";
	}
	
	@Override
	boolean checkMatch(int x, int y) {
		// checks for matches when swapped.
		
		boolean matchmade = false;
		
		// check for a full "X" cross
		if ( m4(x-1, y-1, x+1, y+1, x-1, y+1, x+1, y-1, this) ) matchmade = true;
		if ( m4(x+1, y+1, x+2, y+2, x, y+2, x+2, y, this) ) matchmade = true;
		if ( m4(x-1, y-1, x-2, y-2, x, y-2, x-2, y, this) ) matchmade = true;
		if ( m4(x+1, y-1, x+2, y-2, x, y-2, x+2, y, this) ) matchmade = true;
		if ( m4(x-1, y+1, x-2, y+2, x, y+2, x-2, y, this) ) matchmade = true;

		// check for the corners of an "X" only 
		if ( m3(x+2, y, x, y-2, x+2, y-2, this) ) matchmade = true;
		if ( m3(x-2, y, x, y+2, x-2, y+2, this) ) matchmade = true;
		if ( m3(x+2, y, x, y+2, x+2, y+2, this) ) matchmade = true;
		if ( m3(x-2, y, x, y-2, x-2, y-2, this) ) matchmade = true;
		
		return matchmade; 
	}		

	@Override			
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
