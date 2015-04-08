package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class MirrorEllAbstract extends Shape {

	public MirrorEllAbstract() {
		setRegion(LevelMaster.mirrorell);
		type = "MirrorEll";
		animatable = true;
		animate();
	}
	@Override
	protected float shapeMatch(int x, int y) {
		
		float matchesmade = 0f;
		// four rotations needed
		// calling block on short arm
		if ( m( v(x, y-1), v(x+1, y-1), v(x+2, y-1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x-1, y+1), v(x-2, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x-1, y-1), v(x-1, y-2) ) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x+1, y+1), v(x+1, y+2) ) ) { matchesmade += 0.25f; }
		// calling block at corner
		if ( m( v(x, y+1), v(x+1, y), v(x+2, y) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x-1, y), v(x-2, y) ) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x, y-1), v(x, y-2) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x, y+1), v(x, y+2) ) ) { matchesmade += 0.25f; }
		// calling block at end of long arm
		if ( m( v(x+1, y), v(x+2, y), v(x+2, y-1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x-2, y), v(x-2, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x, y-2), v(x-1, y-2) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x, y+2), v(x+1, y+2) ) ) { matchesmade += 0.25f; }
		// calling block in middle of long arm
		if ( m( v(x-1, y), v(x+1, y), v(x-1, y+1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x+1, y), v(x+1, y-1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x, y-1), v(x-1, y-1) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x, y-1), v(x+1, y+1) ) ) { matchesmade += 0.25f; }
		
		return matchesmade;
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
	
		if ( !pairInS1 && !pairInS3 ) {
			for ( int i = 0; i <= 3; i++ ) 
				if ( shapeMatch( list[i].xi() , list[i].yi() ) > 0f ) return true;
		}
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

		if ( m( v(x+1, y), v(x+1, y-1) ) ) return true;
		if ( m( v(x-2, y), v(x-2, y-1) ) ) return true;
		if ( m( v(x, y+1), v(x-1, y+1) ) ) return true;
		if ( m( v(x, y-2), v(x-1, y-2) ) ) return true;
		
		if ( m( v(x+1, y-1), v(x+2, y-1) ) ) return true;
		if ( m( v(x-2, y), v(x-3, y) ) ) return true;
		if ( m( v(x, y+1), v(x, y+2) ) ) return true;
		if ( m( v(x-1, y-2), v(x-1, y-3) ) ) return true;	
		
		return false;
	}
}
