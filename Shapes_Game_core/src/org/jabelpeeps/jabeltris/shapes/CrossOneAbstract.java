package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Shape;

public abstract class CrossOneAbstract extends Shape {
	// crossone is the '+' shape.
	
	@Override
	protected float shapeMatch(int x, int y, int xx, int yy) {
		int xs = logic.getXsize()-1;
		int ys = logic.getYsize()-1;
		float matchesmade = 0f;
		// check for a full upright cross
		if ( (yy != y-1) && (yy != y+1) && (xx != x+1) && (xx != x-1) 
						 && m(this, v(x, y-1), v(x, y+1), v(x-1, y), v(x+1, y)) ) { matchesmade += 0.2f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x, y+2), v(x-1, y+1), v(x+1, y+1)) ) { matchesmade += 0.2f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x, y-2), v(x-1, y-1), v(x+1, y-1)) ) { matchesmade += 0.2f; }
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+2, y), v(x+1, y-1), v(x+1, y+1)) ) { matchesmade += 0.2f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-2, y), v(x-1, y-1), v(x-1, y+1)) ) { matchesmade += 0.2f; }
		// check for an unfilled cross (a rhomboid diamond)
		if ( !(yy == y+1 && yy == 0) && m(this, v(x-1, y+1), v(x+1, y+1), v(x, y+2)) ) { matchesmade += 0.25f; }
		if ( !(yy == y-1 && yy == ys) && m(this, v(x-1, y-1), v(x+1, y-1), v(x, y-2)) ) { matchesmade += 0.25f; }
		if ( !(xx == x+1 && xx == 0) && m(this, v(x+1, y+1), v(x+1, y-1), v(x+2, y)) ) { matchesmade += 0.25f; }
		if ( !(xx == x-1 && xx == xs) && m(this, v(x-1, y+1), v(x-1, y-1), v(x-2, y)) ) { matchesmade += 0.25f; }
		
		return matchesmade; 
	}
}

