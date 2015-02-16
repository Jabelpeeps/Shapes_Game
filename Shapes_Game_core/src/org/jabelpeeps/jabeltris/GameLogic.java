package org.jabelpeeps.jabeltris;

public abstract class GameLogic extends Thread {
	
	protected PlayArea game;
	
	public GameLogic(PlayArea g) {		
		game = g;
	}

	public boolean isReadyForNewCandidates() {
		return false;
	}
	public void setEndlessPlayModeOn() {
	}
	public void setSwapCandidates(int xSaved, int ySaved, int dX, int dY) {
	}
	public void setBackKeyPressed() {
	}
	public void setShuffle() {
	}
	public boolean hintHasBeenGiven() {
		return false;
	}
	public void requestHint() {
	}
	public void setEndlessPlayModeOff() {
	}
	public void endDemo() {
		
	}
}
