package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;

class Square extends Shape {
	
	public Square() {
		this.setRegion(Master.square);
		color = new Color(0f, 1f, 1f, 1f);
		type = "square";
	}			
	
	@Override
	public boolean checkMatch(int x, int y) {
		// checks for matches when swapped.
		
		boolean matchmade = false;
		
		if ( m3(x-1, y, x, y-1, x-1, y-1, this) ) matchmade = true;
		if ( m3(x+1, y, x, y+1, x+1, y+1, this) ) matchmade = true;
		if ( m3(x-1, y, x, y+1, x-1, y+1, this) ) matchmade = true;
		if ( m3(x+1, y, x, y-1, x+1, y-1, this) ) matchmade = true;
		
		return matchmade; 
	}	

	@Override		
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
