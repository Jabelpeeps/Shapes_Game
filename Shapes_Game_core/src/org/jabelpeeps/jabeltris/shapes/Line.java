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
