package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class FourSwapMethods {
	
	private PlayArea game;
	protected Array<Shape> shapeList = new Array<Shape>(true, 4, Shape.class);;
	protected Array<SpritePlus> spritesToMove = new Array<SpritePlus>(false, 8, SpritePlus.class);
	private final Coords centre = Coords.get();
	
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
			game.shapeTile[each.xi][each.yi] = (Shape) shapeList.items[ it4.get() ].setPosition(each);	
		
		game.blinkList(80, 2, shapeList);
		
		if ( game.matchesFoundAndScored() ) 
			game.findHintsInAllshape();
		else {
			it4.set(0);
			for ( Coords each : savedList )
			game.shapeTile[each.xi][each.yi] = (Shape) shapeList.items[ it4.get() ].setNewXY(each).saveXY();
			
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
}
