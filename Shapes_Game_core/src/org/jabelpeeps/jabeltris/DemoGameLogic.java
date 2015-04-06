package org.jabelpeeps.jabeltris;


public class DemoGameLogic extends GameLogic implements Runnable {

	public DemoGameLogic(PlayArea g) {
		super(g);
	}
	
	@Override
	public void run() {
		if ( !game.playAreaIsReady() ) {
			game.setupBoard();
			game.fillBoard();
			game.setPlayAreaReady();
			game.spinShapesIntoPlace();
		}
		while ( !startSignalSet ) {
			Core.delay(200);
		}
		while ( !loopIsEnding ) {
			
			synchronized( this ) {
				takeBest4SwapMove();
				while ( game.boardHasMatches(150) ) 
					game.replaceMatchedShapes();
			}
			Core.delay(50);
			
			if ( game.findHintsOnBoard() <= 0 ) 
				game.shuffleBoard();
		}
		return;
	}
	@Override
	public boolean hasVisitor() {
		return true;
	}
}
