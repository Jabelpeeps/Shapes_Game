package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;

public class SelectShape extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		protected final GameLogic logic;
		protected final PlayArea game;
		static final Coords touch = Coords.floats();
		static final Coords saved = Coords.ints();
        static final Coords nill = Coords.ints();
		static boolean leftButtonDown = false;
// ---------------------------------------------Constructor----------	
		public SelectShape(PlayArea p, GameLogic l) {
				game = p;
				logic = l;
		}
// ---------------------------------------------Methods--------------	
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			
			if ( !logic.hasVisitor() && pointer == 0 && button == Buttons.LEFT ) {
				
				leftButtonDown = true;
				game.cameraUnproject(x, y, touch);
				touch.updateAllValues();
				
				if ( !game.hasShapeSelected() )
					saved.set(nill);
					
				if 		(  game.getShape(touch).isBlank() 
						|| saved.isEqualTo(touch) )
					return resetSavedTile();		
				
				else if (  saved.isEqualTo(nill)
						|| !saved.isAdjacentTo(touch) ) 
					return setSavedTile(touch);
			}
			return false;
		}
		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			if ( pointer == 0 && button == Buttons.LEFT ) leftButtonDown = false;
			return false;
		}
		private boolean setSavedTile(Coords touch) {
			resetSavedTile();
			saved.set(touch);
			game.selectShape(touch);
			return true;
		}
		protected boolean resetSavedTile() {
			saved.set(-10, -10);
			if ( game.hasShapeSelected() ) 
				game.unSelectShape();
			return true;
		}
		
}
