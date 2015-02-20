package org.jabelpeeps.jabeltris;

public abstract class GameLogic extends Thread {
	
	protected PlayArea game;
	protected boolean backKeyWasPressed = false;
	protected boolean startSignalSet = true;
	
	public GameLogic(PlayArea g) {		
		game = g;
		this.setDaemon(true);
	}

	public void setSwapCandidates(int xSaved, int ySaved, int dX, int dY) {
	}
	public boolean isReadyForNewCandidates() {
		return false;
	}
	public void setEndlessPlayMode(boolean mode) {
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
	public void waitForStartSignal() {
		startSignalSet = false;
	}
	public void sendStartSignal() {
		startSignalSet = true;
	}
}
