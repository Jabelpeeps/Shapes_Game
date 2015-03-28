package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;

public class PlayAreaInput extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameLogic logic;
		private final PlayArea game;
		private final Coords touch = Coords.get();
		private final Coords saved = Coords.get();
        private final Coords nill = Coords.get();
		private boolean leftButtonDown = false;
// ---------------------------------------------Constructor----------	
		public PlayAreaInput(PlayArea p, GameLogic l) {
				game = p;
				logic = l;
		}
		// ---------------------------------------------Methods--------------	
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			
			if ( !logic.hasVisitor() && pointer == 0 && button == Buttons.LEFT ) {
				
				leftButtonDown = true;
				game.cameraUnproject(x, y, touch);
				if ( !game.hasShapeSelected() )
					saved.set(nill);
					
				if 		(  game.outOfBounds(touch) 
						|| game.getShape(touch).isBlank() 
						|| saved.isEqualTo(touch) )
					resetSavedTile();		
				
				else if (  saved.isEqualTo(nill)
						|| !saved.isAdjacentTo(touch) ) 
					setSavedTile(touch);
										
				else setSwapCandidates();   
				return true;
			}
			return false;
		}
		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			if ( pointer == 0 && button == Buttons.LEFT ) leftButtonDown = false;
			return false;
		}
		@Override
		public boolean touchDragged(int x, int y, int pointer) {
			
			if ( !logic.hasVisitor() && pointer == 0 && leftButtonDown ) {

				game.cameraUnproject(x, y, touch);
				
				if 		(  !touch.isEqualTo(saved) 
						&& !saved.isEqualTo(nill)
						&& !game.outOfBounds(touch) 
						&& !game.getShape(touch).isBlank()
						&& saved.isAdjacentTo(touch) 	) {
					setSwapCandidates();
					return true;
				}
			}
			return false;
		}
		private void setSavedTile(Coords set) {
			resetSavedTile();
			saved.set(set);
			game.selectShape(set);
		}
		private void resetSavedTile() {
			saved.reset();
			if ( game.hasShapeSelected() ) game.unSelectShape();
		}
		
		private void setSwapCandidates() {
			logic.acceptVisitor( new DoSwapIfSwappable() );
			resetSavedTile();
		}
		class DoSwapIfSwappable implements LogicVisitor {
			private int c1x, c1y, c2x, c2y;
			
			DoSwapIfSwappable() {
				c1x = saved.ix;
				c1y = saved.iy;
				c2x = touch.ix;
				c2y = touch.iy;
			}
			@Override
			public void greet() {
				game.doSwapIfSwapable(c1x, c1y, c2x, c2y);
			}
		}
}
