package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.Shape;

public abstract class SquareAbstract extends Shape {
	
	@Override
	protected float shapeMatch(int x, int y) {
		
		float matchesmade = 0f;
		if ( m( v(x-1, y), v(x, y-1), v(x-1, y-1)) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x, y+1), v(x+1, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x, y+1), v(x-1, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x, y-1), v(x+1, y-1)) ) { matchesmade += 0.25f; }
		
		return matchesmade; 
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
	
		Coords centreOfGroup = list[4];
		int x = centreOfGroup.xi;
		int y = centreOfGroup.yi;
		
		if ( pairInS1 || pairInS3 ) {
			if ( m( v(x+1, y), v(x+1, y-1) ) ) return true;
			if ( m( v(x-2, y), v(x-2, y-1) ) ) return true;
			if ( m( v(x, y+1), v(x-1, y+1) ) ) return true;
			if ( m( v(x, y-2), v(x-1, y-2) ) ) return true;	
		} else {
			if ( m( v(x+1, y), v(x, y+1), v(x+1, y+1)) ) return true;
			if ( m( v(x-2, y), v(x-1, y+1), v(x-2, y+1)) ) return true;
			if ( m( v(x-2, y-1), v(x-1, y-2), v(x-2, y-2)) ) return true;
			if ( m( v(x+1, y-1), v(x, y-2), v(x+1, y-2)) ) return true;
		}
		return false;
	}
}
