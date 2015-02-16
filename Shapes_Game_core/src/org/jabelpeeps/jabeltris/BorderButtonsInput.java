package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;

public class BorderButtonsInput extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameLogic logic;
		@SuppressWarnings("unused")
		private final PlayArea game;
//		private Vector3 touch = new Vector3();
		
// ---------------------------------------------Constructor----------	
		public BorderButtonsInput(PlayArea p, GameLogic l) {
			game = p;
			logic = l;
			
		}
// ---------------------------------------------Methods--------------	
	    @Override
		public boolean keyTyped(char typed) {
//	    	System.out.println("---------------");
//	    	System.out.println(typed);
//	    	System.out.println("---------------");
	    	
			if ( typed == '' ) {
				logic.setBackKeyPressed();				
			}
			
			// letter keys only used for debugging on desktop version.
			if ( typed == 's' ) {
				logic.setShuffle();
			}
			if ( typed == 'h' ) {
				if ( !logic.hintHasBeenGiven() ) {
					logic.requestHint();
				} 
			}
			return true;
		}
	
//		@Override
//		public boolean touchDown(int x, int y, int pointer, int button) {
//			touch.set(x, y, 0);
//			Core.camera.unproject(touch);
//			
//			
//			return false;
//		}
//	
//		@Override
//		public boolean touchUp(int x, int y, int pointer, int button) {
//			touch.set(x, y, 0);
//			Core.camera.unproject(touch);
//			
//			return false;
//		}
//	
//		@Override
//		public boolean touchDragged(int x, int y, int pointer) {
//			return false;
//		}
//	
		
}
