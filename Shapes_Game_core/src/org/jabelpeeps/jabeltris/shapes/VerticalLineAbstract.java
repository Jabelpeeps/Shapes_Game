package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class VerticalLineAbstract extends Shape {
	
	public VerticalLineAbstract() {
		setRegion(LevelMaster.greyline);
		type = "VerticalLine";
	}
	
	@Override
	protected float shapeMatch(int x, int y, int xx, int yy) {
		
		float matchesmade = 0f;
		// vertical lines
		if ( (yy != y-1) && (yy != y+1) && m(this, v(x, y-1), v(x, y+1)) ) { matchesmade += 1f/3f; }
		if ( (yy != y-1) && m(this, v(x, y-1), v(x, y-2)) ) { matchesmade += 1f/3f; }
		if ( (yy != y+1) && m(this, v(x, y+1), v(x, y+2)) ) { matchesmade += 1f/3f; }

		return matchesmade;
	}
}
