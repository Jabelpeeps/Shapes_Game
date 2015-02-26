package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class SquareAbstract extends Shape {
	
	public SquareAbstract() {
		setRegion(LevelMaster.greysqr);
		type = "Square";
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
