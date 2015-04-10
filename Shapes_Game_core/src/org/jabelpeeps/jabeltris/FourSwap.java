package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FourSwap implements GameMechanic {

	private PlayArea game;
	@SuppressWarnings("unused")
	private GameLogic logic;
	private FourSwapMethods special;
	private HintMethodVisitor visitor = new RotatingSquareHints();
	private Array<Shape> hintList = new Array<Shape>(false, 32, Shape.class);
	private ArrayMap<Shape, Coords> best4SwapMove = new ArrayMap<Shape, Coords>(true, 4, Shape.class, Coords.class);
	private boolean active = false;
	
	public FourSwap(PlayArea gam, GameLogic logi) {
		game = gam;
		logic = logi;
		special = new FourSwapMethods(gam);
	}

	public class RotatingSquareHints implements HintMethodVisitor {
		@Override
		public boolean greet(int x, int y, Shape s) {
			Array<Coords> coords = null;
			boolean returnvalue = false;
			
			for ( FourGroup each : FourGroup.values() ) {
				
				Coords.freeAll(coords);
				coords = each.get4plusC(x, y, s.game);
				
				if ( coords == null ) continue;
				boolean pairInS1 = false, pairInS2 = false, pairInS3 = false;
				
				if ( s.game.getShape(coords.items[1]).matches(s) ) pairInS1 = true; 
				if ( s.game.getShape(coords.items[2]).matches(s) ) pairInS2 = true;
				if ( s.game.getShape(coords.items[3]).matches(s) ) pairInS3 = true;	
		
				if ( pairInS1 && pairInS2 && pairInS3 ) continue;
				
				if ( s.hint4(pairInS1, pairInS2, pairInS3, coords.toArray()) ) {
					hintList.add(s);
					returnvalue = true;
					break;
				}
			}
			Coords.freeAll(coords);
			return returnvalue;
		}
	}

	@Override
	public int searchForMoves() {
		if ( !active || hintList.size < 1 ) return 0;
		
		Array<Coords> tmpCoords = null;
		IterateIn4 it4 = new IterateIn4();
		int highestMatches = 0;
		best4SwapMove.clear();
		
		for ( Shape everyShape : hintList ) {										// check each Shape on the hintList...
			int x = everyShape.getXi();
			int y = everyShape.getYi();
						
			checkGroups:
			for ( FourGroup eachgroup : FourGroup.values() ) {							// ...along with each of the surrounding  
		
				Coords.freeAll(tmpCoords);
				tmpCoords = eachgroup.get4plusC(x, y, everyShape.game);
					
				if ( tmpCoords == null ) continue checkGroups;							// ...(if possible)...
				
				tmpCoords.pop().free();
				Shape[] shapeList = new Shape[4];
				Coords[] coordList = tmpCoords.toArray();
				
				it4.set(0);
				for ( Coords eachShape : coordList )
					shapeList[it4.get()] = game.getShape(eachShape);
				
				for ( int i = 1; i <= 4; i++ ) {										// ...when rotated to each of the possible
																						// three other positions (other than the 
					it4.set(i);															// current). 
					for ( Coords everyPos : coordList ) 								 
						game.shapeTile[everyPos.xi][everyPos.yi] = shapeList[it4.get()];
						
					if ( i < 4 && game.boardHasMatches(0) 								// When i = 4, the Shapes are put back into
							   && ( game.getMatchListSize() > highestMatches ) ) {		// their starting positions.
						
						highestMatches = game.getMatchListSize();
						it4.set(i);
						best4SwapMove.clear();
						for ( Coords each : coordList ) {
							best4SwapMove.put(shapeList[it4.get()], Coords.copy(each));
						}
					}	
					game.clearMatchList();
				}
			}
		}
		Coords.freeAll(tmpCoords);
		
		return highestMatches;
	}

	@Override
	public void takeMove() {
		Coords[] coords = best4SwapMove.values;
		Shape[] shapes = best4SwapMove.keys;
		
		special.setupRotationGroups(coords, shapes);
		
		Shape tmpShape = shapes[0];
		Coords target = coords[0];
		boolean inPlace = false;
		boolean direction = Core.rand.nextBoolean();
		float deltaAngle = 0f;
		int framecount = 0;
		
		game.blinkList(100, 3, special.shapeList);

		while ( !inPlace ) {
			framecount += 5;
			deltaAngle = direction ? framecount
								   : 360 - framecount;
			special.rotateGroups(deltaAngle);
			Gdx.graphics.requestRendering();
			Core.delay(20);
			
			if ( 	MathUtils.isEqual(target.xi, tmpShape.getX(), 0.01f)
				 && MathUtils.isEqual(target.yi, tmpShape.getY(), 0.01f) ) 
				inPlace = true;
		}
		special.resetBaseTilesAndScales();	
		
		for ( Entry<Shape, Coords> each : best4SwapMove ) 
			each.key.setPosition(each.value);
			
		Gdx.graphics.requestRendering();
		special.clearRotationGroups();
		
		for ( int i = 0; i < 4; i++ )
			game.shapeTile[coords[i].xi][coords[i].yi] = shapes[i];
		
		Coords.freeAll(coords);
		Core.delay(20);
	}
	@Override
	public void activate() {
		Shape.addHintVisitor( visitor );
		active = true;
	}
	@Override
	public void deactivate() {
		active = false;
		Shape.removeHintVisitor( visitor );
	}
	@Override
	public void toggleActive() {
		if ( active )
			deactivate();
		else
			activate();
	}
	@Override
	public int getNumOfHints() {
		return hintList.size;
	}
	@Override
	public Array<Shape> getHintList() {
		return hintList;
	}
	@Override
	public void clearHints() {
		hintList.clear();
	}
}
