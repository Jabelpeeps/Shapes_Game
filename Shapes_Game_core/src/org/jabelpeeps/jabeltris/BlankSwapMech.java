package org.jabelpeeps.jabeltris;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jabelpeeps.jabeltris.Core.HintVisitor;
import org.jabelpeeps.jabeltris.Core.LogicInputVisitor;
import org.jabelpeeps.jabeltris.GestureMultiplexer.TouchListener;
import org.jabelpeeps.jabeltris.Select.ShapeSelector;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

public class BlankSwapMech extends GameMechanic {

	private PlayArea game;
	private GameLogic logic;
	
	public BlankSwapMech(PlayArea g, GameLogic l) {
		game = g;
		logic = l;
		visitor = new BlankMoveHints();
		input = new BlankSwapInput();
	}
	
	public class BlankSwapInput extends GestureAdapter implements TouchListener {

		private final Coords nill = Coords.get();
		private final Coords touch = Coords.get();
        private final Coords selected;
        private final Select selector;
        
        private ScheduledFuture<?> animation;
		
    	private BlankSwapInput() {			
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
			if 		(  !logic.hasVisitor() 
					&& pointer == 0
					&& button == Buttons.LEFT ) {
				game.cameraUnproject( x, y, touch );
				
				if 		(  selector.shapeSelected()
						&& selected.isAdjacentTo( touch ) 
						&& game.getShape( touch ).isMobile() )
					return doSwapWithBlank();
			}
			return false;	
		}
		@Override 
		public boolean touchDownSecond(int x, int y, int pointer, int button) {	
			if 		(  !logic.hasVisitor() 
					&& pointer == 0 
					&& button == Buttons.LEFT ) {
				game.cameraUnproject( x , y , touch );
				
				if 		(  selected.isEqualTo( nill )
						|| !touch.isAdjacentTo( selected )  
						&& !game.getShape( touch ).isBlank() ) {
					selector.unSelectShape();
					selector.selectShape( touch , game );
				}
			}
			if 	( selector.shapeSelected() 
				&& 	(  animation == null 
					|| animation.isCancelled() ) ) {
				game.cameraUnproject( x, y, touch );
				animation = Core.threadPool.scheduleAtFixedRate( new BlinkBlanks() , 2000 , 4000 , TimeUnit.MILLISECONDS );
				selector.animations.add( animation );
			}
			return false; 
		}
		@Override
		public boolean touchDraggedSecond(int x, int y, int pointer) {
			if 		(  !logic.hasVisitor() 
					&& pointer == 0 
					&& selector.leftButtonDown ) {
				game.cameraUnproject( x, y, touch );
				
				if 		(  !touch.isEqualTo( selected ) 
						&& !selected.isEqualTo( nill )
						&& game.getShape( touch ).isMobile()
						&& selected.isAdjacentTo( touch ) 	) 
					return doSwapWithBlank();
			}
			return false;
		}
    	private boolean doSwapWithBlank() {
			selector.unSelectShape();
			logic.acceptVisitor( new DoSwapWithBlank() );
			selector.leftButtonDown = false;
			selected.set( -10, -10 );
			return true;
    	}
    	
    	class DoSwapWithBlank implements LogicInputVisitor {
			private int c1x, c1y, c2x, c2y;
			
			DoSwapWithBlank() {
			c1x = selected.xi;  c1y = selected.yi;  c2x = touch.xi;  c2y = touch.yi;
			}
			@Override
			public void greet() {
				
			// TODO add code to carry out move.
				
				if ( game.matchesFoundAndScored() ) {		
					game.findHintsInAllshape();
					return;
				}
				
			// TODO add code to undo move if no match.
			}
    	}
		@Override public boolean touchUpFirst(int x, int y, int pointer, int button) { return false; }
		@Override public boolean touchUpSecond(int x, int y, int pointer, int button) { return false; }
		@Override public boolean touchDraggedFirst(int x, int y, int pointer) { return false; }
	}
	
	@Override
	public int searchForMoves() {
		
		// TODO add move searching algorithm for autoplay.
		
		return 0;
	}

	@Override
	public void takeMove() {
		
		// TODO add move taking code.
		
	}
	class BlinkBlanks implements Runnable {
		@Override
		public void run() {
			synchronized ( game ) {
				
		// TODO add code to highlight legal (but unchecked for validity) moves.
			}
		}
	}
 
	class BlankMoveHints implements HintVisitor {
		@Override
		public boolean greet(int x, int y, Shape s) {
			
		// TODO add hinting methods
			
			return false;
		}
	}
}
