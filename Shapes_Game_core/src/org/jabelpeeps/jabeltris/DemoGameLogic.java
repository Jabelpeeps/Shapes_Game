package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.math.Vector2;

public class DemoGameLogic extends GameLogic implements Runnable {

	private boolean demoIsFinished = false;
	private final Vector2 UP = new Vector2(0, 1); 
	private final Vector2 DOWN = new Vector2(0, -1);
	private final Vector2 LEFT = new Vector2(-1, 0);
	private final Vector2 RIGHT = new Vector2(1, 0);
	private Vector2[] directions = {UP, DOWN, LEFT, RIGHT};
	
	public DemoGameLogic(PlayArea g) {
		super(g);
	}
	@Override
	public void endDemo() {
		demoIsFinished = true;
	}
	@Override
	public void run() {
		int highestNumberOfMatches = 0;
		Shape mostMatchedShape = null;
		Vector2 bestMove = null;
		int bestX = 0, bestY = 0;
		Shape[] localHintList;
		
		if ( !game.boardIsReadyForPlay() ) {
			game.setupBoard();
			game.fillBoard();
			game.setBoardReadyForPlay();
			game.dropShapesIntoPlace();
		}
		Core.delay(60);
		do {
			while ( game.boardHasMatches(150) ) {
				game.replaceMatchedShapes();
			}
			if ( game.findHintsOnBoard() <= 0 ) {
				game.shuffleBoard();
			}		
			localHintList = game.getHintList();
			highestNumberOfMatches = 0;
			bestX = 0; 
			bestY = 0;
			for ( Shape eachShape : localHintList ) {
				int x = (int)eachShape.getX();
				int y = (int)eachShape.getY();
				for ( Vector2 whichWay : directions ) {
					try {		
						game.shapeTileArraySwap(x, y, x+(int)whichWay.x, y+(int)whichWay.y);
						if ( game.boardHasMatches(0) 
								&& ( game.getMatchListSize() > highestNumberOfMatches ) ) {
							highestNumberOfMatches = game.getMatchListSize();
							mostMatchedShape = eachShape;
							bestMove = whichWay;
							bestX = x;
							bestY = y;
						}
						game.shapeTileArraySwap(x, y, x+(int)whichWay.x, y+(int)whichWay.y);
					} catch (ArrayIndexOutOfBoundsException e) {}		
					game.clearMatchList();
				}
			}
			game.blinkList(100, 5, new Shape[]{ mostMatchedShape, game.getShape(bestX+(int)bestMove.x, bestY+(int)bestMove.y) } );
//			mostMatchedShape.blink(150, 4);
			game.animateSwap(bestX, bestY, bestX+(int)bestMove.x, bestY+(int)bestMove.y);
			
		} while ( !demoIsFinished );
		
		while ( game.boardHasMatches(100) ) {
			game.replaceMatchedShapes();
		}
		return;
	}
}
