package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class BorderButtonsInput extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameLogic logic;
		private final PlayArea game;
// ---------------------------------------------Constructor----------	
		public BorderButtonsInput(PlayArea p, GameLogic l) {
			game = p;
			logic = l;
		}
// ---------------------------------------------Methods--------------	
	    @Override
		public boolean keyUp(int typed) {
	    	if ( typed == Keys.BACK || typed == Keys.BACKSPACE || typed == Keys.ESCAPE ) {
	    		logic.setBackKeyWasPressed(true);
			}
			
			// letter keys only used for debugging on desktop version.
			if ( typed == Keys.S && !logic.hasVisitor() ) 
				logic.acceptVisitor( new Shuffle() );
			
			if ( typed == Keys.H && !logic.hasVisitor() ) 
				logic.acceptVisitor( new RequestHint() );
			
			if ( typed == Keys.B && !logic.hasVisitor() ) 
				logic.acceptVisitor( new FreeMove() );
			
			if ( typed == Keys.NUM_1 )
				logic.toggleMechanic(1);
			
			if ( typed == Keys.NUM_2 )
				logic.toggleMechanic(2);
			
			return true;
		}
	    
	    class Shuffle implements LogicVisitor {
			@Override
			public void greet() {
				game.shuffleBoard();
			}	    	
	    }
	    class RequestHint implements LogicVisitor {
			@Override
			public void greet() {
				logic.getHint().blink(150, 3);
			}
	    }
	    class FreeMove implements LogicVisitor {
			@Override
			public void greet() {
				logic.takeBestMoveFound();
				game.matchesFoundAndScored();
				game.findHintsInAllshape();
			}
	    }
}
