package org.jabelpeeps.jabeltris;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

public class GameBoard extends Core implements Screen {

	// ---------------------------------------------Field(s)------------
		final Core game;
		private Shape[][] playboard;
		static int x_size, y_size;
		final GameBoard gameboard;
		private Vector3 touch = new Vector3();
		static int firstX = -1, firstY = -1, tdX, tdY, tuX, tuY;
		static Shape shape = new Line();
		
	// ---------------------------------------------Constructors--------	
			
		public GameBoard(final Core g) {
			this(10,10, g);
		}
		// two constructors allows for different sizes boards in future.
		public GameBoard(int x, int y, final Core g) {
			game = g;
			x_size = x;
			y_size = y;
			playboard = new Shape[x][y];
			gameboard = this;
			
			fillBoard();
		
			// turn off continuous rendering (to save battery on android) 
		//	Gdx.graphics.setContinuousRendering(false);
					
			// setup the actions to respond to user input.
			Gdx.input.setInputProcessor(new InputAdapter() {
				
			// remains of constructor is only the anonymous class from above.	
			// -------------------start of anonymous class for handling input
				@Override
				public boolean touchDown(int x, int y, int pointer, int button) {
					touch.set(x, y, 0);
					game.camera.unproject(touch);
					tdX = (int)(touch.x / 3);
					tdY = (int)(touch.y / 3);
					
					// end event if touch was out of bounds 
					if ( outOfBounds(tdX, tdY) ) {
							deselect(firstX, firstY);
							firstX = -1;
							firstY = -1;
							renderBoard();
							return true;
					}
					// resets if this is second touch on same tile.
					if ( firstX == tdX && firstY == tdY ) {
							deselect(firstX, firstY);
							firstX = -1;
							firstY = -1;
							renderBoard();
							return true;
					}
					// runs when no tile selected.
					if ( firstX == -1 && firstY == -1 ) {
							firstX = tdX;
							firstY = tdY;
							select(firstX, firstY);
							renderBoard();
							return true;
					}
					// runs if touch is not adjacent to selected tile.
					if ( !touching(firstX, firstY, tdX, tdY)  ) {
							deselect(firstX, firstY);
							firstX = tdX;
							firstY = tdY;
							select(firstX, firstY);
							renderBoard();
							return true;
					}
					// runs if none of the above conditions are true.
					select(tdX, tdY);
					renderBoard();
					try {	TimeUnit.MILLISECONDS.sleep(300);
					} catch (InterruptedException e) { e.printStackTrace(); }
					doSwapIfSwapable(firstX, firstY, tdX, tdY);
					return true;
				}
				
				@Override
				public boolean touchUp(int x, int y, int pointer, int button) {
					touch.set(x, y, 0);
					game.camera.unproject(touch);
					tuX = (int)(touch.x / 3);
					tuY = (int)(touch.y / 3);
					
					// end event if touch released where started (should leave tile selected by touchDown)
					if ( tuX == firstX && tuY == firstY ) return true;
					
					// end event if touch released out of bounds
					if ( outOfBounds(tuX, tuY) ) return true;
					
					// end event if touch released too far from first tile.
					if ( !touching(firstX, firstY, tuX, tuY) ) { 
							deselect(firstX, firstY);
							firstX = -1;
							firstY = -1;
							renderBoard();
							return true;
					}
		//			select(tuX, tuY);
		//			renderBoard();
		//			try {  TimeUnit.MILLISECONDS.sleep(300);
		//			} catch (InterruptedException e) { e.printStackTrace();	}
					doSwapIfSwapable(firstX, firstY, tuX, tuY);
					return true;
				}
				private boolean outOfBounds(int x, int y) {
					if ( x < 0 || y < 0 || x > 9 || y > 9 ) {
						return true;
					} else return false;
				}
				// Swaps shapes, tests for matches, and swaps the shapes back if no match found.
				private void doSwapIfSwapable(int x1, int y1, int x2, int y2) {
					boolean matchfound = false;
					swap(x1, y1, x2, y2);
					if ( playboard[x1][y1].checkMatch(x1, y1, gameboard) ) {
							playboard[x1][y1].matched = true;
							matchfound = true;
					}
					if ( playboard[x2][y2].checkMatch(x2, y2, gameboard) ) {
							playboard[x2][y2].matched = true;
							matchfound = true;
					}
					firstX = -1;
					firstY = -1;
					if ( matchfound ) {
							deselect(x1, y1);
							deselect(x2, y2);
							clearMatched();
							fillBoard();
					} else {
							swap(x1, y1, x2, y2);
							renderBoard();
					}
				}
				private void swap(int x1, int y1, int x2, int y2) {
		//			System.out.println("swap called with:- ("+x1+", "+y1+")("+x2+", "+y2+")");
					select(x1, y1);
					select(x2, y2);
					renderBoard();
					if ( x1 == x2 && y1 < y2 ) {
		//				System.out.println("passed x1==x2, y1<y2");
						for ( float i=1.5f; i==4.5f; i+=0.5f) {
		//					System.out.printf("%.2d", i);
							playboard[x1][y1].tile.setCenterY(y1*3 + i);
							playboard[x2][y2].tile.setCenterY((y2+1)*3 - i);
							renderBoard();
						}
					} else if ( x1 == x2 && y1 > y2 ) {
		//				System.out.println("passed x1==x2, y1>y2");
						for ( float i=1.5f; i==4.5f; i+=0.5f) {
		//					System.out.printf("%.2d", i);
							playboard[x1][y1].tile.setCenterY((y1+1)*3 - i);
							playboard[x2][y2].tile.setCenterY(y2*3 + i);
							renderBoard();
						}
					} else if ( y1 == y2 && x1 < x2 ) {
		//				System.out.println("passed x1<x2, y1==y2");
						for ( float i=1.5f; i==4.5f; i+=0.5f) {
		//					System.out.printf("%.2d", i);
							playboard[x1][y1].tile.setCenterX(x1*3 + i);
							playboard[x2][y2].tile.setCenterX((x2+1)*3 - i);
							renderBoard();
						}
					} else {
		//				System.out.println("defaulted to x1>x2, y1==y2");
						for ( float i=1.5f; i==4.5f; i+=0.5f) {
		//					System.out.printf("%.2d", i);
							playboard[x1][y1].tile.setCenterX((x1+1)*3 - i);
							playboard[x2][y2].tile.setCenterX(x2*3 + i);
							renderBoard();
						}
					}
		//			System.out.println(" ");
					deselect(x1, y1);
					deselect(x2, y2);
					renderBoard();
					Shape tmpShape = playboard[x1][y1];
					playboard[x1][y1] = playboard[x2][y2];
					playboard[x2][y2] = tmpShape;
				}
				private boolean touching(int x1, int y1, int x2, int y2) {
					if ( (y1 == y2 && (x1+1 == x2 || x1-1 == x2) ) 
					  || (x1 == x2 && (y1+1 == y2 || y1-1 == y2) ) ) {
							return true;
					} else return false;
				}	
				private void select(int tx, int ty) {
					if (tx == -1 || ty == -1) return;
					Shape tmpShape = playboard[tx][ty];
					tmpShape.tile.setColor(tmpShape.color);
				}
				
				private void deselect(int tx, int ty) { 
					if (tx == -1 || ty == -1) return;
					Shape tmpShape = playboard[tx][ty];
					tmpShape.tile.setColor(1f, 1f, 1f, 1f);
				}
			}); // -------------------------------end of anonymous Class-----------
		}
//-------------------------------------------Methods of GameBoard Class-------
		private void fillBoard() {
			// TODO (maybe) change this method to drop Shapes from the top.
			do {
				for (int i=0; i<=9; i++) {
			    	for (int j=0; j<=9; j++) {
			    		if ( playboard[i][j] == null ) {
				    			playboard[i][j] = shape.makeNew(i, j);
						    	playboard[i][j].tile.setCenter(i*3+1.5f, j*3+1.5f);
			    		}
			    	}         
			    }             
				try { TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) { e.printStackTrace();	}
				renderBoard();
			} while ( boardHasMatches() );
		}
		
