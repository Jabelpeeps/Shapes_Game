package org.jabelpeeps.jabeltris;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;

public abstract class Shape extends Sprite implements Runnable, Serializable {	
// -------------------------------------------------Field(s)---------
		protected String type;
		protected PlayArea game;
		protected int x_offset;
		protected int y_offset;
		protected Color selected;
		protected Color deselected;
		
		private Pool<Vector2> vector2Pool = new Pool<Vector2>(){
			    @Override
			    protected Vector2 newObject() {
			        return new Vector2();
			    }
		};
		private Vector2 savedXY = vector2Pool.obtain().set(0, 0);
		private Vector2 newXY = vector2Pool.obtain().set(0, 0);
		private Vector2 savedOrigin = vector2Pool.obtain().set(0,0);
		
		protected boolean[][] views = {{false, false},{false, true},{true, true},{true, false}};
		protected int facing = 0;
		protected boolean needsFlipping = false;
		private ScheduledFuture<?> animation;
//  ----------------------------------------------Methods--------------- 
		protected void animate() {
			facing = Core.rand.nextInt(11);
			long delay = (long) (( Core.rand.nextGaussian() + 5) / 4 );
			animation = Core.threadPool.scheduleWithFixedDelay(this, delay*1000, 1500, TimeUnit.MILLISECONDS);
		}
		@Override
		public void run() {
			setRotation(30 * facing);
			
			if ( needsFlipping ) {
				setFlip( views[facing / 3][0] , views[facing / 3][1] );
			} 
			facing = ( facing == 11 ) ? 0 : facing + 1 ;
			Gdx.graphics.requestRendering();
		}
		
		@Override
		public void write (Json json) {
			json.writeValue("Shape", getClass().getName());
			json.writeValue("x", super.getX());
			json.writeValue("y", super.getY());
		}
		@Override
		public void read(Json json, JsonValue jsonData) {
			super.setX(jsonData.getFloat("x"));
			super.setY(jsonData.getFloat("y"));
			deselect();
		}
		
		public void setAnimation(boolean animate) {
			if ( animate ) {
				animate();
			} else {
				if ( animation != null ) {
					animation.cancel(false);
				}
			}
		}
	
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
			if ( isNotBlank(x, y) && hintMatch(x, y) ) {
					game.addHint(this);
			}
		}
		private boolean hintMatch(int x, int y) {
			boolean hintFound = false;
			if ( isNotBlank(x+1, y) && shapeMatch(x+1, y, x, y) > 0f ) hintFound = true;
			if ( isNotBlank(x-1, y) && shapeMatch(x-1, y, x, y) > 0f ) hintFound = true;
			if ( isNotBlank(x, y+1) && shapeMatch(x, y+1, x, y) > 0f ) hintFound = true;
			if ( isNotBlank(x, y-1) && shapeMatch(x, y-1, x, y) > 0f ) hintFound = true;
			return hintFound;
		}
		private boolean isNotBlank(int x, int y) {
			try {
				if ( game.getShape(x, y).type != "blank" ) return true;
			} catch (ArrayIndexOutOfBoundsException e) {}
			return false;
		}
		protected abstract float shapeMatch(int x, int y, int xx, int yy);
		
		public void select() {
			setColor(selected);
		}
		
		public void deselect() {
			setColor(deselected);
		}

		public Shape setOffsets(int x_off, int y_off) {
			x_offset = x_off;
			y_offset = y_off;
			return this;
		}
		public Shape setOffsets() {
			x_offset = game.getXoffset();
			y_offset = game.getYoffset();
			return this;
		}
		public Shape setPlayArea(PlayArea p) {
			game = p;
			if ( game == null ) {
				System.out.println("null PlayArea set in Shape.setPlayArea()");
			} 
			return this;
		}
		@Override
		public void setOrigin(float x, float y) {      // sets origin in relation to the playArea rather than the Shape.
			super.setOrigin( ( x - getX() ) * 4 + 2 , ( y - getY() ) * 4 + 2 );
		}
		public Shape setOriginAndBounds() {
			return setOriginAndBounds(getX(), getY());
		}
		public Shape setOriginAndBounds(float f, float g) {
			setBounds( f * 4 + x_offset , g * 4 + y_offset , 4 , 4 );
			setOriginCenter();
			setScale(0.9f);
			setAlpha(0f);
			return this;
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
		@Override
		protected void finalize() {
			vector2Pool.free(savedXY);
			vector2Pool.free(newXY);
			vector2Pool.free(savedOrigin);
		}
	}




