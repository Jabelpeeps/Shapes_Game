package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class BorderButtonsInput extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameLogic logic;
//		private Vector3 touch = new Vector3();
		
// ---------------------------------------------Constructor----------	
		public BorderButtonsInput(GameLogic l) {
			logic = l;

			Gdx.input.setCatchBackKey(true);
		}
// ---------------------------------------------Methods--------------	
	    @Override
		public boolean keyTyped(char typed) {
			if ( typed == Keys.BACK ) {
				logic.setBackKeyPressed();
			}
			
			// letter keys only used for debugging on desktop version.
			if ( typed == Keys.S ) {
				logic.setShuffle();
			}
			if ( typed == Keys.H ) {
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
