package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class FourSwapMethods {
	
	private PlayArea game;
	protected Array<Shape> shapeList = new Array<Shape>(true, 4, Shape.class);;
	protected Array<SpritePlus> spritesToMove = new Array<SpritePlus>(false, 8, SpritePlus.class);
	private final Coords centre = Coords.floats();
	
	FourSwapMethods(PlayArea game) {
		this.game = game;
	}
	
	void setupRotationGroups(Array<Coords> list) {
		Array<Shape> tmpList = new Array<Shape>(false, 4, Shape.class);
		
		for ( Coords each : list ) 
			tmpList.add( game.getShape(each) );
		
		setupRotationGroups(list.toArray(), tmpList.toArray());			
	}
	void setupRotationGroups(Coords[] coords, Shape[] shapes) {
		if ( shapeList.size > 0 || spritesToMove.size > 0 ) 
			System.out.println("ERROR - attempting to setup rotation groups before they have been reset.");
		
		shapeList.addAll(shapes);
		
		for ( Coords each : coords ) 
			spritesToMove.add( game.getBoardTile(each).setNewAlpha(0.5f) );
		
		centre.setToCentre(coords);
		spritesToMove.addAll(shapeList);
		
		for ( SpritePlus each : spritesToMove ) 
			each.saveXY().scale(-0.1f);
	}
	Coords getGroupCentre() {
		return centre;
	}
	void rotateGroups(float angle) {
		for ( SpritePlus each : spritesToMove ) 
			each.rotateInSquare(angle, centre);
	}
	void resetBaseTilesAndScales() {
		for ( SpritePlus each : spritesToMove ) {
			each.scale(+0.1f);
			if ( !(each instanceof Shape) ) 
				each.resetPosition().setColor(game.baseColor);
		}
	}
	void clearRotationGroups() {
		shapeList.clear();
		spritesToMove.clear();
	}
	void swap4ShapesIfPossible(int segment) {
		resetBaseTilesAndScales();
		
		Coords	saved0 = Coords.copy( shapeList.items[0].getSavedXY() ),
				saved1 = Coords.copy( shapeList.items[1].getSavedXY() ),
				saved2 = Coords.copy( shapeList.items[2].getSavedXY() ),
				saved3 = Coords.copy( shapeList.items[3].getSavedXY() );
		
		Coords[] savedList = new Coords[]{saved0, saved1, saved2, saved3};
		IterateIn4 it4 = new IterateIn4(segment);
		
		for ( Coords each : savedList )
			game.shapeTile[each.xi()][each.yi()] = (Shape) shapeList.items[ it4.get() ].setPosition(each);	
		
		game.blinkList(80, 2, shapeList);
		
		if ( game.matchesFoundAndScored() ) 
			game.findHintsOnBoard();
		else {
			it4.set(0);
			for ( Coords each : savedList )
			game.shapeTile[each.xi()][each.yi()] = (Shape) shapeList.items[ it4.get() ].setNewXY(each).saveXY();
			
			for ( int a = 1; a <= 8; a++ ) {		// animate shapes back to their old positions.
				for ( Shape each : shapeList ) {
					game.moveShape(each, a);
					each.setRotation( 180 - 20 * a );
				}
				Gdx.graphics.requestRendering();
				Core.delay(30);
			}
			for ( Shape each : shapeList )
				each.setRotation(0);
			
			Gdx.graphics.requestRendering();	
		}
		clearRotationGroups();
		Coords.freeAll(savedList);
	}
	public Array<Coords> get4Coords(Coords c, float angle) {
		
		switch ( (int)(angle / 90) ) {
		case 0: 
			return Group.RIGHT.get4(c.xi(), c.yi(), game);
		case 1:
			return Group.UP.get4(c.xi(), c.yi(), game);
		case 2:
			return Group.LEFT.get4(c.xi(), c.yi(), game);
		case 3:
			return Group.DOWN.get4(c.xi(), c.yi(), game);
		}
		return null;
	}
	enum Group {
		RIGHT(Coords.get(0, 0), Coords.get(1, 0), Coords.get(1, 1), Coords.get(0, 1)), 
		UP(Coords.get(0, 0), Coords.get(0, 1), Coords.get(-1, 1), Coords.get(-1, 0)), 
		LEFT(Coords.get(0, 0), Coords.get(-1, 0), Coords.get(-1, -1), Coords.get(0, -1)), 
		DOWN(Coords.get(0, 0), Coords.get(0, -1), Coords.get(1, -1), Coords.get(1, 0));	
		
		private final Array<Coords> setOf4 = new Array<Coords>(true, 4, Coords.class);
		
		private Group(final Coords one, final Coords two, final Coords three, final Coords four) {
			setOf4.addAll(one, two, three, four);
		}
		
		protected Array<Coords> get4(int x, int y, PlayArea game) {
			boolean setAllowed = true;
			Array<Coords> returnSet = new Array<Coords>(true, 4, Coords.class);
			
			for ( Coords each : setOf4 ) {
				Coords temp = Coords.get( x + each.xi, y + each.yi );
				returnSet.add(temp);
				
				if ( game.getShape(temp).isBlank() ) {
					Coords.freeAll(returnSet);
					setAllowed = false;
					break;
				}
			}
			return setAllowed ? returnSet : null;
		}
	}
}
