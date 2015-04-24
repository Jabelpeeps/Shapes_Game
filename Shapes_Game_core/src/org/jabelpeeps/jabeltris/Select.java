package org.jabelpeeps.jabeltris;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.jabelpeeps.jabeltris.GestureMultiplexer.TouchListener;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.utils.Array;

public class Select {
	
	private Shape selectedShape = null;
	protected final Coords selected = Coords.get();
	protected boolean leftButtonDown = false;
	protected final Array<ScheduledFuture<?>> animations = new Array<ScheduledFuture<?>>(4);
	
	public final static Set<Group> FourCornerGroups = Collections.unmodifiableSet( 
			EnumSet.of( Group.UpRight, Group.UpLeft, Group.DownLeft, Group.DownRight ) );	
	
	enum Pt {
		ORIGIN 		( 0,  0),	RIGHT 	(+1,  0),	UP_RIGHT 	(+1, +1),
		UP 			( 0, +1),	UP_LEFT (-1, +1),	LEFT		(-1,  0),
		DOWN_LEFT 	(-1, -1),	DOWN 	( 0, -1),	DOWN_RIGHT 	(+1, -1),
		RIGHT2		(+1,  0);		
		
		private Pt(int xi, int yi) { x = xi; y = yi; }
		final int x, y;		
	}
	enum Group {
		Cardinals	( EnumSet.of(Pt.RIGHT, 		Pt.UP, 		Pt.LEFT, 		Pt.DOWN), 		Pt.ORIGIN ),
		Corners		( EnumSet.of(Pt.UP_RIGHT, 	Pt.UP_LEFT, Pt.DOWN_LEFT, 	Pt.DOWN_RIGHT), Pt.ORIGIN ),
		UpRight	 	( EnumSet.of(Pt.ORIGIN, 	Pt.RIGHT, 	Pt.UP_RIGHT, 	Pt.UP), 		Pt.UP_RIGHT ),
		UpLeft	 	( EnumSet.of(Pt.ORIGIN, 	Pt.UP, 		Pt.UP_LEFT, 	Pt.LEFT), 		Pt.UP ),
		DownLeft	( EnumSet.of(Pt.ORIGIN, 	Pt.LEFT, 	Pt.DOWN_LEFT, 	Pt.DOWN), 		Pt.ORIGIN ),
		DownRight	( EnumSet.of(Pt.ORIGIN, 	Pt.DOWN, 	Pt.DOWN_RIGHT, 	Pt.RIGHT2), 	Pt.RIGHT ),
		EightAround ( EnumSet.of(Pt.RIGHT, Pt.UP_RIGHT, Pt.UP, Pt.UP_LEFT, Pt.LEFT, Pt.DOWN_LEFT, Pt.DOWN, Pt.DOWN_RIGHT), Pt.ORIGIN);
	
		private Group(Set<Pt> enumset, Pt cent) { 
			set = Collections.unmodifiableSet( enumset ); 
			centre = cent; 
		}
		private final Set<Pt> set; 
		private final Pt centre;
		private final Array<Coords> returnCoords = new Array<Coords>(true, 8, Coords.class);
		private final Array<Shape> returnShapes = new Array<Shape>(true, 8, Shape.class);
		
		Array<Coords> getValidGroupPlusC(int x, int y, PlayArea game) {
			if ( getValidGroup( x , y , game ) == null ) return null;
			
			returnCoords.add( Coords.get( x + centre.x , y + centre.y ) );
			return returnCoords;
		}
		Array<Coords> getValidGroup(int x, int y, PlayArea game) {
			returnCoords.clear();
			boolean setAllowed = true;
			
			for ( Pt each : set ) {
				Coords tempRef = Coords.get( x + each.x , y + each.y );
				returnCoords.add( tempRef );
				Shape tempShape = game.getShape( tempRef );
						
				if ( tempShape.isBlank() || !tempShape.isMobile() ) {
					setAllowed = false;
					Coords.freeAll( returnCoords );
					break;
				}
			}
			return setAllowed ? returnCoords 
							  : null;
		}
		Array<Shape> getMobileShapes(int x, int y, PlayArea game) {
			setupSelectionGroup( x , y , game );
			return fetchSelectionGroup();
		}
		void setupSelectionGroup(int x, int y, PlayArea game) {
			returnShapes.clear();
			
			for ( Pt each : set ) {
				if ( each == Pt.ORIGIN ) continue;
				Shape tempShape = game.getShape( x + each.x , y + each.y );
				
				if ( tempShape.isBlank() || !tempShape.isMobile() ) continue;
				returnShapes.add( tempShape );
			}
		}
		Array<Shape> fetchSelectionGroup() {
			return ( returnShapes.size > 0 ) ? returnShapes 
											 : null;
		}
	}
//-----------------------------------------------------end of Enum-------------
	
