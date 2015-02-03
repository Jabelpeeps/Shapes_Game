package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;


class Line extends Shape {
	
	public Line() {
		this.setRegion(Master.line);
		color = new Color(1f, 0f, 0f, 1f);
		type = "line";
	}
	
	@Override
	public boolean checkMatch(int x, int y) {
		// checks for matches when swapped.
		
		boolean matchmade = false;
		
		if ( m2(x-1, y, x+1, y, this) ) matchmade = true;
		if ( m2(x-1, y, x-2, y, this) ) matchmade = true;
		if ( m2(x+1, y, x+2, y, this) ) matchmade = true;
		if ( m2(x, y-1, x, y+1, this) ) matchmade = true;
		if ( m2(x, y-1, x, y-2, this) ) matchmade = true;
		if ( m2(x, y+1, x, y+2, this) ) matchmade = true;
		
		return matchmade;
	}			

	@Override
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
