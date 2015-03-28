package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class RealEllAbstract extends Shape {

	public RealEllAbstract() {
			setRegion(LevelMaster.realell);
			type = "RealEll";
			animate();
	}

	@Override
	protected float shapeMatch(int x, int y, int xx, int yy) {
		
		float matchesmade = 0f;
		// four rotations needed
		// calling block on short arm
		if ( (yy != y-1) && m(this, v(x, y-1), v(x-1, y-1), v(x-2, y-1) ) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x+1, y+1), v(x+2, y+1) ) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-1, y+1), v(x-1, y+2) ) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+1, y-1), v(x+1, y-2) ) ) { matchesmade += 0.25f; }
		// calling block at corner
		if ( (xx != x-1) && (yy != y+1) && m(this, v(x, y+1), v(x-1, y), v(x-2, y) ) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && (yy != y-1) && m(this, v(x, y-1), v(x+1, y), v(x+2, y) ) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && (yy != y+1) && m(this, v(x+1, y), v(x, y+1), v(x, y+2) ) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && (yy != y-1) && m(this, v(x-1, y), v(x, y-1), v(x, y-2) ) ) { matchesmade += 0.25f; }
		// calling block at end of long arm
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+2, y), v(x+2, y+1) ) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-2, y), v(x-2, y-1) ) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x, y-2), v(x+1, y-2) ) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x, y+2), v(x-1, y+2) ) ) { matchesmade += 0.25f; }
		// calling block in middle of long arm
		if ( (xx != x+1) && (xx != x-1) && m(this, v(x-1, y), v(x+1, y), v(x+1, y+1) ) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && (xx != x-1) && m(this, v(x-1, y), v(x+1, y), v(x-1, y-1) ) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && (yy != y+1) && m(this, v(x, y+1), v(x, y-1), v(x+1, y-1) ) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && (yy != y+1) && m(this, v(x, y+1), v(x, y-1), v(x-1, y+1) ) ) { matchesmade += 0.25f; }
		
		return matchesmade;
	}
}
