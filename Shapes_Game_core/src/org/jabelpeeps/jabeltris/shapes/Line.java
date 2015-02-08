package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;


public class Line extends Shape {
	
	public Line() {
		this.setRegion(Master.line);
		type = "line";
	}

	@Override
	public void select() {
		this.setColor(1f, 0f, 0f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
	@Override
	public float shapeMatch(int x, int y) {		
		
		float matchesmade = 0f;
		// horizontal lines
		if ( m2(x-1, y, x+1, y, this) ) { matchesmade += 1f/3f; }
		if ( m2(x-1, y, x-2, y, this) ) { matchesmade += 1f/3f; }
		if ( m2(x+1, y, x+2, y, this) ) { matchesmade += 1f/3f; }
		// vertical lines
		if ( m2(x, y-1, x, y+1, this) ) { matchesmade += 1f/3f; }
		if ( m2(x, y-1, x, y-2, this) ) { matchesmade += 1f/3f; }
		if ( m2(x, y+1, x, y+2, this) ) { matchesmade += 1f/3f; }

		return matchesmade;
	}
	@Override
	protected boolean hintMatch(int x, int y) {
		
		boolean hintFound = false;
		// horizontal lines y+1
		if ( m2(x-1, y+1, x+1, y+1, this) ) hintFound = true;
		if ( m2(x-1, y+1, x-2, y+1, this) ) hintFound = true;
		if ( m2(x+1, y+1, x+2, y+1, this) ) hintFound = true;
		// horizontal lines y-1
		if ( m2(x-1, y-1, x+1, y-1, this) ) hintFound = true;
		if ( m2(x-1, y-1, x-2, y-1, this) ) hintFound = true;
		if ( m2(x+1, y-1, x+2, y-1, this) ) hintFound = true;
		// horizontal lines x +/- 1
		if ( m2(x+2, y, x+3, y, this) ) hintFound = true;
		if ( m2(x-2, y, x-3, y, this) ) hintFound = true;		
		// vertical lines x+1
		if ( m2(x+1, y-1, x+1, y+1, this) ) hintFound = true;
		if ( m2(x+1, y-1, x+1, y-2, this) ) hintFound = true;
		if ( m2(x+1, y+1, x+1, y+2, this) ) hintFound = true;
		// vertical lines x-1
		if ( m2(x-1, y-1, x-1, y+1, this) ) hintFound = true;
		if ( m2(x-1, y-1, x-1, y-2, this) ) hintFound = true;
		if ( m2(x-1, y+1, x-1, y+2, this) ) hintFound = true;
		// vertical lines y +/- 1
		if ( m2(x, y-2, x, y-3, this) ) hintFound = true;
		if ( m2(x, y+2, x, y+3, this) ) hintFound = true;
		
		return hintFound;
	}
}
