package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;

public class Square extends Shape {
	
	public Square() {
		this.setRegion(Master.square);
		type = "square";
	}			

	@Override
	public void select() {
		this.setColor(0f, 1f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
	@Override
	public boolean shapeMatch(int x, int y) {		
		
		boolean matchmade = false;
		if ( m3(x-1, y, x, y-1, x-1, y-1, this) ) matchmade = true;
		if ( m3(x+1, y, x, y+1, x+1, y+1, this) ) matchmade = true;
		if ( m3(x-1, y, x, y+1, x-1, y+1, this) ) matchmade = true;
		if ( m3(x+1, y, x, y-1, x+1, y-1, this) ) matchmade = true;
		
		return matchmade; 
	}
	@Override
	protected boolean hintMatch(int x, int y) {

		boolean hintFound = false;
		// for position (x+1 , y)
		if ( m3(x+1, y+1, x+2, y+1, x+2, y, this) ) hintFound = true;
		if ( m3(x+1, y-1, x+2, y-1, x+2, y, this) ) hintFound = true;
		// for position (x-1 , y)
		if ( m3(x-2, y, x-1, y+1, x-2, y+1, this) ) hintFound = true;
		if ( m3(x-2, y, x-2, y-1, x-1, y-1, this) ) hintFound = true;
		// for position (x , y+1)
		if ( m3(x-1, y+1, x-1, y+2, x, y+2, this) ) hintFound = true;
		if ( m3(x+1, y+1, x+1, y+2, x, y+2, this) ) hintFound = true;
		// for position (x , y-1)
		if ( m3(x-1, y-1, x-1, y-2, x, y-2, this) ) hintFound = true;
		if ( m3(x+1, y-1, x+1, y-2, x, y-2, this) ) hintFound = true;
		
		//return false;
		return hintFound; 
	}
}
