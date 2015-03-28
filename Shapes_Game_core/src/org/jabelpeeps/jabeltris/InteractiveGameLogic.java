package org.jabelpeeps.jabeltris;

public class InteractiveGameLogic extends GameLogic implements Runnable {

	private boolean visitorAccepted = false;
	private LogicVisitor visitor;

	public InteractiveGameLogic(PlayArea g) {
		super(g);
	}
	public InteractiveGameLogic(PlayArea g, boolean handOver) {
		super(g);
		handOverBoard = handOver;
	}
	@Override
	public void run() {
		if ( !game.playAreaIsReady() ) {
			game.setupBoard();
			game.fillBoard();
			game.setPlayAreaReady();
		}
		if ( !handOverBoard ) {
			game.swirlShapesIntoPlace();
		}
		while ( !startSignalSet ) {
			Core.delay(200);
		}
		while ( !loopIsEnding ) {
			
			synchronized( this ) {
				
				if ( visitorAccepted ) {
					visitor.greet();
					visitor = null;
					visitorAccepted = false;
				}
				if ( game.getHintListSize() <= 0 && endlessPlayMode ) {
					game.setMessage("No Moves Left, Shuffling");
					Core.delay(100);
					game.shuffleBoard();
				}
			}
			Core.delay(80);
		}
		return;    								
	}							        
// ----------------------------------------------------------------other Methods-------
	@Override
	public void setEndlessPlayMode(boolean mode) {		// sets field to allow automatic shuffling
		endlessPlayMode = mode;							// of the board
	}
	@Override
	public void acceptVisitor(LogicVisitor visitor) {
		visitorAccepted = true;
		this.visitor = visitor;
	}
	@Override
	public boolean hasVisitor() {
		return visitorAccepted;
	}
}
