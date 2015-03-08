package org.jabelpeeps.jabeltris;


public class InteractiveGameLogic extends GameLogic implements Runnable {

	private int c1x, c1y, c2x, c2y;
	private boolean candidatesAreSet = false;
	private boolean shuffleCalled = false;
	private boolean hintRequested = false;

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
		
		while ( !startSignalSet ) {
			Core.delay(200);
		}
		if ( !handOverBoard ) {
			game.swirlShapesIntoPlace();
		}
		
		do {
			if ( hintRequested ) {
				game.getHintList()[0].blink(150, 3);
				hintRequested = false;
			}
			if ( candidatesAreSet ) {
				game.doSwapIfSwapable(c1x, c1y, c2x, c2y);
				candidatesAreSet = false;
			}
			if ( shuffleCalled ) {
				game.shuffleBoard();
				shuffleCalled  = false;	
			}
			if ( game.getHintListSize() <= 0 && endlessPlayMode ) {
				game.setMessage("No Moves Left, Shuffling");
				Core.delay(100);
				game.shuffleBoard();
			}
			Core.delay(100);
			
		} while ( !game.level.IsFinished() && !backKeyWasPressed );
		
		return;    								
	}							        
// ----------------------------------------------------------------other Methods-------
	@Override
	public void setSwapCandidates(int x1, int y1, int x2, int y2) {
		c1x = x1;										// used by PlayAreaInput to hand in sets
		c1y = y1;										// of coordinates, for testing,
		c2x = x2;										// to this class in a thread-safe way.
		c2y = y2;
		candidatesAreSet = true;
	}
	@Override
	public boolean isReadyForNewCandidates() {			// second part of above.
		return !candidatesAreSet;
	}
	@Override
	public boolean hintHasBeenGiven() {					// currently used by BorderButtonsInput
		return hintRequested;							// for debugging, may be used for other
	}													// purposes in future.
	@Override											//		
	public void requestHint() {							//
		hintRequested = true;							//
	}													//
	@Override											//
	public void setShuffle() {							//
		shuffleCalled = true;							//
	}
	@Override
	public void setEndlessPlayMode(boolean mode) {		// sets field to allow automatic shuffling
		endlessPlayMode = mode;							// of the board
	}
}
