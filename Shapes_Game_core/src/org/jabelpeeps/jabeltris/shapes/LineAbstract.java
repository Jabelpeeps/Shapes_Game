package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Shape;

public abstract class LineAbstract extends Shape  {
	
	public LineAbstract() {
		needsFlipping = true;
		animate();
	}
	@Override
	protected float shapeMatch(int x, int y, int xx, int yy) {
		
		float matchesmade = 0f;
		// horizontal lines
		if ( (xx != x+1) && (xx != x-1)	&& m(this, v(x-1, y), v(x+1, y)) ) { matchesmade += 1f/3f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-2, y)) ) { matchesmade += 1f/3f; }
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+2, y)) ) { matchesmade += 1f/3f; }
		// vertical lines
		if ( (yy != y-1) && (yy != y+1) && m(this, v(x, y-1), v(x, y+1)) ) { matchesmade += 1f/3f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x, y-2)) ) { matchesmade += 1f/3f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x, y+2)) ) { matchesmade += 1f/3f; }

		return matchesmade;
	}
}
