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
	public float shapeMatch(int x, int y, int xx, int yy) {		
		
		float matchesmade = 0f;
		// calling block at centre (4 rotations, order: up, down, right, left)
		if ( (yy != y+1) && (xx != x+1) && (xx != x-1) && m(this, v(x-1, y), v(x+1, y), v(x, y+1)) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && (xx != x+1) && (xx != x-1) && m(this, v(x-1, y), v(x+1, y), v(x, y-1)) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && (yy != y+1) && (xx != x+1) && m(this, v(x+1, y), v(x, y-1), v(x, y+1)) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && (yy != y+1) && (xx != x-1) && m(this, v(x-1, y), v(x, y-1), v(x, y+1)) ) { matchesmade += 0.25f; }
		// calling block on left arm
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+2, y), v(x+1, y+1)) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-2, y), v(x-1, y-1)) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x, y-2), v(x+1, y-1)) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x, y+2), v(x-1, y+1)) ) { matchesmade += 0.25f; }
		// calling block on right arm
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-2, y), v(x-1, y+1)) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+2, y), v(x+1, y-1)) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x, y+2), v(x+1, y+1)) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x, y-2), v(x-1, y-1)) ) { matchesmade += 0.25f; }
		// calling block on centre arm
		if ( (yy != y-1) && m(this, v(x-1, y-1), v(x+1, y-1), v(x, y-1)) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && m(this, v(x-1, y+1), v(x+1, y+1), v(x, y+1)) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && m(this, v(x-1, y+1), v(x-1, y-1), v(x-1, y)) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && m(this, v(x+1, y+1), v(x+1, y-1), v(x+1, y)) ) { matchesmade += 0.25f; }

		return matchesmade; 
	}
}
