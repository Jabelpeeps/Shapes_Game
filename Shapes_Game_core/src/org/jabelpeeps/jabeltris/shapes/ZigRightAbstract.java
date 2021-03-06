package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class ZigRightAbstract extends Shape {

	public ZigRightAbstract() {
		setRegion(LevelMaster.zigright);
		type = "ZigRight";
		animatable = true;
		animate();
	}
	@Override
	protected float shapeMatch(int x, int y) {
		float matchesmade = 0f;
		
		if ( m( v(x, y+1), v(x+1, y+1), v(x+1, y+2) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x-1, y+1), v(x-2, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x-1, y-1), v(x-1, y-2) ) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x+1, y-1), v(x+2, y-1) ) ) { matchesmade += 0.25f; }
		
		if ( m( v(x+1, y), v(x, y-1), v(x+1, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x, y+1), v(x-1, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x, y+1), v(x-1, y-1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x, y-1), v(x+1, y-1) ) ) { matchesmade += 0.25f; }

		return matchesmade;
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
		
		if ( pairInS1 || pairInS3 ) {
			Coords centreOfGroup = list[4];
			int x = centreOfGroup.xi;
			int y = centreOfGroup.yi;

			if ( pairInS2 && ( m( v(x-2, y) ) 
							|| m( v(x, y+1) ) 
							|| m( v(x+1, y-1) ) 
							|| m( v(x-1, y-2) ) ) ) return true;
			
			if ( m( v(x+1, y), v(x+1, y+1) ) ) return true;
			if ( m( v(x-1, y+1), v(x+1, y-2) ) ) return true;
			if ( m( v(x-2, y-1), v(x-2, y-2) ) ) return true;
			if ( m( v(x, y-2), v(x+1, y-2) ) ) return true;	
		
		} else {
			for ( int i = 0; i <= 3; i++ ) {
				if ( i == 2 && pairInS2 ) continue;
				if ( shapeMatch( list[i].xi , list[i].yi ) > 0f ) 
					return true;
			}
		}
		return false;
	}
}
