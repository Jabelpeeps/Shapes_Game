package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;

public class CrossOne extends Shape {
	
	public CrossOne() {
		this.setRegion(Master.crossone);
		type = "crossone";
	}
	
	@Override
	public void select() {
		this.setColor(0f, 1f, 0f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
	@Override
	public boolean checkMatch() {
		boolean matchmade = false;
		int x = (int) getX();
		int y = (int) getY();
		
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

