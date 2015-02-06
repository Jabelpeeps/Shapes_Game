package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Shape extends Sprite {	
// -------------------------------------------------Field(s)---------
		protected String type;
		public static GameBoard board;												
//  ----------------------------------------------Methods--------------- 
		
		// the 'm' methods are called from the various shape objects
		// depending how many objects make up their matching shape.
		
		protected boolean m2(int x1, int y1, int x2, int y2, Shape s) {
			try {
				if ( board.getShape(x1, y1).type.equals(s.type)
				  && board.getShape(x2, y2).type.equals(s.type) ) {
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
						return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {};
			return false;
		}
		@Override
		public float getX() {
			return super.getX()/4;
		}
		@Override
		public float getY() {
			return super.getY()/4;
		}
		
		public void setCenter(int x, int y) {
			super.setCenter(x*4+2, y*4+2);
		}
		public abstract void select();
		
		public abstract void deselect();
		
		public abstract boolean checkMatch();
		
		abstract public void findHint();
	}
