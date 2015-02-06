package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

class InputEvents extends InputAdapter {
// ---------------------------------------------Field(s)-------------
		private final GameBoard board;
		private Vector3 touch = new Vector3();
// ---------------------------------------------Constructor----------	
		InputEvents(GameBoard b) {
			board = b;
		}
// ---------------------------------------------Methods--------------	
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			touch.set(x, y, 0);
			Core.camera.unproject(touch);
			board.tdX = (int) touch.x/3;
			board.tdY = (int) touch.y/3;
			return true;
		}
		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			touch.set(x, y, 0);
			Core.camera.unproject(touch);
			board.tuX = (int) touch.x/3;
			board.tuY = (int) touch.y/3;
			return true;
		}
}

