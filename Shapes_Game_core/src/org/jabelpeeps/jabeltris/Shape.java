package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class Shape extends Sprite {	
// -------------------------------------------------Field(s)---------
		protected String type;
		public static GameLogic logic;
		private Vector2 oldXY = new Vector2();
//  ----------------------------------------------Methods--------------- 
		
		// The 'm' method is called from the various shape objects, 
		// to check for matches with their neighbours.
		//
		// The try/catch blocks are the quickest way to exclude matches
		// with shapes that would be off the edge of the play area.
		//
		// As the checking of matches gets through many Vector2 objects, 
		// they are provided from a pool, and reused (in method 'v').
		
		protected boolean m(Shape s, Vector2... xy) {
			int matchesNeeded = xy.length;
			
			try {		
				for ( Vector2 each : xy ) {
					if ( logic.getShape(each).type.equals(s.type) ) { 
						matchesNeeded--; 
					}
					Core.vector2Pool.free(each);
				}
			} catch (ArrayIndexOutOfBoundsException e) {};
			
			if (  matchesNeeded == 0 ) return true;
			return false;
		}
		protected Vector2 v(int x, int y) {
			return Core.obtainVectorFromPool(x, y);
		}
		@Override
		public float getX() {
			return super.getX()/4;
		}
		@Override
		public float getY() {
			return super.getY()/4;
		}
		@Override
		public void setPosition(float x, float y) {
			super.setPosition(x*4, y*4);
		}
		public void saveXY() {
			oldXY.set( getX(), getY() );
		}
		public int getSavedX() {
			return (int) oldXY.x;
		}
		public int getSavedY() {
			return (int) oldXY.y;
		}
		public float checkMatch(int x, int y) {
			return shapeMatch(x, y, x, y);
		}
		public void addHintsToHintList() {
			if ( hintMatch( (int)getX(), (int)getY() ) ) {
					logic.addHint(this);
			}
		}
		public void blink(long time, int repeats) {
			for ( int i = 1; i <= repeats; i++) {
				select();
				Gdx.graphics.requestRendering();
				Core.delay(time);
				deselect();
				Gdx.graphics.requestRendering();
				Core.delay(time);
				}
		}
		protected boolean hintMatch(int x, int y) {
			boolean hintFound = false;
			if ( shapeMatch(x+1, y, x, y) > 0f ) hintFound = true;
			if ( shapeMatch(x-1, y, x, y) > 0f ) hintFound = true;
			if ( shapeMatch(x, y+1, x, y) > 0f ) hintFound = true;
			if ( shapeMatch(x, y-1, x, y) > 0f ) hintFound = true;
			return hintFound;
		}
		protected abstract float shapeMatch(int x, int y, int xx, int yy);
		
		public abstract void select();
		
		public abstract void deselect();
		
	}
