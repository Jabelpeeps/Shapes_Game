package org.jabelpeeps.jabeltris;

public abstract class GameLogic extends Thread {
	
	protected PlayArea game;
	protected boolean backKeyWasPressed = false;
	
	public GameLogic(PlayArea g) {		
		game = g;
	}

	public void setSwapCandidates(int xSaved, int ySaved, int dX, int dY) {
	}
	public boolean isReadyForNewCandidates() {
		return false;
	}
	public void setEndlessPlayModeOn() {
	}
	public void setEndlessPlayModeOff() {
	}
	public void setShuffle() {
	}
	public void requestHint() {
	}
	public boolean hintHasBeenGiven() {
		return false;
	}
	public void endDemo() {
	}
	public void setBackKeyPressed(boolean state) {
		backKeyWasPressed = state;
	}
	public boolean getBackKeyWasPressed() {
		return backKeyWasPressed;
	}
}
