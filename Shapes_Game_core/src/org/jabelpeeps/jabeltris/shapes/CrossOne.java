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
	protected float shapeMatch(int x, int y, int xx, int yy) {
		
		float matchesmade = 0f;
		// check for a full upright cross
		if ( (yy != y-1) && (yy != y+1) && (xx != x+1) && (xx != x-1) 
				&& m5(xx, yy, x, y-1, x, y+1, x-1, y, x+1, y, this) ) { matchesmade += 0.2f; }
		if ( (yy != y+1) && m5(xx, yy, x, y+1, x, y+2, x-1, y+1, x+1, y+1, this) ) { matchesmade += 0.2f; }
		if ( (yy != y-1) && m5(xx, yy, x, y-1, x, y-2, x-1, y-1, x+1, y-1, this) ) { matchesmade += 0.2f; }
		if ( (xx != x+1) && m5(xx, yy, x+1, y, x+2, y, x+1, y-1, x+1, y+1, this) ) { matchesmade += 0.2f; }
		if ( (xx != x-1) && m5(xx, yy, x-1, y, x-2, y, x-1, y-1, x-1, y+1, this) ) { matchesmade += 0.2f; }
		// check for an unfilled cross (a rhomboid diamond)
		if ( m4(xx, yy, x-1, y+1, x+1, y+1, x, y+2, this) ) { matchesmade += 0.25f; }
		if ( m4(xx, yy, x-1, y-1, x+1, y-1, x, y-2, this) ) { matchesmade += 0.25f; }
		if ( m4(xx, yy, x+1, y+1, x+1, y-1, x+2, y, this) ) { matchesmade += 0.25f; }
		if ( m4(xx, yy, x-1, y+1, x-1, y-1, x-2, y, this) ) { matchesmade += 0.25f; }
		
		return matchesmade; 
	}
}

