package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;

public class Square extends Shape {
	
	public Square() {
		this.setRegion(Master.square);
		type = "square";
	}			

	@Override
	public void select() {
		this.setColor(0f, 1f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
	@Override
	public float shapeMatch(int x, int y, int xx, int yy) {	
		float matchesmade = 0f;
		if ( (yy != y-1) && (xx != x-1) && m(this, v(x-1, y), v(x, y-1), v(x-1, y-1)) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && (xx != x+1) && m(this, v(x+1, y), v(x, y+1), v(x+1, y+1)) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && (xx != x-1) && m(this, v(x-1, y), v(x, y+1), v(x-1, y+1)) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && (xx != x+1) && m(this, v(x+1, y), v(x, y-1), v(x+1, y-1)) ) { matchesmade += 0.25f; }
		return matchesmade; 
	}	
}