	static Array<Coords> getValid4plusC(Coords o, float angle, PlayArea game) {
		return getSetfromAngle( angle ).getValidGroupPlusC( o.xi , o.yi , game );
	}
	static Array<Coords> getValidGroupOf4(Coords o, float angle, PlayArea game) {
		return getSetfromAngle( angle ).getValidGroup( o.xi , o.yi , game );
	}
	private static Group getSetfromAngle(float angle) {
		switch ( (int) ( angle / 90 ) ) {
			case 0: return Group.UpRight;
			case 1:	return Group.UpLeft;
			case 2:	return Group.DownLeft;
			case 3:	return Group.DownRight;
			default: return null;
		}
	}
	public boolean shapeSelected() {
		return selectedShape != null;
	}
	public void unSelectShape() {
		if ( shapeSelected() ) {
			selectedShape.deselect( true );
			selectedShape = null;
			cancelAnimations();
		}
	}
	public void selectShape(Coords touch, PlayArea game){
		selected.set( touch );
		selectedShape = game.getShape( touch ).select( true );
	}
	void cancelAnimations() {
		for ( ScheduledFuture<?> each : animations ) 
			if ( !each.isCancelled() ) 
				each.cancel( false );
	}
	void activateShapeSelector(PlayArea game, GameLogic logic) {
		GestureMultiplexer.getInstance().addListener( 0 , new ShapeSelector( game , logic , this ) );
	}
	
	class ShapeSelector extends GestureAdapter implements TouchListener {
		
		protected final GameLogic logic;
		protected final PlayArea game;
		protected final Select selector;
		protected final Coords touch = Coords.get();
        protected final Coords nill = Coords.get();
        private boolean shapeUnselected = false;
		
		public ShapeSelector(PlayArea p, GameLogic l, Select s) {
			game = p;
			logic = l;
			selector = s;
		}
		@Override
		public boolean touchDownFirst(int x, int y, int pointer, int button) {
			if 		(  !logic.hasVisitor() 
					&& pointer == 0 
					&& button == Buttons.LEFT ) {
				leftButtonDown = true;
				game.cameraUnproject( x , y , touch );
				
				if 		( !shapeSelected() )
					selected.set( nill );
				else if	(  touch.isEqualTo( selected ) 
						|| !game.getShape( touch ).isMobile() )	{
					unSelectShape();
					selected.set( -10 , -10 );
					shapeUnselected = true;
				}
			}
			return false;
		}	
		@Override
		public boolean touchDownSecond(int x, int y, int pointer, int button) { 
			if 		(  !logic.hasVisitor() 
					&& pointer == 0 
					&& button == Buttons.LEFT
					&& !shapeUnselected ) {
				game.cameraUnproject( x , y , touch );
				
				if 		(  selected.isEqualTo( nill )
						|| !touch.isAdjacentTo( selected )  
						&& !game.getShape( touch ).isBlank() ) {
					unSelectShape();
					selectShape( touch , game );
					
					for ( Group each : Group.values() ) 
						each.setupSelectionGroup( touch.xi , touch.yi , game );
				}
			}
			shapeUnselected = false;
			return false;
		}
		@Override public boolean touchUpFirst(int x, int y, int pointer, int button) {
			if ( pointer == 0 && button == Buttons.LEFT ) 
				leftButtonDown = false;
			return false;
		}
		@Override public boolean touchUpSecond(int x, int y, int pointer, int button) { return false; }
		@Override public boolean touchDraggedFirst(int x, int y, int pointer) { return false; }
		@Override public boolean touchDraggedSecond(int x, int y, int pointer) { return false; }
	}
}
