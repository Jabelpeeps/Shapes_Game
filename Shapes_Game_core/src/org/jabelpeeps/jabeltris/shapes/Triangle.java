package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;

public class Triangle extends Shape {
	// really a short 'T' shape, but triangle sounded better.
	
	public Triangle() {
		this.setRegion(Master.triangle);
		type = "triangle";
	}		

	@Override
	public void select() {
		this.setColor(1f, 0f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
	@Override
	public boolean shapeMatch(int x, int y) {		
		
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
	protected boolean hintMatch(int x, int y) {
		
		boolean hintFound = false;
		// moving block to be beside the centre of a line of three
		if ( m3(x+2, y+1, x+2, y, x+2, y-1, this) ) hintFound = true;
		if ( m3(x-2, y+1, x-2, y, x-2, y-1, this) ) hintFound = true;
		if ( m3(x+1, y+2, x, y+2, x-1, y+2, this) ) hintFound = true;
		if ( m3(x+1, y-2, x, y-2, x-1, y-2, this) ) hintFound = true;
		// following checks are grouped depending on a matching diagonal shape.
		if ( m1(x+1, y+1, this) ) {
			if ( m2(x, y+1, x+1, y+2, this) ) hintFound = true;
			if ( m2(x, y+1, x+2, y+1, this) ) hintFound = true;
			if ( m2(x+1, y+2, x+2, y+1, this) ) hintFound = true;
			if ( m2(x+1, y+2, x+1, y, this) ) hintFound = true;
			if ( m2(x+2, y+1, x+1, y, this) ) hintFound = true;
			if ( m2(x-1, y+1, x, y+2, this) ) hintFound = true;
		}
		if ( m1(x-1, y+1, this) ) {
			if ( m2(x-1, y+2, x, y+1, this) ) hintFound = true;
			if ( m2(x-2, y+1, x, y+1, this) ) hintFound = true;
			if ( m2(x-2, y+1, x-1, y+2, this) ) hintFound = true;
			if ( m2(x-1, y, x-1, y+2, this) ) hintFound = true;
			if ( m2(x-1, y, x-2, y+1, this) ) hintFound = true;
			if ( m2(x-1, y-1, x-2, y, this) ) hintFound = true;
		}
		if ( m1(x-1, y-1, this) ) {
			if ( m2(x-2, y-1, x-1, y-2, this) ) hintFound = true;
			if ( m2(x-2, y-1, x, y-1, this) ) hintFound = true;
			if ( m2(x-1, y-2, x, y-1, this) ) hintFound = true;
			if ( m2(x-2, y-1, x-1, y, this) ) hintFound = true;
			if ( m2(x-1, y, x-1, y-2, this) ) hintFound = true;
			if ( m2(x+1, y-1, x, y-2, this) ) hintFound = true;
		}
		if ( m1(x+1, y-1, this) ) {
			if ( m2(x+2, y-1, x+1, y-2, this) ) hintFound = true;
			if ( m2(x+2, y-1, x, y-1, this) ) hintFound = true;
			if ( m2(x+1, y-2, x, y-1, this) ) hintFound = true;
			if ( m2(x+1, y, x+2, y-1, this) ) hintFound = true;
			if ( m2(x+1, y, x+1, y-2, this) ) hintFound = true;
			if ( m2(x+1, y+1, x+2, y, this) ) hintFound = true;
		}
		
		return hintFound;
	}
}
