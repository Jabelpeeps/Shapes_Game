package org.jabelpeeps.jabeltris;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public abstract class Shape extends SpritePlus implements Runnable, Serializable {	
// -------------------------------------------------Field(s)---------
		protected String type;
		protected String colorString = "";
		protected Color selected;
		protected Color deselected;
		
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
			
			if ( needsFlipping ) 
				setFlip( views[facing / 3][0] , views[facing / 3][1] );
			
			facing = ( facing == 11 ) ? 0 : facing + 1 ;
			Gdx.graphics.requestRendering();
		}
		
		@Override
		public void write (Json json) {
			json.writeValue("Shape", getClass().getSimpleName());
			json.writeValue("x", getX());
			json.writeValue("y", getY());
			json.writeValue("colour", colorString);
		}
		@Override
		public void read(Json json, JsonValue jsonData) {
			setX(jsonData.getFloat("x"));
			setY(jsonData.getFloat("y"));
			setupReadColors(jsonData.getString("colour"));
			deselect();
		}
		protected void setupReadColors(String color) {
			// stub method needs overriding in shapes with variable colours.
		}
		
		public void setupColors(String color) {
			colorString = color;
			type = (type + color).intern();
			selected = Colors.get("DARK_" + color.toUpperCase());
			deselected = Colors.get(color.toUpperCase());
		}
		public void setAnimation(boolean animate) {
			if ( animate ) 
				animate();
			else if ( animation != null ) 
				animation.cancel(false);
		}
		
		/** 
		 * The 'm' method is called from the various shape objects, 
		 * to check for matches with their neighbours.
		 *
		 */		
		protected boolean m(Shape s, Coords... xy) {
			int matchesNeeded = xy.length;

			for ( Coords each : xy ) {
				if ( game.getShape(each).type.equals(s.type) ) 
					matchesNeeded-- ; 
				each.free();
			}
			return ( matchesNeeded == 0 );
		}
		
		protected Coords v(int x, int y) {
			return Coords.get(x, y);
		}
		
		public float checkMatch(int x, int y) {
			return isBlank() ? 0f 
							 : shapeMatch(x, y, x, y);
		}
		public void addHintsToList() {
			addHintsToList( getXi(), getYi() );
		}
		public void addHintsToList(int x, int y) {
			if ( !isBlank(x, y) && hintMatch(x, y) ) 
					game.addHint(this);
		}
		private boolean hintMatch(int x, int y) {
			
			if ( !isBlank(x+1, y) && shapeMatch(x+1, y, x, y) > 0f ) return true;
			if ( !isBlank(x-1, y) && shapeMatch(x-1, y, x, y) > 0f ) return true;
			if ( !isBlank(x, y+1) && shapeMatch(x, y+1, x, y) > 0f ) return true;
			if ( !isBlank(x, y-1) && shapeMatch(x, y-1, x, y) > 0f ) return true;
			return false;
		}
		// This method is private as it is used to test if other Shapes are blanks.
		private boolean isBlank(int x, int y) {
				return game.getShape(x, y).isBlank();
		}
		// This public method provides the response to the previous call.  (It is Overridden in the Blank child class.)
		public boolean isBlank() {
			return false;
		}
		protected abstract float shapeMatch(int x, int y, int xx, int yy);
		
		public Shape select() {
			setColor(selected);
			return this;
		}
		public Shape deselect() {
			setColor(deselected);
			return this;
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
	}
