package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;

public class FourSwapInput extends InputAdapter {

	private PlayArea game;
	private FourSwapMethods special;
	private GameLogic logic;
	
	private boolean onePointerDown = false;
	private boolean twoPointersDown = false;
	private boolean rotationActive = false;
	private float initialAngle, deltaAngle;

	private final Coords pointer1 = Coords.get();
	private final Coords pointer2 = Coords.get();
	private final Coords touch = Coords.ints();
	
	public FourSwapInput(PlayArea g, GameLogic l) {
		game = g;
		special = new FourSwapMethods(g);
		logic = l;
	}
	
	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		return touchDown((float)x, (float)y, pointer, button);
	}
	public boolean touchDown (float x, float y, int pointer, int button) {
		if ( pointer > 1 || logic.hasVisitor() ) return false;
		boolean validtouch = true;

		game.cameraUnproject(x, y, touch);
		
		if ( game.hasShapeSelected() ) {
			
			Coords selected = game.getSelectedShape().getXY();
			
			if ( touch.isEqualTo(selected) || touch.isAdjacentTo(selected) ) 
				validtouch = false;
			
			selected.free();
			onePointerDown = validtouch;
			if ( !validtouch ) return false;
		} 
		if ( Gdx.input.isTouched(0) && Gdx.input.isTouched(1) ) 
			setPointersDown(false, true);
		
		if ( !(twoPointersDown || onePointerDown) ) return false; 
		
		if ( game.hasShapeSelected() ) {
			Coords selected = game.getSelectedShape().getXY();
			pointer1.set(selected);
			game.unSelectShape();		
			selected.free();
			initialAngle = pointer2.set(touch).sub(pointer1).angle();
		} else {
			game.cameraUnproject(Gdx.input.getX(0), Gdx.input.getY(0), pointer1);
			game.cameraUnproject(Gdx.input.getX(1), Gdx.input.getY(1), pointer2);
			initialAngle = pointer2.sub(pointer1).angle();
		}
		Array<Coords> shapeCoords = get4ValidCoords(pointer1, initialAngle);
		
		if ( shapeCoords == null ) {
			setPointersDown(false, false);
			return false;
		}
		special.setupRotationGroups(shapeCoords);
		logic.acceptVisitor( new BlinkSpecialShapeList(80, 2) );
			
		if ( onePointerDown )
			pointer1.set(special.getGroupCentre());
		
		rotationActive = true;
		Coords.freeAll(shapeCoords);
		return true;
	}
	
	private Array<Coords> get4ValidCoords(Coords c, float angle) {
		
		boolean setAllowed = true;
		Array<Coords> setOf4 = new Array<Coords>(true, 4, Coords.class);
		int x = c.x.i();
		int y = c.y.i();
		
		switch ( (int)(angle / 90) ) {
		case 0:
			setOf4.addAll(Coords.ints(x, y), Coords.ints(x+1, y), Coords.ints(x+1, y+1), Coords.ints(x, y+1) );
			break;
		case 1:
			setOf4.addAll(Coords.ints(x, y), Coords.ints(x, y+1), Coords.ints(x-1, y+1), Coords.ints(x-1, y) );
			break;
		case 2:
			setOf4.addAll(Coords.ints(x, y), Coords.ints(x-1, y), Coords.ints(x-1, y-1), Coords.ints(x, y-1) );
			break;
		case 3:
			setOf4.addAll(Coords.ints(x, y), Coords.ints(x, y-1), Coords.ints(x+1, y-1), Coords.ints(x+1, y) );
			break;
		}
		for ( Coords each : setOf4 ) 
			if ( game.getShape(each).isBlank() ) 
				setAllowed = false;
		
		if ( !setAllowed ) 
			Coords.freeAll(setOf4);
			
		return ( setAllowed ? setOf4 : null );
	}
	
	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		return touchDragged((float)x, (float)y, pointer);
	}
	public boolean touchDragged (float x, float y, int pointer) {
		if ( pointer > 1 || logic.hasVisitor() ) return false;
		if ( !(onePointerDown || twoPointersDown) ) return false;

		game.cameraUnproject(x, y, touch);
		
		if ( pointer == 0 && twoPointersDown )
			pointer1.set(touch.x.f(), touch.y.f());
		else        								
			pointer2.set(touch.x.f(), touch.y.f());			
		
		deltaAngle = ( pointer2.sub(pointer1).angle() - initialAngle );

		while ( deltaAngle >= 360 )
			deltaAngle -= 360;
		while ( deltaAngle < 0 )
			deltaAngle += 360;		
		
		special.rotateGroups(deltaAngle);
		return true;
	}	
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return touchUp((float)x, (float)y, pointer, button);
	}
	public boolean touchUp (float x, float y, int pointer, int button) {
		if ( pointer > 1 ) return false;

		if ( twoPointersDown ) {
			twoPointersDown = false;
			if ( pointer == 0 ) 	// this refers to the lifted pointer.
				onePointerDown = true;
			else  
				cancelRotation();
			return true;
		}		
		if ( onePointerDown ) {
			onePointerDown = false;
				
			switch ( (int)( deltaAngle / 45 ) ) {
			case 7:
			case 0: 
				cancelRotation();
				break;
			case 1:
			case 2:			    
				logic.acceptVisitor( new Swap4ShapesIfPossible(3) );
				break;
			case 3:
			case 4: 			
				logic.acceptVisitor( new Swap4ShapesIfPossible(2) );
				break; 
			case 5: 
			case 6: 			
				logic.acceptVisitor( new Swap4ShapesIfPossible(1) );
				break;
			}
			return true;
		}
		return false;
	}
	private void cancelRotation() {
		
		synchronized (this) {
			if ( !rotationActive ) return;
			else rotationActive = false;
		}
		for ( SpritePlus each : special.spritesToMove ) {
			each.resetPosition().scale(+0.1f);
			if ( !(each instanceof Shape) )
				each.setColor(game.baseColor);
		}
		special.clearRotationGroups();
	}
	private void setPointersDown(boolean onePointer, boolean twoPointers) {
		onePointerDown = onePointer;
		twoPointersDown = twoPointers;
	}
	
	class Swap4ShapesIfPossible implements LogicVisitor {
		private int segment;
		
		Swap4ShapesIfPossible(int seg) {
			segment = seg;
		}
		@Override
		public void greet() {
			special.swap4ShapesIfPossible(segment);
		}
	}
	
	class BlinkSpecialShapeList implements LogicVisitor {
		private long time;
		private int repeats;
		
		BlinkSpecialShapeList(long time, int repeats) {
			this.time = time;
			this.repeats = repeats;
		}
		@Override
		public void greet() {
			game.blinkList(time, repeats, special.shapeList);
			if ( !Gdx.input.isTouched() ) {
				setPointersDown(false, false);
				cancelRotation();
				Gdx.graphics.requestRendering();
			}
		}
	}
}
	