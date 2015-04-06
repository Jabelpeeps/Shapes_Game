package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.Shape;

public abstract class CrossTwoAbstract extends Shape {
	// crosstwo is the 'X' shape
	
	@Override
	public float shapeMatch(int x, int y) {
		
		float matchesmade = 0f;
		// check for a full "X" cross
		if ( m( v(x-1, y-1), v(x+1, y+1), v(x-1, y+1), v(x+1, y-1)) ) { matchesmade += 0.2f; }
		if ( m( v(x+1, y+1), v(x+2, y+2), v(x, y+2), v(x+2, y)) ) { matchesmade += 0.2f; }
		if ( m( v(x-1, y-1), v(x-2, y-2), v(x, y-2), v(x-2, y)) ) { matchesmade += 0.2f; }
		if ( m( v(x+1, y-1), v(x+2, y-2), v(x, y-2), v(x+2, y)) ) { matchesmade += 0.2f; }
		if ( m( v(x-1, y+1), v(x-2, y+2), v(x, y+2), v(x-2, y)) ) { matchesmade += 0.2f; }
		
		// check for the corners of an "X" only 
		if ( m( v(x+2, y), v(x, y-2), v(x+2, y-2)) ) { matchesmade += 0.25f; }
		if ( m( v(x-2, y), v(x, y+2), v(x-2, y+2)) ) { matchesmade += 0.25f; }
		if ( m( v(x+2, y), v(x, y+2), v(x+2, y+2)) ) { matchesmade += 0.25f; }
		if ( m( v(x-2, y), v(x, y-2), v(x-2, y-2)) ) { matchesmade += 0.25f; }

		return matchesmade; 
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
		
		Coords centreOfGroup = Coords.getCentre(list).add(0.5f);
		int x = centreOfGroup.x.i();
		int y = centreOfGroup.y.i();
		centreOfGroup.free();

		if ( m( v(x+1, y+1), v(x+1, y-1), v(x-1, y+1) ) ) return true;
		if ( m( v(x, y+1), v(x-2, y+1), v(x-2, y-1) ) ) return true;
		if ( m( v(x-2, y), v(x-2, y-2), v(x, y-2) ) ) return true;
		if ( m( v(x+1, y), v(x+1, y-2), v(x-1, y-2) ) ) return true; 
		
		for ( int i = 0; i <= 3; i++ )
			if ( shapeMatch( list[i].x.i() , list[i].y.i() ) > 0f ) 
				return true;
					
		return false;
	}
}
