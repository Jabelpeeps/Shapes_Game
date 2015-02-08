package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;

public class CrossOne extends Shape {
	// crossone is the '+' shape.
	
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
	protected float shapeMatch(int x, int y) {
		
		float matchesmade = 0f;
		// check for a full upright cross 
		if ( m4(x, y-1, x, y+1, x-1, y, x+1, y, this) ) { matchesmade += 0.2f; }
		if ( m4(x, y+1, x, y+2, x-1, y+1, x+1, y+1, this) ) { matchesmade += 0.2f; }
		if ( m4(x, y-1, x, y-2, x-1, y-1, x+1, y-1, this) ) { matchesmade += 0.2f; }
		if ( m4(x+1, y, x+2, y, x+1, y-1, x+1, y+1, this) ) { matchesmade += 0.2f; }
		if ( m4(x-1, y, x-2, y, x-1, y-1, x-1, y+1, this) ) { matchesmade += 0.2f; }
		matchesmade += unfilledCross(x, y);

		return matchesmade; 
	}
	private float unfilledCross(int x, int y) {
		float ufc = 0f;
		if ( x >= 0 && y >=0 && x <= logic.getX() && y <= logic.getY() ) {
			// check for an unfilled cross (a rhomboid diamond)
			if ( m3(x-1, y+1, x+1, y+1, x, y+2, this) ) { ufc += 0.25f; }
			if ( m3(x-1, y-1, x+1, y-1, x, y-2, this) ) { ufc += 0.25f; }
			if ( m3(x+1, y+1, x+1, y-1, x+2, y, this) ) { ufc += 0.25f; }
			if ( m3(x-1, y+1, x-1, y-1, x-2, y, this) ) { ufc += 0.25f; }
		}
		return ufc;
	}
	@Override
	protected boolean hintMatch(int x, int y) {
		boolean hintFound = false;
		// check for the full cross (x +/- 1)
		if ( m4(x, y+1, x+1, y+1, x+1, y+2, x+2, y+1, this) ) hintFound = true;
		if ( m4(x+2, y+1, x+2, y, x+2, y-1, x+3, y, this) ) hintFound = true;
		if ( m4(x, y-1, x+1, y-1, x+1, y-2, x+2, y-1, this) ) hintFound = true;
		if ( m4(x, y-1, x-1, y-1, x-1, y-2, x-2, y-1, this) ) hintFound = true;
		if ( m4(x-2, y-1, x-2, y, x-3, y, x-2, y+1, this) ) hintFound = true;
		if ( m4(x, y+1, x-1, y+1, x-2, y+1, x-1, y+2, this) ) hintFound = true;
		// full cross (y +/- 1)
		if ( m4(x+1, y, x+1, y-1, x+1, y-2, x+2, y-1, this) ) hintFound = true;
		if ( m4(x+1, y-2, x, y-2, x, y-3, x-1, y-2, this) ) hintFound = true;
		if ( m4(x-1, y, x-1, y-1, x-1, y-2, x-2, y-1, this) ) hintFound = true;
		if ( m4(x-1, y, x-1, y+1, x-1, y+2, x-2, y+1, this) ) hintFound = true;
		if ( m4(x-1, y+2, x, y+2, x+1, y+2, x, y+3, this) ) hintFound = true;
		if ( m4(x+1, y, x+1, y+1, x+1, y+2, x+2, y+1, this) ) hintFound = true;	
		if ( unfilledCross(x+1, y) > 0f ) hintFound = true;
		if ( unfilledCross(x-1, y) > 0f ) hintFound = true;
		if ( unfilledCross(x, y+1) > 0f ) hintFound = true;
		if ( unfilledCross(x, y-1) > 0f ) hintFound = true;
		
		return hintFound;
	}
}

