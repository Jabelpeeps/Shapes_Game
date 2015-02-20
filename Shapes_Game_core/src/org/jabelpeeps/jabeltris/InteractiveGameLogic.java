package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;

public class InteractiveGameLogic extends GameLogic implements Runnable {

	private int c1x, c1y, c2x, c2y;
	private boolean candidatesAreSet = false;
	private boolean shuffleCalled = false;
	private boolean hintRequested = false;
	private boolean endlessPlayMode = false;

	public InteractiveGameLogic(PlayArea g) {
		super(g);
	}

	@Override
	public void run() {
		if ( !game.boardIsReadyForPlay() ) {
			game.setupBoard();
			game.fillBoard();
			game.setBoardReadyForPlay();
			game.swirlShapesIntoPlace();
		}
		Core.delay(60);
		while ( game.boardHasMatches(100) ) {		// clear the board of any pre-existing matches.
			game.replaceMatchedShapes();
		}
		game.findHintsOnBoard();
		            			       // From here, this is is the main loop of the game logic.
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
			while ( game.getHintListSize() <= 0 && endlessPlayMode ) {
				game.setMessage("Shuffling Board");
				Core.delay(100);
				game.shuffleBoard();
				game.findHintsOnBoard();
			}
			Core.delay(100);
		} while ( !game.level.IsFinished() && !backKeyWasPressed );
		
		Gdx.graphics.requestRendering();
		Core.delay(500);
		return;    								
	}							        // The end of the main logic loop.
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
	public void setEndlessPlayModeOn() {					// sets boolean to allow automatic shuffling
		endlessPlayMode = true;							// of the board
	}
    @Override
	public void setEndlessPlayModeOff() {
    	endlessPlayMode = false;
	}
}
