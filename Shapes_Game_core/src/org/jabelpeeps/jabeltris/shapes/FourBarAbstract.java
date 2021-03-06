package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

public abstract class FourBarAbstract extends Shape {

	public FourBarAbstract() {
			setRegion(LevelMaster.fourbar);
			type = "FourBar";
			animatable = true;
			animate();
	}
	@Override
	protected float shapeMatch(int x, int y) {
		float matchesmade = 0f;
		if ( m( v(x-1, y), v(x+1, y), v(x+2, y) ) ) { matchesmade += 0.25f; }
		if ( m( v(x-1, y), v(x+1, y), v(x-2, y) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x, y+1), v(x, y+2) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x, y+1), v(x, y-2) ) ) { matchesmade += 0.25f; }
		
		return matchesmade + armMatch(x, y);
	}	
	private float armMatch(int x, int y) {
		float matchesmade = 0f;
		if ( m( v(x-1, y), v(x-2, y), v(x-3, y) ) ) { matchesmade += 0.25f; }
		if ( m( v(x+1, y), v(x+2, y), v(x+3, y) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y-1), v(x, y-2), v(x, y-3) ) ) { matchesmade += 0.25f; }
		if ( m( v(x, y+1), v(x, y+2), v(x, y+3) ) ) { matchesmade += 0.25f; }
		
		return matchesmade;
	}
	@Override
	protected boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list) {
	
		if ( pairInS1 || pairInS3 ) {
			Coords centreOfGroup = list[4];
			int x = centreOfGroup.xi;
			int y = centreOfGroup.yi;
			
			if ( m( v(x+1, y), v(x+1, y-1) ) ) return true;
			if ( m( v(x-2, y), v(x-2, y-1) ) ) return true;
			if ( m( v(x, y+1), v(x-1, y+1) ) ) return true;
			if ( m( v(x, y-2), v(x-1, y-2) ) ) return true;	
		} 
		else {
			for ( int i = 0; i <= 3; i++ ) {
				if ( i == 2 && pairInS2 ) continue;
				if ( armMatch( list[i].xi , list[i].yi ) > 0f ) return true;
			}
		}
		return false;
	}
}