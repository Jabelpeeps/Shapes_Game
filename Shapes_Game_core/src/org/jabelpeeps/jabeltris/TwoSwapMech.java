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

public class TwoSwapMech extends GameMechanic {
	
	private PlayArea game;
	private GameLogic logic;
	private Shape firstShape, otherShape; 
	
	public TwoSwapMech(PlayArea g, GameLogic l) {
		game = g;
		logic = l;
		visitor = new StandardMoveHints();
		input = new TwoSwapInput();
	}
	
	class TwoSwapInput extends GestureAdapter implements TouchListener {
		
        private final Select selector;
        private final Coords selected;
		private final Coords touch = Coords.get();
        private final Coords nill = Coords.get();
        private ScheduledFuture<?> animation;
		
        private TwoSwapInput() {			
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
						&& !game.getShape( touch ).isBlank() )
					return setSwapCandidates();
			}
			return false;	
		}
		@Override 
		public boolean touchDownSecond(int x, int y, int pointer, int button) {			
			if 	( selector.shapeSelected() 
				&& 	(  animation == null 
					|| animation.isCancelled() ) ) {
				game.cameraUnproject( x, y, touch );
				animation = Core.threadPool.scheduleAtFixedRate( new BlinkCardinals() , 2000 , 4000 , TimeUnit.MILLISECONDS );
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
						&& !game.getShape( touch ).isBlank()
						&& selected.isAdjacentTo( touch ) 	) 
					return setSwapCandidates();
			}
			return false;
		}
		
		private boolean setSwapCandidates() {
			selector.unSelectShape();
			logic.acceptVisitor( new DoSwapIfSwappable() );
			selector.leftButtonDown = false;
			selected.set( -10, -10 );
			return true;
		}
		class DoSwapIfSwappable implements LogicInputVisitor {
			private int c1x, c1y, c2x, c2y;
			
			DoSwapIfSwappable() {
			c1x = selected.xi;  c1y = selected.yi;  c2x = touch.xi;  c2y = touch.yi;
			}
			@Override
			public void greet() {
				game.getBoardTile( c1x , c1y ).restoreAlpha();
				game.getBoardTile( c2x , c2y ).restoreAlpha();
				game.animateSwap( c1x , c1y , c2x , c2y );  // Swap the shapes... 
						
				if ( game.matchesFoundAndScored() ) {		// and score matches...
					game.findHintsInAllshape();
					return;
				} 
				Core.delay( 80 );
				game.animateSwap( c1x , c1y , c2x , c2y );	// or swap back no match found
			}
		}
		@Override public boolean touchUpFirst(int x, int y, int pointer, int button) { return false; }
		@Override public boolean touchUpSecond(int x, int y, int pointer, int button) { return false; }
		@Override public boolean touchDraggedFirst(int x, int y, int pointer) { return false; }
	}
	@Override
	protected int searchForMoves() {
		if ( !active || hintList.size() < 1 ) return 0;	
		firstShape = null;
		otherShape = null;
		int highestMatches = 0;
		
		for ( Shape eachShape : hintList ) {
			int x = eachShape.getXi();
			int y = eachShape.getYi();
						
			for ( Shape whichShape : Select.Group.Cardinals.getMobileShapes( x, y, game ) ) {
				
				game.shapeTileArraySwap( eachShape , whichShape );
				
				if ( game.boardHasMatches(0) && ( game.getMatchListSize() > highestMatches ) ) {
					highestMatches = game.getMatchListSize();
					firstShape = eachShape;
					otherShape = whichShape;
				}					
				game.shapeTileArraySwap( eachShape , whichShape );
				game.clearMatchList();
			}
		}
		return highestMatches;
	}
	@Override
	protected void takeMove() {
		Shape.blink( 100 , 3 , true , firstShape , otherShape );
		game.animateSwap( firstShape , otherShape );
	}
	
	class BlinkCardinals implements Runnable {
		@Override
		public void run() {
    		synchronized ( game ) {
    			for ( Shape each : Select.Group.Cardinals.fetchSelectionGroup() )
    				Shape.blinkTile( 150 , 1 , each );
				Core.delay(150);
    		}
    	}
    }
	class StandardMoveHints implements HintVisitor {
		@Override
		public boolean greet(int x, int y, Shape s) {
			if (   ( !game.getShape(x+1, y).isBlank() && s.shapeMatch(x+1, y) > 0f ) 
				|| ( !game.getShape(x, y+1).isBlank() && s.shapeMatch(x, y+1) > 0f ) 
				|| ( !game.getShape(x-1, y).isBlank() && s.shapeMatch(x-1, y) > 0f ) 
				|| ( !game.getShape(x, y-1).isBlank() && s.shapeMatch(x, y-1) > 0f ) ) {
					hintList.add(s);
					return true;
			}			
			return false;
		}
	}	
}
