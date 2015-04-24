package org.jabelpeeps.jabeltris;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jabelpeeps.jabeltris.Core.HintVisitor;
import org.jabelpeeps.jabeltris.Core.LogicInputVisitor;
import org.jabelpeeps.jabeltris.GestureMultiplexer.TouchListener;
import org.jabelpeeps.jabeltris.Select.Group;
import org.jabelpeeps.jabeltris.Select.ShapeSelector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FourSwapMech extends GameMechanic {

	private PlayArea game;
	private GameLogic logic;
	private Array<Shape> shapeList = new Array<Shape>(true, 4, Shape.class);
	private Array<SpritePlus> spritesToMove = new Array<SpritePlus>(false, 8, SpritePlus.class);
	private final Coords centre = Coords.get();
	private ArrayMap<Shape, Coords> best4SwapMove = new ArrayMap<Shape, Coords>(true, 4, Shape.class, Coords.class);
	
	public FourSwapMech(PlayArea g, GameLogic l) {
		game = g;
		logic = l;
		visitor = new RotatingSquareHints();
		input = new FourSwapInput();
	}
	
	class FourSwapInput extends GestureAdapter implements TouchListener {

		private final Coords pointer1 = Coords.get();
		private final Coords pointer2 = Coords.get();
		private final Coords touch = Coords.get();
		private final Coords selected;
        private final Select selector;
        
        private ScheduledFuture<?> animation;
		private boolean rotationActive = false;
		private float initialAngle, deltaAngle;

		private FourSwapInput() {
			for ( GestureListener each : GestureMultiplexer.getInstance().getListeners() ) 
				if 		(  each instanceof ShapeSelector 
						&& game == ((ShapeSelector) each).game ) {
					selector = ((ShapeSelector) each).selector;
					selected = selector.selected;
					return;
				}
			selector = null;
			selected = null;
		}
		@Override
		public boolean touchDownFirst(int x, int y, int pointer, int button) {
			if ( pointer > 0 || logic.hasVisitor() ) return false;
			game.cameraUnproject( x , y , touch );
			
			if 		(  !selector.shapeSelected() 
					|| touch.isEqualTo( selected ) 
					|| touch.isAdjacentTo( selected ) ) 
				return false;
		
			pointer1.set( selected );
			selected.set( -10 , -10 );
			selector.unSelectShape();	
			deltaAngle = 0f;
			initialAngle = normalise( pointer2.set( touch ).sub( pointer1 ).angle() );
			
			Array<Coords> shapeCoords = Select.getValid4plusC( pointer1 , initialAngle , game );
			
			if ( shapeCoords == null ) return false;
			
			pointer1.set( shapeCoords.pop() );
			setupRotationGroups( shapeCoords );
			logic.acceptVisitor( new BlinkShapeList( 80 , 2 ) );
		
			rotationActive = true;
			Coords.freeAll( shapeCoords );
			return true;
		}
		@Override 
		public boolean touchDownSecond(int x, int y, int pointer, int button) { 
			if 	(  selector.shapeSelected() 
				&& 	(  animation == null 
					|| animation.isCancelled() ) ) {
				animation = Core.threadPool.scheduleAtFixedRate( new BlinkDiagonals(), 2500, 4000, TimeUnit.MILLISECONDS );
				selector.animations.add( animation );
			}
			return false; 
		}
		@Override
		public boolean touchDraggedFirst(int x, int y, int pointer) {
			if ( !rotationActive || pointer > 0 || logic.hasVisitor() ) return false;
			game.cameraUnproject( x , y , touch );
			
			pointer2.set( touch );	
			deltaAngle = normalise( pointer2.sub( pointer1 ).angle() - initialAngle );
			rotateGroups( deltaAngle );
			return true;
		}
		@Override
		public boolean touchUpSecond(int x, int y, int pointer, int button) {
			if ( !rotationActive || pointer > 0 ) return false;

			switch ( (int)( deltaAngle / 45 ) ) {
			case 7:	case 0: 
				cancelRotation();
				break;
			case 1:	case 2:			    
				logic.acceptVisitor( new Swap4ShapesIfPossible( 3 ) );
				break;
			case 3:	case 4: 			
				logic.acceptVisitor( new Swap4ShapesIfPossible( 2 ) );
				break; 
			case 5: case 6: 			
				logic.acceptVisitor( new Swap4ShapesIfPossible( 1 ) );
				break;
			}
			return true;
		}
		
		private float normalise(float angle) {
			while ( angle >= 360 )
				angle -= 360;
			while ( angle < 0 )
				angle += 360;
			return angle;
		}
		private void cancelRotation() {
			synchronized ( this ) {
				if ( !rotationActive ) return;
				else rotationActive = false;
			}
			for ( SpritePlus each : spritesToMove ) {
				each.resetPosition().scale( +0.1f );
				if ( !(each instanceof Shape) )
					each.restoreAlpha();
			}
			clearRotationGroups();
		}
		
		class BlinkShapeList implements LogicInputVisitor {
			private long time;
			private int repeats;
			
			BlinkShapeList(long time, int repeats) {
				this.time = time;
				this.repeats = repeats;
			}
			@Override
			public void greet() {
				Shape.blink( time , repeats , shapeList );
				
				if ( !Gdx.input.isTouched() ) {
					cancelRotation();
					Gdx.graphics.requestRendering();
				}
			}
		}
		
		class Swap4ShapesIfPossible implements LogicInputVisitor {
			private int segment;
			
			Swap4ShapesIfPossible(int seg) {
				segment = seg;
				rotationActive = false;
			}
			@Override
			public void greet() {
				resetBaseTilesAndScales();
				
				Coords	saved0 = Coords.copy( shapeList.items[0].getSavedXY() ),
						saved1 = Coords.copy( shapeList.items[1].getSavedXY() ),
						saved2 = Coords.copy( shapeList.items[2].getSavedXY() ),
						saved3 = Coords.copy( shapeList.items[3].getSavedXY() );
				
				Coords[] savedList = new Coords[]{saved0, saved1, saved2, saved3};
				IterateIn4 it4 = new IterateIn4( segment );
				
				for ( Coords each : savedList )
					game.shapeTilePut( each , (Shape) shapeList.items[ it4.get() ].setPosition( each ) );	
				
				Shape.blink( 80 , 2 , shapeList );
				
				if ( game.matchesFoundAndScored() ) 
					game.findHintsInAllshape();
				else {
					it4.set( 0 );
					for ( Coords each : savedList )
					game.shapeTilePut( each , (Shape) shapeList.items[ it4.get() ].setNewXY( each ).saveXY() );
					
					for ( int a = 1; a <= 8; a++ ) {		// animate shapes back to their old positions.
						for ( Shape each : shapeList ) {
							game.moveShape( each , a );
							each.setRotation( 180 - 20 * a );
						}
						Gdx.graphics.requestRendering();
						Core.delay( 30 );
					}
					for ( Shape each : shapeList )
						each.setRotation( 0 );
					
					Gdx.graphics.requestRendering();	
				}
				clearRotationGroups();
				Coords.freeAll( savedList );
			}
		}
		@Override public boolean touchUpFirst(int x, int y, int pointer, int button) { return false; }
		@Override public boolean touchDraggedSecond(int x, int y, int pointer) { return false; }
	}

	@Override
	public int searchForMoves() {
		if ( !active || hintList.size() < 1 ) return 0;
		
		Array<Coords> tmpCoords = null;
		IterateIn4 it4 = new IterateIn4();
		int highestMatches = 0;
		best4SwapMove.clear();
		
		for ( Shape everyShape : hintList ) {										// check each Shape on the hintList...
			int x = everyShape.getXi();
			int y = everyShape.getYi();
						
			checkGroups:
			for ( Select.Group eachgroup : Select.FourCornerGroups ) {					// ...along with each of the surrounding  
		
				Coords.freeAll( tmpCoords );
				tmpCoords = eachgroup.getValidGroup( x , y , everyShape.game );
					
				if ( tmpCoords == null ) continue checkGroups;						// ...(if possible)...
				
				Shape[] shapeList = new Shape[4];
				Coords[] coordList = tmpCoords.toArray();
				
				it4.set( 0 );
				for ( Coords eachShape : coordList )
					shapeList[ it4.get() ] = game.getShape( eachShape );
				
				for ( int i = 1; i <= 4; i++ ) {										// ...when rotated to each of the possible
																						// three other positions (other than the 
					it4.set( i );															// current). 
					for ( Coords everyPos : coordList ) 								 
						game.shapeTilePut( everyPos , shapeList[ it4.get() ] );
						
					if ( i < 4 && game.boardHasMatches(0) 								// When i = 4, the Shapes are put back into
							   && ( game.getMatchListSize() > highestMatches ) ) {		// their starting positions.
						
						highestMatches = game.getMatchListSize();
						best4SwapMove.clear();
						it4.set( i );
						for ( Coords each : coordList ) {
							best4SwapMove.put( shapeList[ it4.get() ], Coords.copy( each ) );
						}
					}	
					game.clearMatchList();
				}
			}
		}
		Coords.freeAll( tmpCoords );
		return highestMatches;
	}
	@Override
	public void takeMove() {
		Coords[] coords = best4SwapMove.values;
		Shape[] shapes = best4SwapMove.keys;
		Shape tmpShape = shapes[0];
		Coords target = coords[0];
		
		Shape.blink( 100 , 3 , true , shapes );
		setupRotationGroups( coords , shapes );
		
		boolean inPlace = false;
		boolean direction = Core.rand.nextBoolean();
		float deltaAngle = 0f;
		int framecount = 0;

		while ( !inPlace ) {
			framecount += 5;
			deltaAngle = direction ? framecount
								   : 360 - framecount;
			rotateGroups( deltaAngle );
			Gdx.graphics.requestRendering();
			Core.delay( 20 );
			
			if ( 	MathUtils.isEqual( target.xi , tmpShape.getX() , 0.01f )
				 && MathUtils.isEqual( target.yi , tmpShape.getY() , 0.01f ) ) 
				inPlace = true;
		}
		resetBaseTilesAndScales();	
		
		for ( Entry<Shape, Coords> each : best4SwapMove ) 
			each.key.setPosition( each.value );
			
		Gdx.graphics.requestRendering();
		clearRotationGroups();
		
		for ( int i = 0; i < 4; i++ )
			game.shapeTilePut( coords[i] , shapes[i] );
		
		Coords.freeAll( coords );
		Core.delay( 20 );
	}
	
	private class BlinkDiagonals implements Runnable {
		@Override
		public void run() {
			synchronized ( game ) {
				for ( Group each : Select.FourCornerGroups )
					Shape.blinkTile( 150 , 1 , each.fetchSelectionGroup() );
				Core.delay( 150 );
			}
		}
	}
	private class RotatingSquareHints implements HintVisitor {
		@Override
		public boolean greet(int x, int y, Shape s) {
			Array<Coords> coords = null;
			boolean returnvalue = false;
			
			for ( Select.Group each : Select.FourCornerGroups ) {
				
				Coords.freeAll( coords );
				coords = each.getValidGroupPlusC( x , y , s.game );
				
				if ( coords == null ) continue;
				boolean pairInS1 = false, pairInS2 = false, pairInS3 = false;
				
				if ( s.game.getShape( coords.items[1] ).matches( s ) ) pairInS1 = true; 
				if ( s.game.getShape( coords.items[2] ).matches( s ) ) pairInS2 = true;
				if ( s.game.getShape( coords.items[3] ).matches( s ) ) pairInS3 = true;	
		
				if ( pairInS1 && pairInS2 && pairInS3 ) continue;
				
				if ( s.hint4( pairInS1, pairInS2, pairInS3, coords.toArray() ) ) {
					hintList.add( s );
					returnvalue = true;
					break;
				}
			}
			Coords.freeAll( coords );
			return returnvalue;
		}
	}
	private void setupRotationGroups(Array<Coords> list) {
		setupShapeList( list );
		finishSetup( list.toArray() );		
	}
	private void setupRotationGroups(Coords[] coords, Shape[] shapes) {
		shapeList.addAll( shapes );
		finishSetup( coords );
	}
	private void setupShapeList(Array<Coords> list) {
		for ( Coords each : list ) 
			shapeList.add( game.getShape( each ) );
	}
	private void finishSetup(Coords[] list) {
		for ( Coords each : list ) 
			spritesToMove.add( game.getBoardTile( each ).adjustAlpha( -0.2f ) );
	
		centre.setToCentre( list );
		spritesToMove.addAll( shapeList );
		
		for ( SpritePlus each : spritesToMove ) 
			each.saveXY().scale( -0.1f );
	}
	private void rotateGroups(float angle) {
		for ( SpritePlus each : spritesToMove ) 
			each.rotateInSquare( angle , centre ) ;
	}
	private void resetBaseTilesAndScales() {
		for ( SpritePlus each : spritesToMove ) {
			each.scale( +0.1f );
			if ( !(each instanceof Shape) ) 
				each.resetPosition().restoreAlpha();
		}
	}
	private void clearRotationGroups() {
		shapeList.clear();
		spritesToMove.clear();
	}
}
