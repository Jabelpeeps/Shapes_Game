package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

public class BorderButtonsInput extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameLogic logic;
		private Vector3 touch = new Vector3();
		
// ---------------------------------------------Constructor----------	
		public BorderButtonsInput(GameLogic l) {
			logic = l;
		}
// ---------------------------------------------Methods--------------	
	
		@Override
		public boolean keyTyped(char typed) {
			if ( typed == 's') {
				logic.setShuffle();
			}
			return true;
		}
	
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			touch.set(x, y, 0);
			Core.camera.unproject(touch);
			
			
			return false;
		}
	
		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			touch.set(x, y, 0);
			Core.camera.unproject(touch);
			
			return false;
		}
	
		@Override
		public boolean touchDragged(int x, int y, int pointer) {
			return false;
		}
	
		
}
