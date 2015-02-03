package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;

class Triangle extends Shape {
	
	public Triangle() {
		this.setRegion(Master.triangle);
		color = new Color(1f, 0f, 1f, 1f);
		type = "triangle";
	}		
	
	@Override
	public boolean checkMatch(int x, int y) {
		// checks for matches when swapped.
		
		boolean matchmade = false;
		
		// calling block at centre (4 rotations, order: up, down, right, left)
		if ( m3(x-1, y, x+1, y, x, y+1, this) ) matchmade = true;
		if ( m3(x-1, y, x+1, y, x, y-1, this) ) matchmade = true;
		if ( m3(x+1, y, x, y-1, x, y+1, this) ) matchmade = true;
		if ( m3(x-1, y, x, y-1, x, y+1, this) ) matchmade = true;
		// calling block on left arm
		if ( m3(x+1, y, x+2, y, x+1, y+1, this) ) matchmade = true;
		if ( m3(x-1, y, x-2, y, x-1, y-1, this) ) matchmade = true;
		if ( m3(x, y-1, x, y-2, x+1, y-1, this) ) matchmade = true;
		if ( m3(x, y+1, x, y+2, x-1, y+1, this) ) matchmade = true;
		// calling block on right arm
		if ( m3(x-1, y, x-2, y, x-1, y+1, this) ) matchmade = true;
		if ( m3(x+1, y, x+2, y, x+1, y-1, this) ) matchmade = true;
		if ( m3(x, y+1, x, y+2, x+1, y+1, this) ) matchmade = true;
		if ( m3(x, y-1, x, y-2, x-1, y-1, this) ) matchmade = true;
		// calling block on centre arm
		if ( m3(x-1, y-1, x+1, y-1, x, y-1, this) ) matchmade = true;
		if ( m3(x-1, y+1, x+1, y+1, x, y+1, this) ) matchmade = true;
		if ( m3(x-1, y+1, x-1, y-1, x-1, y, this) ) matchmade = true;
		if ( m3(x+1, y+1, x+1, y-1, x+1, y, this) ) matchmade = true;
		
		return matchmade; 
	}	

	@Override				
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
