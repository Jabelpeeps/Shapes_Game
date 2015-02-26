package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;


public abstract class HorizontalLineAbstract extends Shape {
	
	public HorizontalLineAbstract() {
		setRegion(LevelMaster.horizgreyline);
		type = "HorizontalLine";
	}
	
	@Override
	protected float shapeMatch(int x, int y, int xx, int yy) {
		
		float matchesmade = 0f;
		// horizontal lines
		if ( (xx != x+1) && (xx != x-1)	&& m(this, v(x-1, y), v(x+1, y)) ) { matchesmade += 1f/3f; }
		if ( (xx != x-1) && m(this, v(x-1, y), v(x-2, y)) ) { matchesmade += 1f/3f; }
		if ( (xx != x+1) && m(this, v(x+1, y), v(x+2, y)) ) { matchesmade += 1f/3f; }

		return matchesmade;
	}
}
