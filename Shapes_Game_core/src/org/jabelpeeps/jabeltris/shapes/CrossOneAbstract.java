package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.Shape;

public abstract class CrossOneAbstract extends Shape {
	// crossone is the '+' shape.
	
	@Override
	protected float shapeMatch(int x, int y) {
		float matchesmade = 0f;
		
		// checks for a full upright cross and an unfilled cross (a rhomboid diamond)
		if ( m( v(x, y-1), v(x, y+1), v(x-1, y), v(x+1, y)) ) { matchesmade += 0.2f; }
		
		if ( m( v(x, y+1), v(x, y+2), v(x-1, y+1), v(x+1, y+1)) ) { matchesmade += 0.2f; }
		if ( m( v(x-1, y), v(x-2, y), v(x-1, y-1), v(x-1, y+1)) ) { matchesmade += 0.2f; }
		if ( m( v(x, y-1), v(x, y-2), v(x+1, y-1), v(x-1, y-1)) ) { matchesmade += 0.2f; }
		if ( m( v(x+1, y), v(x+2, y), v(x+1, y+1), v(x+1, y-1)) ) { matchesmade += 0.2f; }
		
		if ( m( v(x-1, y+1), v(x+1, y+1), v(x, y+2)) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y-1), v(x-1, y+1), v(x-2, y)) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y-1), v(x-1, y-1), v(x, y-2)) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y+1), v(x+1, y-1), v(x+2, y)) ) { matchesmade += 0.25f; }

		return matchesmade; 
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {

		if ( pairInS2 || ( pairInS1 && pairInS3 ) ) {
			
			Coords centreOfGroup = Coords.getCentre(list).add(0.5f);
			int x = centreOfGroup.xi();
			int y = centreOfGroup.yi();
			centreOfGroup.free();
		
			if ( m( v(x+1, y), v(x, y+1) ) ) return true;
			if ( m( v(x, y-2), v(x+1, y-1) ) ) return true;
			if ( m( v(x-1, y+1), v(x-2, y) ) ) return true;
			if ( m( v(x-2, y-1), v(x-1, y-2) ) ) return true; 
			
		} else {
			for ( int i = 1; i <= 3; i++ ) {
				if ( i == 1 && pairInS3 ) continue;
				if ( i == 3 && pairInS1 ) continue;
				if ( shapeMatch( list[i].xi , list[i].yi ) > 0f ) 
					return true;
			}
		}	
		return false;
	}
}
