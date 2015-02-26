package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class FourBarAbstract extends Shape {

	public FourBarAbstract() {
			setRegion(LevelMaster.fourbar);
			type = "FourBar";
	}

	@Override
	protected float shapeMatch(int x, int y, int xx, int yy) {
		
		float matchesmade = 0f;
		// horizontal lines
		if ( (xx != x+1) && (xx != x-1)	&&
					( m(this, v(x-1, y), v(x+1, y), v(x+2, y)) 
				   || m(this, v(x-1, y), v(x+1, y), v(x-2, y)) ) ) { matchesmade += 0.25f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-2, y), v(x-3, y)) ) { matchesmade += 0.25f; }
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+2, y), v(x+3, y)) ) { matchesmade += 0.25f; }
		// vertical lines
		if ( (yy != y-1) && (yy != y+1) &&
					( m(this, v(x, y-1), v(x, y+1), v(x, y+2))
				   || m(this, v(x, y-1), v(x, y+1), v(x, y-2)) ) ) { matchesmade += 0.25f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x, y-2), v(x, y-3)) ) { matchesmade += 0.25f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x, y+2), v(x, y+3)) ) { matchesmade += 0.25f; }

		return matchesmade;
	}
}
