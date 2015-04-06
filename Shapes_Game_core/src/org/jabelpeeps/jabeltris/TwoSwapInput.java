package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Input.Buttons;

public class TwoSwapInput extends SelectShape {

	public TwoSwapInput(PlayArea p, GameLogic l) {
		super(p, l);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		
		if ( !logic.hasVisitor() && pointer == 0 && button == Buttons.LEFT ) {
			
			if ( game.hasShapeSelected() )
				return setSwapCandidates();
		}
		return false;	
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		
		if ( !logic.hasVisitor() && pointer == 0 && leftButtonDown ) {

			game.cameraUnproject(x, y, touch);
			
			if 		(  !touch.isEqualTo(saved) 
					&& !saved.isEqualTo(nill)
					&& !game.getShape(touch).isBlank()
					&& saved.isAdjacentTo(touch) 	) 
				return setSwapCandidates();
		}
		return false;
	}
	
	private boolean setSwapCandidates() {
		logic.acceptVisitor( new DoSwapIfSwappable() );
		resetSavedTile();
		return true;
	}
	
	class DoSwapIfSwappable implements LogicVisitor {
		private int c1x, c1y, c2x, c2y;
		
		DoSwapIfSwappable() {
			c1x = saved.x.i();
			c1y = saved.y.i();
			c2x = touch.x.i();
			c2y = touch.y.i();
		}
		@Override
		public void greet() {

			game.animateSwap(c1x, c1y, c2x, c2y);       // Swap the shapes... 
					
			if ( game.matchesFoundAndScored() ) 		// and score matches...
				game.findHintsOnBoard();
			else {
				Core.delay(80);
				game.animateSwap(c1x, c1y, c2x, c2y);	// or swap back no match found
			}
		}
	}
}
