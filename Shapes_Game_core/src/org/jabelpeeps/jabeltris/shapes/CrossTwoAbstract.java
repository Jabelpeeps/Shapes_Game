package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class CrossTwoAbstract extends Shape {
	// crosstwo is the 'X' shape
	
	public CrossTwoAbstract() {
		setRegion(LevelMaster.greycrtwo);
		type = "CrossTwo";
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
