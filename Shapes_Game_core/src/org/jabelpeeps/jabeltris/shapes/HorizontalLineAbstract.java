package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.Shape;

public abstract class HorizontalLineAbstract extends Shape {
	
	@Override
	protected float shapeMatch(int x, int y) {
			
		float matchesmade = 0f;
		// horizontal lines
		if ( m( v(x-1, y), v(x+1, y)) ) { matchesmade += 1f/3f; }
		if ( m( v(x-1, y), v(x-2, y)) ) { matchesmade += 1f/3f; }
		if ( m( v(x+1, y), v(x+2, y)) ) { matchesmade += 1f/3f; }

		return matchesmade;
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
	
		Coords centreOfGroup = Coords.getCentre(list).add(0.5f);
		int x = centreOfGroup.xi();
		int y = centreOfGroup.yi();
		centreOfGroup.free();
		
		if ( pairInS1 || pairInS3 ) {
			if ( m( v(x+1, y) ) ) return true;
			if ( m( v(x-2, y) ) ) return true;	
			if ( m( v(x+1, y-1) ) ) return true;
			if ( m( v(x-2, y-1) ) ) return true;
			
		} else {
			if ( m( v(x+1, y), v(x+2, y) ) ) return true;
			if ( m( v(x-2, y), v(x-3, y) ) ) return true;	
			if ( m( v(x+1, y-1), v(x+2, y-1) ) ) return true;
			if ( m( v(x-2, y-1), v(x-3, y-1) ) ) return true;
		}
		return false;
	}
}

