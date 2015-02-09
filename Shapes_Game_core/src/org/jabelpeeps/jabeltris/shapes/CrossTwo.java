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
	public float shapeMatch(int x, int y, int xx, int yy) {
		
		float matchesmade = 0f;
		// check for a full "X" cross
		if ( m(this, v(x-1, y-1), v(x+1, y+1), v(x-1, y+1), v(x+1, y-1)) ) { matchesmade += 0.2f; }
		if ( m(this, v(x+1, y+1), v(x+2, y+2), v(x, y+2), v(x+2, y)) ) { matchesmade += 0.2f; }
		if ( m(this, v(x-1, y-1), v(x-2, y-2), v(x, y-2), v(x-2, y)) ) { matchesmade += 0.2f; }
		if ( m(this, v(x+1, y-1), v(x+2, y-2), v(x, y-2), v(x+2, y)) ) { matchesmade += 0.2f; }
		if ( m(this, v(x-1, y+1), v(x-2, y+2), v(x, y+2), v(x-2, y)) ) { matchesmade += 0.2f; }
		// check for the corners of an "X" only 
		if ( m(this, v(x+2, y), v(x, y-2), v(x+2, y-2)) ) { matchesmade += 0.25f; }
		if ( m(this, v(x-2, y), v(x, y+2), v(x-2, y+2)) ) { matchesmade += 0.25f; }
		if ( m(this, v(x+2, y), v(x, y+2), v(x+2, y+2)) ) { matchesmade += 0.25f; }
		if ( m(this, v(x-2, y), v(x, y-2), v(x-2, y-2)) ) { matchesmade += 0.25f; }

		return matchesmade; 
	}
}
