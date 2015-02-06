package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;

public class CrossTwo extends Shape {
	// crosstwo is the 'X' shape
	
	public CrossTwo() {
		this.setRegion(Master.crosstwo);
		type = "crosstwo";
	}

	@Override
	public void select() {
		this.setColor(0f, 0f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
	@Override
	public boolean shapeMatch(int x, int y) {
		
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
	protected boolean hintMatch(int x, int y) {
		
		boolean hintFound = false;
		if ( shapeMatch(x, y+1) ) hintFound = true;
		if ( shapeMatch(x, y-1) ) hintFound = true;
		if ( shapeMatch(x+1, y) ) hintFound = true;
		if ( shapeMatch(x-1, y) ) hintFound = true;
		
		//return false;
		return hintFound;
	}
}
