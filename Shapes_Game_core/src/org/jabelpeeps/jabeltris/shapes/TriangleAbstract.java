package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.Shape;

public abstract class TriangleAbstract extends Shape {
	// really a short 'T' shape, but triangle sounded better.
	
	@Override
	protected float shapeMatch(int x, int y) {	
		
		float matchesmade = 0f;
		// calling block at centre (4 rotations, order: up, down, right, left)
		if ( m( v(x-1, y), v(x+1, y), v(x, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x+1, y), v(x, y-1)) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x, y-1), v(x, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x, y-1), v(x, y+1)) ) { matchesmade += 0.25f; }
		
		return matchesmade + armMatch(x, y);
	}	
	private float armMatch(int x, int y) {
		float matchesmade = 0f;
		// calling block on left arm
		if ( m( v(x+1, y), v(x+2, y), v(x+1, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x-2, y), v(x-1, y-1)) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x, y-2), v(x+1, y-1)) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x, y+2), v(x-1, y+1)) ) { matchesmade += 0.25f; }
		// calling block on right arm
		if ( m( v(x-1, y), v(x-2, y), v(x-1, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x+2, y), v(x+1, y-1)) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x, y+2), v(x+1, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x, y-2), v(x-1, y-1)) ) { matchesmade += 0.25f; }
		// calling block on centre arm
		if ( m( v(x-1, y-1), v(x+1, y-1), v(x, y-1)) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y+1), v(x+1, y+1), v(x, y+1)) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y+1), v(x-1, y-1), v(x-1, y)) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y+1), v(x+1, y-1), v(x+1, y)) ) { matchesmade += 0.25f; }

		return matchesmade; 
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
		
		if ( pairInS1 || pairInS3 ) {
			
			Coords centreOfGroup = Coords.getCentre(list).add(0.5f);
			int x = centreOfGroup.xi();
			int y = centreOfGroup.yi();
			centreOfGroup.free();
	
			if ( pairInS2 && ( m( v(x+1, y) ) 
							|| m( v(x-2, y) ) 
							|| m( v(x, y+1) ) 
							|| m( v(x, y-2) ) 
							|| m( v(x+1, y-1) ) 
							|| m( v(x-2, y-1) ) 
							|| m( v(x-1, y+1) ) 
							|| m( v(x-1, y-2) ) ) ) return true;
			
			if ( m( v(x+1, y), v(x, y+1) ) ) return true;
			if ( m( v(x, y-2), v(x+1, y-1) ) ) return true;
			if ( m( v(x-1, y+1), v(x-2, y) ) ) return true;
			if ( m( v(x-2, y-1), v(x-1, y-2) ) ) return true;	
			
		} else {
			for ( int i = 1; i <= 3; i++ ) {
				if ( i == 2 && pairInS2 ) continue;
				if ( armMatch( list[i].xi , list[i].yi ) > 0f ) 
					return true;
			}
		}
		return false;
	}
}
