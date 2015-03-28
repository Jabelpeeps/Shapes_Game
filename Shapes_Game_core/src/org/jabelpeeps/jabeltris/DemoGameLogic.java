package org.jabelpeeps.jabeltris;


public class DemoGameLogic extends GameLogic implements Runnable {

	private final static int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
	
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
				takeBestMove(game);
				while ( game.boardHasMatches(150) ) 
					game.replaceMatchedShapes();
			}
			Core.delay(50);
			
			if ( game.findHintsOnBoard() <= 0 ) 
				game.shuffleBoard();
		}
		return;
	}
		
	public static void takeBestMove(PlayArea game) {
		Shape[] localHintList = game.getHintList();
		int bestMatches = 0;
		Shape bestShape = null;
		Shape bestWhich = null;
		
		for ( Shape eachShape : localHintList ) {
			
			for ( int[] whichWay : directions ) {
			
				Shape whichShape = game.getShape( eachShape.getXi() + whichWay[0] , eachShape.getYi() + whichWay[1] );
				
				if ( whichShape.isBlank() ) continue;
				
				game.shapeTileArraySwap( eachShape , whichShape );
				
				if ( game.boardHasMatches(0) && ( game.getMatchListSize() > bestMatches ) ) {
					bestMatches = game.getMatchListSize();
					bestShape = eachShape;
					bestWhich = whichShape;
				}					
				game.shapeTileArraySwap( eachShape , whichShape );
				game.clearMatchList();
			}
		}
		long time = (long) (( Core.rand.nextGaussian() + 1.5) * 150);
		Core.delay(time);
		
		game.blinkList(100, 5, bestShape , bestWhich );
		game.animateSwap( bestShape , bestWhich );
	}

	@Override
	public void acceptVisitor(LogicVisitor visitor) {
	}
	@Override
	public boolean hasVisitor() {
		return false;
	}
}
