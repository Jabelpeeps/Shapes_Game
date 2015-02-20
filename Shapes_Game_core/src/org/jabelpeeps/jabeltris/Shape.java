package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public abstract class Shape extends Sprite {	
// -------------------------------------------------Field(s)---------
		protected String type;
		public static PlayArea game;
		protected static int x_offset;
		protected static int y_offset;
		
		private final Pool<Vector2> vector2Pool = new Pool<Vector2>(){
			    @Override
			    protected Vector2 newObject() {
			        return new Vector2();
			    }
		};
		private Vector2 savedXY = vector2Pool.obtain().set(0, 0);
		private Vector2 newXY = vector2Pool.obtain().set(0, 0);
		private Vector2 savedOrigin = vector2Pool.obtain().set(0,0);
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
					if ( game.getShape(each).type.equals(s.type) ) { 
						matchesNeeded--; 
					};
					vector2Pool.free(each);
				}
			} catch (ArrayIndexOutOfBoundsException e) {};
			
			if (  matchesNeeded == 0 ) return true;
			return false;
		}
		protected Vector2 v(int x, int y) {
			return vector2Pool.obtain().set(x, y);
		}
		public float checkMatch(int x, int y) {
			return shapeMatch(x, y, x, y);
		}
		public void addHintsToList() {
			addHintsToList( (int)getX(), (int)getY() );
		}
		public void addHintsToList(int x, int y) {
			if ( hintMatch(x, y) ) {
					game.addHint(this);
			}
		}
		boolean hintMatch(int x, int y) {
			boolean hintFound = false;
			if ( shapeMatch(x+1, y, x, y) > 0f ) hintFound = true;
			if ( shapeMatch(x-1, y, x, y) > 0f ) hintFound = true;
			if ( shapeMatch(x, y+1, x, y) > 0f ) hintFound = true;
			if ( shapeMatch(x, y-1, x, y) > 0f ) hintFound = true;
			return hintFound;
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
		protected abstract float shapeMatch(int x, int y, int xx, int yy);
		
		public abstract void select();
		
		public abstract void deselect();

		@Override
		public float getX() {
			return ( super.getX() - x_offset ) / 4 ;
		}
		@Override
		public float getY() {
			return ( super.getY() - y_offset ) / 4 ;
		}
		@Override
		public void setPosition(float x, float y) {
			super.setPosition( x * 4 + x_offset , y * 4 + y_offset );
		}
		@Override
		public void setOrigin(float x, float y) {      // sets origin in relation to the playArea rather than the Shape.
			super.setOrigin( ( x - getX() ) * 4 + 2 , ( y - getY() ) * 4 + 2 );
		}
		public void setOriginAndBounds(int x, int y) {
			setBounds( x * 4 + x_offset , y * 4 + y_offset , 4 , 4 );
			setOriginCenter();
			setScale(0.9f);
			setAlpha(0f);
		}
		public void saveXY() {
			savedXY.set( getX() , getY() );
		}
		public float getSavedX() {
			return savedXY.x;
		}
		public float getSavedY() {
			return savedXY.y;
		}
		public void setNewX(float x) {
			setNewXY( x , getY() );
		}
		public void setNewY(float y) {
			setNewXY( getX() , y );
		}
		public void setNewXY() {
			setNewXY( getX() , getY() );
		}
		public void setNewXY(float x, float y) {
			newXY.set( x , y );
		}
		public float getNewX() {
			return newXY.x;
		}
		public float getNewY() {
			return newXY.y;
		}
		public void saveOrigin() {
			savedOrigin.set( (getOriginX() - 2 - x_offset) / 4 , (getOriginY() - 2 - y_offset) / 4 );
		}
		public void saveOrigin(float x, float y) {
			savedOrigin.set(x, y);
		}
		public float getSavedOriginX() {
			return savedOrigin.x;
		}
		public float getSavedOriginY() {
			return savedOrigin.y;
		}
		@Override
		protected void finalize() {
			vector2Pool.free(savedXY);
			vector2Pool.free(newXY);
			vector2Pool.free(savedOrigin);
		}
	}