		private boolean boardHasMatches() {
			boolean matchesfound = false;
			for (int i=0; i<=9; i++) {
			    	for (int j=0; j<=9; j++) {
			    		if ( playboard[i][j].checkMatch(i, j, gameboard) ) {
			    				matchesfound = true;
			    		}
			    	}         
			    }             
			clearMatched();
			return matchesfound;
		}
		
		private void clearMatched() {
			for (int i=0; i<=9; i++) {
				for (int j=0; j<=9; j++) {
					if (playboard[i][j].matched) {
							playboard[i][j] = null;
					}
				}
			}
		}
			
		private void renderBoard() {
			try {  
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		    Gdx.graphics.requestRendering();
		}
		@Override
		public void render(float delta) {
			// clear screen and update camera
		    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			game.camera.update();
			game.batch.setProjectionMatrix(game.camera.combined);
			
			game.batch.begin();
		    background.draw(game.batch);
		    
		    for( int i=0; i<=9; i++ ) {
		    	for( int j=0; j<=9; j++ ) {
			    	playboard[i][j].tile.setCenter(i*3+1.5f, j*3+1.5f);
			    	playboard[i][j].tile.draw(game.batch);
			    	
		    	}           
		    	game.batch.flush();
		    }               
		    game.batch.end();
		}
		
		Shape getShape(int x, int y) {
			return playboard[x][y];
		}
		
		void drop() {
			// TODO method to handle the moving of shapes into empty spaces 
		}
		
		void clear() {
			// TODO method to clear board when level finished
		}
			
//-------------------------------------------------Empty Methods--------
		
		// main method for testing
		public static void main(String[] args) {
		}
		@Override
		public void show() {
		}
		@Override
		public void hide() {
		}
		@Override
		public void dispose() {
		}

//----------------------------------------------End-of-Class--------
}		
