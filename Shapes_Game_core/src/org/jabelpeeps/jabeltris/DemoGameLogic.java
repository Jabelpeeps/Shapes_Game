package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DemoGameLogic implements Runnable {

	private GameLogic game;
	private boolean demoIsFinished = false;
	private final Vector2 UP = new Vector2(0, 1); 
	private final Vector2 DOWN = new Vector2(0, -1);
	private final Vector2 LEFT = new Vector2(-1, 0);
	private final Vector2 RIGHT = new Vector2(1, 0);
	private Vector2[] directions = {UP, DOWN, LEFT, RIGHT};
	private Array<Shape> localHintList = new Array<Shape>(false, 32, Shape.class);
	
	DemoGameLogic(GameLogic g) {
		game = g;
	}

	@Override
	public void run() {
		int highestNumberOfMatches;
		Shape bestHintShape;
		Vector2 bestMove = new Vector2(0, 0);
		int bestX = 0, bestY = 0;
		
		game.fillBoard();
		Core.delay(60);
		while ( game.boardHasMatches(300) ) {		// clear the board of any pre-existing matches.
			game.replaceMatchedShapes();
		} 
		game.findHintsOnBoard();
		do {
			localHintList = game.getHintList();
			localHintList.shuffle();
			highestNumberOfMatches = 0;
			bestHintShape = localHintList.first();
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
							bestHintShape = eachShape;
							bestMove = whichWay;
							bestX = x;
							bestY = y;
						}
						game.shapeTileArraySwap(x, y, x+(int)whichWay.x, y+(int)whichWay.y);
					} catch (ArrayIndexOutOfBoundsException e) {};
					game.clearMatchList();
				}	
			}
			bestHintShape.blink(100, 5);
			game.animateSwap(bestX, bestY, bestX+(int)bestMove.x, bestY+(int)bestMove.y);
			while ( game.boardHasMatches(150) ) game.replaceMatchedShapes();
			game.findHintsOnBoard();
			if ( game.getHintListSize() <= 0 ) {
				game.shuffleBoard();
				while ( game.boardHasMatches(200) ) game.replaceMatchedShapes();
				game.findHintsOnBoard();
			}
			Core.delay(200);
		} while ( !demoIsFinished );		
		return;
	}
	public void endDemo() {
		demoIsFinished = true;
	}
}
