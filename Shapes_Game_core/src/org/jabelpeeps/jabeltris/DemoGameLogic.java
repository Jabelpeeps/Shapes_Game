package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DemoGameLogic extends GameLogic implements Runnable {

	private boolean demoIsFinished = false;
	private final Vector2 UP = new Vector2(0, 1); 
	private final Vector2 DOWN = new Vector2(0, -1);
	private final Vector2 LEFT = new Vector2(-1, 0);
	private final Vector2 RIGHT = new Vector2(1, 0);
	private Vector2[] directions = {UP, DOWN, LEFT, RIGHT};
	private Array<Shape> localHintList = new Array<Shape>(false, 32, Shape.class);
	
	public DemoGameLogic(PlayArea g) {
		super(g);
	}
	@Override
	public void endDemo() {
		demoIsFinished = true;
	}
	@Override
	public void setBackKeyPressed() {
		endDemo();
	}

	@Override
	public void run() {
		int highestNumberOfMatches;
		Shape mostMatchedShape;
		Vector2 bestMove = new Vector2(0, 0);
		int bestX = 0, bestY = 0;
		
		if ( !game.boardIsAlreadySet() ) {
			game.setupBoard();
			game.fillBoard();
			game.dropShapesIntoPlace();
			game.setBoardReadyForPlay();
		}
		Core.delay(60);
		do {
			while ( game.boardHasMatches(200) ) {
				game.replaceMatchedShapes();
			}
			game.findHintsOnBoard();
			while ( game.getHintListSize() <= 0 ) {
				game.shuffleBoard();
				game.findHintsOnBoard();
			}			
			localHintList = game.getHintList();
			localHintList.shuffle();
			highestNumberOfMatches = 0;
			mostMatchedShape = localHintList.first();
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
					} catch (ArrayIndexOutOfBoundsException e) {};
					game.clearMatchList();
				}	
			}
			mostMatchedShape.blink(100, 4);
			game.animateSwap(bestX, bestY, bestX+(int)bestMove.x, bestY+(int)bestMove.y);
			
		} while ( !demoIsFinished );	
		return;
	}
}
