package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public abstract class GameLogic extends Thread {
	
	protected PlayArea game;
	protected boolean backKeyWasPressed = false;
	protected boolean startSignalSet = true;
	protected boolean endlessPlayMode = false;
	protected boolean handOverBoard = false;
	protected boolean loopIsEnding = false;
	private FourSwapMethods special;
	
	public GameLogic(PlayArea g) {		
		game = g;
		this.setDaemon(true);
	}
	public void takeBest2SwapMove() {
		Shape[] localHintList = game.getHintList();
		int bestMatches = 0;
		Shape bestShape = null;
		Shape bestWhich = null;
		
		for ( Shape eachShape : localHintList ) {
			for ( int[] whichWay : Core.LEFT_UP_RIGHT_DOWN ) {
				Shape whichShape = game.getShape( eachShape.getXi() + whichWay[0] , eachShape.getYi() + whichWay[1] );
				
				if ( whichShape.isBlank() ) continue;
				
				game.shapeTileArraySwap( eachShape , whichShape );
				
				if ( game.boardHasMatches(0) && ( game.getMatchListSize() > bestMatches ) ) {
					bestMatches = game.getMatchListSize();
					bestShape = eachShape;
					bestWhich = whichShape;
				}					
				game.shapeTileArraySwap( eachShape , whichShape );
				game.clearMatchList();
			}
		}
		long time = (long) (( Core.rand.nextGaussian() + 1.5) * 150);
		Core.delay(time);
		if ( loopIsEnding ) return;
		
		game.blinkList(100, 5, bestShape , bestWhich );
		game.animateSwap( bestShape , bestWhich );
	}
	
	public void takeBest4SwapMove() {
		special = new FourSwapMethods(game);
		Shape[] localHintList = game.getHintList();
		int bestNumberOfMatches = 0;
		ArrayMap<Shape, Coords> bestShapesAndNewCoords = new ArrayMap<Shape, Coords>(true, 4, Shape.class, Coords.class);
		
		IterateIn4 it4 = new IterateIn4();
		Coords 	seg0 = Coords.ints(), 
				seg1 = Coords.ints(), 
				seg2 = Coords.ints(), 
				seg3 = Coords.ints();
		final Coords[] segList = new Coords[] {seg0, seg1, seg2, seg3};
		
		for ( Shape eachShape : localHintList ) {										// check each Shape on the hintList...
			int x = eachShape.getXi();
			int y = eachShape.getYi();
			
			checkGroups:
			for ( int[] dir : Core.LEFT_UP_RIGHT_DOWN ) {										// ...along with each of the surrounding  
												// loop 1) sm = 1  sl = 1  now = true		groups of four Shapes...
				int sm = dir[0] - dir[1] ;		// loop 2) sm = -1 sl = 1  now = false
				int sl = dir[1] + dir[0] ;		// loop 3) sm = -1 sl = -1 now = true
				boolean now = sm * sl > 0;		// loop 4) sm = 1  sl = -1 now = false

				seg0.set( x 				, y 				);
				seg1.set( x + (now? sm:0)	, y + (now? 0:sl) 	);
				seg2.set( x + sm 			, y + sl 			);
				seg3.set( x + (now? 0:sm)	, y + (now? sl:0) 	);
				
				for ( Coords each : segList)					
					if ( game.getShape(each).isBlank() ) continue checkGroups;						// ...(if possible)...
				
				Shape[] shapeList = new Shape[4];
				
				it4.set(0);
				for ( Coords each : segList )
					shapeList[it4.get()] = game.getShape(each);
				
				for ( int i = 1; i <= 4; i++ ) {										// ...when rotated to each of the possible
																						// three other positions (other than the 
					it4.set(i);															// current). 
					for ( Coords each : segList ) 										// When i = 4, the Shapes are put back into 
						game.shapeTile[each.x.i()][each.y.i()] = shapeList[it4.get()];		// their starting positions.
						
					if ( i < 4 && game.boardHasMatches(0) 
							   && ( game.getMatchListSize() > bestNumberOfMatches ) ) {
						
						bestNumberOfMatches = game.getMatchListSize();
						it4.set(i);
						bestShapesAndNewCoords.clear();
						for ( Coords each : segList ) {
							bestShapesAndNewCoords.put(shapeList[it4.get()], Coords.get(each));
						}
					}	
					game.clearMatchList();
				}
			}
		}
		if ( loopIsEnding ) return;
		
		Coords[] coords = bestShapesAndNewCoords.values;
		Shape[] shapes = bestShapesAndNewCoords.keys;
		
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
			
			if ( 	MathUtils.isEqual(target.x.i(), tmpShape.getX(), 0.01f)
				 && MathUtils.isEqual(target.y.i(), tmpShape.getY(), 0.01f) ) 
				inPlace = true;
		}
		for ( Entry<Shape, Coords> each : bestShapesAndNewCoords ) 
			each.key.setPosition(each.value);
		
		special.resetBaseTilesAndScales();		
		Gdx.graphics.requestRendering();
		special.clearRotationGroups();
		
		for ( int i = 0; i < 4; i++ )
			game.shapeTile[coords[i].x.i()][coords[i].y.i()] = shapes[i];
		
		bestShapesAndNewCoords = null;
		Coords.freeAll(coords);
		Coords.freeAll(seg0, seg1, seg2, seg3, target);
		Core.delay(20);
	}
	public boolean getEndlessPlayMode() {
		return endlessPlayMode;
	}
	public void setEndlessPlayMode(boolean mode) {
	}
	public void shutDown() {
		loopIsEnding = true;
		startSignalSet = true;
	}
	public void setBackKeyWasPressed(boolean state) {
		backKeyWasPressed = state;
	}
	public boolean getBackKeyWasPressed() {
		return backKeyWasPressed;
	}
	public void waitForStartSignal() {
		startSignalSet = false;
	}
	public void sendStartSignal() {
		startSignalSet = true;
	}
	public void acceptVisitor(LogicVisitor visitor) {
	}
	public abstract boolean hasVisitor();
}
