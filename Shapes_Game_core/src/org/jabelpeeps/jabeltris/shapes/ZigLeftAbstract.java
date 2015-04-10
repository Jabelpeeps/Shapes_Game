package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class ZigLeftAbstract extends Shape {

	public ZigLeftAbstract() {
		setRegion(LevelMaster.zigleft);
		type = "ZigLeft";
		animatable = true;
		animate();
	}
	@Override
	protected float shapeMatch(int x, int y) {
		float matchesmade = 0f;

		if ( m( v(x+1, y), v(x+1, y+1), v(x+2, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x+1, y-1), v(x+1, y-2) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x-1, y-1), v(x-2, y-1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x-1, y+1), v(x-1, y+2) ) ) { matchesmade += 0.25f; }

		if ( m( v(x-1, y), v(x, y+1), v(x+1, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x, y-1), v(x-1, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x, y-1), v(x-1, y-1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x, y+1), v(x+1, y-1) ) ) { matchesmade += 0.25f; }
		
		return matchesmade;
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
		
		if ( !pairInS1 && !pairInS3 ) {
			for ( int i = 0; i <= 3; i++ ) 
				if ( shapeMatch( list[i].xi , list[i].yi ) > 0f ) return true;
		}
		Coords centreOfGroup = Coords.getCentre(list).add(0.5f);
		int x = centreOfGroup.xi;
		int y = centreOfGroup.yi;
		centreOfGroup.free();

		if ( pairInS2 && ( m( v(x+1, y) ) 
						|| m( v(x, y-2) ) 
						|| m( v(x-2, y-1) ) 
						|| m( v(x-1, y+1) ) ) ) return true;
		
		if ( m( v(x+1, y-1), v(x+1, y-2) ) ) return true;
		if ( m( v(x, y+1), v(x+1, y+1) ) ) return true;
		if ( m( v(x-2, y), v(x-2, y+1) ) ) return true;
		if ( m( v(x-1, y-2), v(x-2, y-2) ) ) return true;	
	
		return false;
	}
}
