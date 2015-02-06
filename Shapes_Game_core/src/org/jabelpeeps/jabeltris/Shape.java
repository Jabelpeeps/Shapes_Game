package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Shape extends Sprite {	
// -------------------------------------------------Field(s)---------
		protected String type;
		public static GameBoard board;
//  ----------------------------------------------Methods--------------- 
		
		// The 'm' methods are called from the various shape objects
		// depending how many objects make up their matching shape.
		//
		// The try/catch blocks are the quickest way to detect being
		// asked to search for matches with shapes that would be off 
		// the edge of the play area.
		
		protected boolean m1(int x1, int y1, Shape s) {
			try {
				if ( board.getShape(x1, y1).type.equals(s.type) ) {
						return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {};
			return false;
		}
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
		protected boolean m5(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int x5, int y5, Shape s) {
			try {
				if ( board.getShape(x1, y1).type.equals(s.type)
				  && board.getShape(x2, y2).type.equals(s.type)
				  && board.getShape(x3, y3).type.equals(s.type)
				  && board.getShape(x4, y4).type.equals(s.type)
				  && board.getShape(x5, y5).type.equals(s.type) ) {
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
		
		public boolean checkMatch() {
			int x = (int) getX();
			int y = (int) getY();
			return shapeMatch(x, y);
		}
		protected abstract boolean shapeMatch(int x, int y);  
		
		protected abstract boolean hintMatch(int x, int y);
		
		public void findHints() {
			int x = (int) getX();
			int y = (int) getY();
			if ( hintMatch(x, y) ) {
				board.hintList.add(this); 
				System.out.print("(" + x + ", " + y + ")");
			}
		}
	}
