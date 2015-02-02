package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

abstract class Shape extends Core {	
	// -------------------------------------------------Field(s)---------
			Sprite tile;
			Shape type;
			Color color;
			static GameBoard board;
			boolean matched = false;
			
	//  ----------------------------------------------Methods--------------- 
			public Shape makeNew(int ini_x, int ini_y) {
				
				int option = rand.nextInt(5) + 1;
				switch (option) {
					case 1:                       // line
						type = new Line();
						break;
					case 2:                       // crossOne ('+')
						type = new CrossOne();
						break;
					case 3:                       // crossTwo ('x')
						type = new CrossTwo();				
						break;
					case 4:                       // triangle
						type = new Triangle();				
						break;
					case 5:                       // square
						type = new Square();				
						break;
					default:    // should never be used, but stops errors in IDE.
						type = null;
						break;
				}
				type.tile.setOriginCenter();
				type.tile.setSize(2.5f, 2.5f);
				return type;
			}
				
			// the 'm' methods are called from the various shape objects, depending on their shape.
			protected boolean m2(int x1, int y1, int x2, int y2) {
				try {
					if ( board.getShape(x1, y1).getClass().isInstance(this)
					  && board.getShape(x2, y2).getClass().isInstance(this) ) {
							board.getShape(x1, y1).matched = true;
							board.getShape(x2, y2).matched = true;
							return true;
						}
				} catch (ArrayIndexOutOfBoundsException e) {};
				return false;
			}
			protected boolean m3(int x1, int y1, int x2, int y2, int x3, int y3) {
				try {
					if ( board.getShape(x1, y1).getClass().isInstance(this)
					  && board.getShape(x2, y2).getClass().isInstance(this)
					  && board.getShape(x3, y3).getClass().isInstance(this) ) {
							board.getShape(x1, y1).matched = true;
							board.getShape(x2, y2).matched = true;
							board.getShape(x3, y3).matched = true;
							return true;
						}
				} catch (ArrayIndexOutOfBoundsException e) {};
				return false;
			}
			protected boolean m4(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
				try {
					if ( board.getShape(x1, y1).getClass().isInstance(this)
					  && board.getShape(x2, y2).getClass().isInstance(this)
					  && board.getShape(x3, y3).getClass().isInstance(this)
					  && board.getShape(x4, y4).getClass().isInstance(this) ) {
							board.getShape(x1, y1).matched = true;
							board.getShape(x2, y2).matched = true;
							board.getShape(x3, y3).matched = true;
							board.getShape(x4, y4).matched = true;
							return true;
						}
				} catch (ArrayIndexOutOfBoundsException e) {};
				return false;
			}
			
			void clearShape() {
				// TODO method to remove shapes that have 'matched', along with the other shapes in the match.
			}

			abstract boolean checkMatch(int x, int y, GameBoard p);
		}
