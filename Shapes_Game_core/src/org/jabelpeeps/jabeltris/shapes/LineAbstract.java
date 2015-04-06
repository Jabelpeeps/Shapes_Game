package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.Shape;

public abstract class LineAbstract extends Shape  {
	
	public LineAbstract() {
		needsFlipping = true;
		animatable = true;
		animate();
	}
	@Override
	protected float shapeMatch(int x, int y) {
		
		float matchesmade = 0f;
		// horizontal lines
		if ( m( v(x-1, y), v(x+1, y)) ) { matchesmade += 1f/3f; }
		if ( m( v(x-1, y), v(x-2, y)) ) { matchesmade += 1f/3f; }
		if ( m( v(x+1, y), v(x+2, y)) ) { matchesmade += 1f/3f; }
		// vertical lines
		if ( m( v(x, y-1), v(x, y+1)) ) { matchesmade += 1f/3f; }
		if ( m( v(x, y-1), v(x, y-2)) ) { matchesmade += 1f/3f; }
		if ( m( v(x, y+1), v(x, y+2)) ) { matchesmade += 1f/3f; }

		return matchesmade;
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
	
		if ( pairInS1 || pairInS3 ) {
			
			Coords centreOfGroup = Coords.getCentre(list).add(0.5f);
			int x = centreOfGroup.x.i();
			int y = centreOfGroup.y.i();
			centreOfGroup.free();
			
			if 	 ( m( v(x+1, y) ) 
				|| m( v(x-2, y) ) 
				|| m( v(x, y+1) ) 
				|| m( v(x, y-2) ) 
				|| m( v(x+1, y-1) ) 
				|| m( v(x-2, y-1) ) 
				|| m( v(x-1, y+1) ) 
				|| m( v(x-1, y-2) ) ) return true;
		}		
		for ( int i = 0; i <= 3; i++ ) 
			if ( shapeMatch( list[i].x.i() , list[i].y.i() ) > 0f ) 
				return true;
		
		return false;
	}
}
