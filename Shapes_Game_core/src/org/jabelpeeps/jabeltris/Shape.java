package org.jabelpeeps.jabeltris;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public abstract class Shape extends SpritePlus implements Runnable, Serializable {	
// -------------------------------------------------Field(s)---------
		protected String type;
		protected String colorString = "_";
		protected Color selected;
		protected Color deselected;
		
		protected static Array<HintMethodVisitor> hintVisitorList = new Array<HintMethodVisitor>();
		protected boolean[][] views = {{false, false},{false, true},{true, true},{true, false}};
		protected int facing = 0;
		protected boolean needsFlipping = false;
		protected boolean animatable = false;
		private ScheduledFuture<?> animation;
		
		public static int mCalls = 0;
//  ----------------------------------------------Static Methods--------------- 
		public static void addHintVisitor(HintMethodVisitor visitor) {
			boolean methodAlreadyAdded = false;
			for ( HintMethodVisitor each : hintVisitorList ) {
				if ( each.equals(visitor) )
					methodAlreadyAdded = true;
			}
			if ( !methodAlreadyAdded )
				hintVisitorList.add(visitor);
		}
		public static void removeHintVisitor(HintMethodVisitor visitor) {
			for ( HintMethodVisitor each : hintVisitorList ) 
				if ( each.equals(visitor) )
					hintVisitorList.removeValue(visitor, true);
		}
		public static void clearHintVisitorList() {
			hintVisitorList.clear();
		}
//  ----------------------------------------------Instance Methods--------------- 

		/** The 'm' method is called from the various shape objects, to check for matches with their neighbours. */		
		protected boolean m(Coords... xy) {
			if ( Core.LOGGING ) mCalls++;
			
			int matchesNeeded = xy.length;
			for ( Coords each : xy ) {
				Shape tmpShape = game.getShape(each);
				if ( !tmpShape.matches(this) || tmpShape == this ) 
					break;					
				matchesNeeded-- ;
			}
			Coords.freeAll(xy);
			return ( matchesNeeded == 0 );
		}
		protected boolean matches(Shape other) {
			return type == other.type;
		}
		
		protected Shape animate() {
			facing = Core.rand.nextInt(11);
			long delay = (long) (( Core.rand.nextGaussian() + 5) / 4 );
			animation = Core.threadPool.scheduleWithFixedDelay(this, delay*1000, 1500, TimeUnit.MILLISECONDS);
			return this;
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
			setupColors(jsonData.getString("colour"));
		}
		public void setupColors(String color) {
			if ( !color.equals("_") ) {
				colorString = color;
				type = (type + color).intern();
				selected = Colors.get("DARK_" + color.toUpperCase());
				deselected = Colors.get(color.toUpperCase());
				deselect();
			}
		}
		public void setAnimation(boolean animate) {
			if ( animate && animatable ) 
				animate();
			else if ( animation != null ) 
				animation.cancel(false);
		}	
		protected Coords v(int x, int y) {
			return Coords.get(x, y);
		}
		public float checkMatch(int x, int y) {
			return isBlank() ? 0f 
							 : shapeMatch(x, y);
		}
		protected abstract float shapeMatch(int x, int y);
		protected abstract boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list);
		
		/** <p>Adds possible moves to the hintList based on the Shape's <b> currently displayed </b>
		 * position.</p><p> DO NOT call unless all Shapes are displayed at their correct places; use 
		 * {@link #addHintsToList(int, int)} instead.</p>*/
		public boolean addHintsToList() {
			return addHintsToList( getXi(), getYi() );
		}
		/** <p>Provides a safe way to add possible moves to the hintList when the ShapeTile Array is
		 * out of sync with the displayed positions of the Shapes.</p><p>  To achieve this, it needs to be 
		 * supplied with its current position in the Array.</p> */
		public boolean addHintsToList(int x, int y) {
			return ( !isBlank() && hintMatch(x, y) );
		}
		/** <p>This method needs to be supplied with the current position on the ShapeTile Array, as the
		 * values supplied by getX() & getY() cannot be relied upon in all circumstances. */
		private boolean hintMatch(int x, int y) {
			
			for ( HintMethodVisitor each : hintVisitorList ) 
				if ( each.greet(x, y, this) ) {
					return true;
				}
			return false; 
		}
		/** Return true if the Shape at the supplied coordinates returns true its {@link #isBlank()} call. */
		boolean isBlank(int x, int y) {
			return game.getShape(x, y).isBlank();
		}
		/** Returns false, unless overridden. (It is overridden in the Blank child class.) */
		protected boolean isBlank() {
			return false;
		}
		
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
