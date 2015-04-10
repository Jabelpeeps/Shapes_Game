package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.utils.Array;

public interface GameMechanic {

	public void activate();
	public void deactivate();
	public void toggleActive();
	public int getNumOfHints();
	public Array<Shape> getHintList();
	public void clearHints();
	public int searchForMoves();
	public void takeMove();
}
