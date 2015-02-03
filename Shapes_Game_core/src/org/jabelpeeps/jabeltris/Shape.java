package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

abstract class Shape extends Sprite {	
// -------------------------------------------------Field(s)---------
		String type;
		Color color;
		static GameBoard board;
		boolean matched = false;
												
//  ----------------------------------------------Methods--------------- 
		
		// the 'm' methods are called from the various shape objects
		// depending how many object make up their matching shape.
		
		protected boolean m2(int x1, int y1, int x2, int y2, Shape s) {
			try {
				if ( board.getShape(x1, y1).type.equals(s.type)
				  && board.getShape(x2, y2).type.equals(s.type) ) {
						board.getShape(x1, y1).matched = true;
						board.getShape(x2, y2).matched = true;
						s.matched = true;
						return true;
					}
			} catch (ArrayIndexOutOfBoundsException e) {};
			return false;
		}
		protected boolean m3(int x1, int y1, int x2, int y2, int x3, int y3, Shape s) {
			try {
				if ( board.getShape(x1, y1).type.equals(s.type)
				  && board.getShape(x2, y2).type.equals(s.type)
				  && board.getShape(x3, y3).type.equals(s.type) ) {
						board.getShape(x1, y1).matched = true;
						board.getShape(x2, y2).matched = true;
						board.getShape(x3, y3).matched = true;
						s.matched = true;
						return true;
					}
			} catch (ArrayIndexOutOfBoundsException e) {};
			return false;
		}
		protected boolean m4(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, Shape s) {
			try {
				if ( board.getShape(x1, y1).type.equals(s.type)
				  && board.getShape(x2, y2).type.equals(s.type)
				  && board.getShape(x3, y3).type.equals(s.type)
				  && board.getShape(x4, y4).type.equals(s.type) ) {
						board.getShape(x1, y1).matched = true;
						board.getShape(x2, y2).matched = true;
						board.getShape(x3, y3).matched = true;
						board.getShape(x4, y4).matched = true;
						s.matched = true;
						return true;
					}
			} catch (ArrayIndexOutOfBoundsException e) {};
			return false;
		}
		
		public void setCenter(int x, int y){
			setCenter(x*3+1.5f, y*3+1.5f);
		}
		
		abstract boolean checkMatch(int x, int y);
		
		abstract public void findHint();
	}
