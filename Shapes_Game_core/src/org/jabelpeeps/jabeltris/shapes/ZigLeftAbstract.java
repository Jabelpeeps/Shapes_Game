package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class ZigLeftAbstract extends Shape {

	public ZigLeftAbstract() {
		setRegion(LevelMaster.zigleft);
		type = "ZigLeft";
		animate();
	}
	@Override
	protected float shapeMatch(int x, int y, int xx, int yy) {
		float matchesmade = 0f;
		
		// needs two rotations
		// vertical 
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+1, y+1), v(x+2, y+1) ) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && (yy != y+1) && m(this, v(x-1, y), v(x, y+1), v(x+1, y+1) ) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && (yy != y-1) && m(this, v(x-1, y-1), v(x, y-1), v(x+1, y) ) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-1, y-1), v(x-2, y-1) ) ) { matchesmade += 0.25f; }
		
		//horizontal
		if ( (yy != y+1) && m(this, v(x, y+1), v(x-1, y+1), v(x-1, y+2) ) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && (xx != x-1) && m(this, v(x, y-1), v(x-1, y), v(x-1, y+1) ) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && (xx != x+1) && m(this, v(x, y+1), v(x+1, y), v(x+1, y-1) ) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x+1, y-1), v(x+1, y-2) ) ) { matchesmade += 0.25f; }

		return matchesmade;
	}
}