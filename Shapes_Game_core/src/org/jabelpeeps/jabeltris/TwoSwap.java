package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.utils.Array;

public class TwoSwap implements GameMechanic {
	
	private PlayArea game;
	@SuppressWarnings("unused")
	private GameLogic logic;
	private HintMethodVisitor visitor = new StandardMoveHints();
	private Array<Shape> hintList = new Array<Shape>(false, 32, Shape.class);
	private Shape firstShape, otherShape; 
	private boolean active = false;
	
	public TwoSwap(PlayArea gam, GameLogic logi) {
		game = gam;
		logic = logi;
	}
	
	class StandardMoveHints implements HintMethodVisitor {
		@Override
		public boolean greet(int x, int y, Shape s) {
			
			if (	   ( !s.isBlank(x+1, y) && s.shapeMatch(x+1, y) > 0f ) 
					|| ( !s.isBlank(x, y+1) && s.shapeMatch(x, y+1) > 0f ) 
					|| ( !s.isBlank(x-1, y) && s.shapeMatch(x-1, y) > 0f ) 
					|| ( !s.isBlank(x, y-1) && s.shapeMatch(x, y-1) > 0f ) ) {
				hintList.add(s);
				return true;
			}			
			return false;
		}
	}	
	
	@Override
	public int searchForMoves() {
		if ( !active || hintList.size < 1 ) return 0;	
		
		int highestMatches = 0;
		firstShape = null;
		otherShape = null;
		
		for ( Shape eachShape : hintList ) {
			int x = eachShape.getXi();
			int y = eachShape.getYi();
			
			for ( int[] whichWay : Core.LEFT_UP_RIGHT_DOWN ) {
				Shape whichShape = game.getShape( x + whichWay[0] , y + whichWay[1] );
				
				if ( whichShape.isBlank() ) continue;
				
				game.shapeTileArraySwap( eachShape , whichShape );
				
				if ( game.boardHasMatches(0) && ( game.getMatchListSize() > highestMatches ) ) {
					highestMatches = game.getMatchListSize();
					firstShape = eachShape;
					otherShape = whichShape;
				}					
				game.shapeTileArraySwap( eachShape , whichShape );
				game.clearMatchList();
			}
		}
		return highestMatches;
	}

	@Override
	public void takeMove() {	
		game.blinkList(100, 5, firstShape , otherShape );
		game.animateSwap( firstShape , otherShape );
	}
	
	@Override
	public void activate() {
		Shape.addHintVisitor( visitor );
		active = true;
	}
	@Override
	public void deactivate() {
		active = false;
		Shape.removeHintVisitor( visitor );
	}
	@Override
	public void toggleActive() {
		if ( active )
			deactivate();
		else
			activate();
	}
	@Override
	public int getNumOfHints() {
		return hintList.size;
	}
	@Override
	public Array<Shape> getHintList() {
		return hintList;
	}
	@Override
	public void clearHints() {
		hintList.clear();
	}
}
