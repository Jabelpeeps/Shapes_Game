package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.utils.Array;

public enum FourGroup {
	RIGHT(	Coords.get(0, 0), 	Coords.get(+1, 0), 	Coords.get(+1, +1),	Coords.get(0, +1), Coords.get(1, 1)	), 
	UP(		Coords.get(0, 0), 	Coords.get(0, +1), 	Coords.get(-1, +1),	Coords.get(-1, 0), Coords.get(0, 1)	), 
	LEFT(	Coords.get(0, 0), 	Coords.get(-1, 0), 	Coords.get(-1, -1),	Coords.get(0, -1), Coords.get(0, 0)	), 
	DOWN(	Coords.get(0, 0), 	Coords.get(0, -1), 	Coords.get(+1, -1),	Coords.get(+1, 0), Coords.get(1, 0)	);	
	
	private final Array<Coords> setOf4plusC = new Array<Coords>(true, 5, Coords.class);	
	
	private FourGroup(Coords zero, Coords one, Coords two, Coords three, Coords centre) {
		setOf4plusC.addAll(zero, one, two, three, centre);
	}
	
	private static FourGroup getGroup(float angle) {	
		switch ( (int)(angle / 90) ) {
			case 0: return RIGHT;
			case 1:	return UP;
			case 2:	return LEFT;
			case 3:	return DOWN;
		}
		return null;
	}
	
	protected static Array<Coords> get4plusC(Coords c, float angle, PlayArea game) {
		return getGroup(angle).get4plusC(c.xi, c.yi, game);
	}
	
	protected Array<Coords> get4plusC(int x, int y, PlayArea game) {
		
		Array<Coords> returnSet = new Array<Coords>(true, 5, Coords.class);
		boolean setAllowed = true;
		int loopCount = 1;
		
		for ( Coords each : setOf4plusC ) {
			Coords temp = Coords.get( x + each.xi, y + each.yi );
			returnSet.add(temp);
			
			if ( loopCount++ < 5 && game.getShape(temp).isBlank() ) {
				Coords.freeAll(returnSet);
				setAllowed = false;
				break;
			}
		}
		return setAllowed ? returnSet : null;
	}
}