package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DemoGameLogic implements Runnable {

	private GameLogic game;
	boolean levelIsFinished = false;
	private final Vector2 UP = new Vector2(0, 1); 
	private final Vector2 DOWN = new Vector2(0, -1);
	private final Vector2 LEFT = new Vector2(-1, 0);
	private final Vector2 RIGHT = new Vector2(1, 0);
	private Array<Vector2> directions = new Array<Vector2>(false, 4, Vector2.class) ;
	
	DemoGameLogic(GameLogic g) {
		game = g;
		directions.addAll(UP, DOWN, LEFT, RIGHT);
	}

	@Override
	public void run() {
		game.fillBoard();
		Core.delay(60);
		while ( game.boardHasMatches(100) ) {		// clear the board of any pre-existing matches.
			game.replaceMatchedShapes();
		} 
		game.findHintsOnBoard();
		do {
			game.hintList.shuffle();
			Shape first = game.hintList.first();
			first.select();
			int x = (int)first.getX();
			int y = (int)first.getY();
			directions.shuffle();
			for ( Vector2 each : directions ) {
				try {
					game.selectAndSwap(x, y, x+(int)each.x, y+(int)each.y);
					if ( game.boardHasMatches(0) ) { 
						break;
					} else {
						game.selectAndSwap(x, y, x+(int)each.x, y+(int)each.y);
					}
				} catch (ArrayIndexOutOfBoundsException e) {};				
			} 
			while ( game.boardHasMatches(200) ) {
				game.replaceMatchedShapes();
			}
			Core.delay(200);
			game.findHintsOnBoard();
			if ( game.hintList.size <= 0 ) {
				game.setMessage("Shuffling Board");
				Core.delay(300);
				game.shuffleBoard();
				game.findHintsOnBoard();
			}			
		} while ( !levelIsFinished );		
		return;
	}
}
