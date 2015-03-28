package org.jabelpeeps.jabeltris;


public abstract class GameLogic extends Thread {
	
	protected PlayArea game;
	protected boolean backKeyWasPressed = false;
	protected boolean startSignalSet = true;
	protected boolean endlessPlayMode = false;
	protected boolean handOverBoard = false;
	protected boolean loopIsEnding = false;
	
	public GameLogic(PlayArea g) {		
		game = g;
		this.setDaemon(true);
	}

	public boolean getEndlessPlayMode() {
		return endlessPlayMode;
	}
	public void setEndlessPlayMode(boolean mode) {
	}
	public void shutDown() {
		loopIsEnding = true;
		startSignalSet = true;
	}
	public void setBackKeyWasPressed(boolean state) {
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
	public abstract void acceptVisitor(LogicVisitor visitor);
	
	public abstract boolean hasVisitor();
}
