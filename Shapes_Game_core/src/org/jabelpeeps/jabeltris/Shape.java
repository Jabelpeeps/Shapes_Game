package org.jabelpeeps.jabeltris;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jabelpeeps.jabeltris.Core.HintVisitor;

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
		
		protected static Array<HintVisitor> hintVisitorList = new Array<HintVisitor>();
		protected boolean[][] views = {{false, false},{false, true},{true, true},{true, false}};
		protected int facing = 0;
		protected boolean needsFlipping = false;
		protected boolean animatable = false;
		private ScheduledFuture<?> animation;
		
		public static int mCalls = 0;
//  ----------------------------------------------Static Methods--------------- 
		public static void addHintVisitor(HintVisitor visitor) {
			boolean methodAlreadyAdded = false;
			for ( HintVisitor each : hintVisitorList ) {
				if ( each.equals( visitor ) )
					methodAlreadyAdded = true;
			}
			if ( !methodAlreadyAdded )
				hintVisitorList.add( visitor );
		}
		public static void removeHintVisitor(HintVisitor visitor) {
			for ( HintVisitor each : hintVisitorList ) 
				if ( each.equals( visitor ) )
					hintVisitorList.removeValue( visitor, true );
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
				Shape tmpShape = game.getShape( each );
				if ( !tmpShape.matches( this ) || tmpShape == this ) 
					break;					
				matchesNeeded-- ;
			}
			Coords.freeAll( xy );
			return ( matchesNeeded == 0 );
		}
		protected boolean matches(Shape other) {
			return type == other.type;
		}
		
		protected Shape animate() {
			facing = Core.rand.nextInt(11);
			long delay = (long) ( ( Core.rand.nextGaussian() + 5 ) / 4 );
			animation = Core.threadPool.scheduleWithFixedDelay( this , delay*1000 , 1500 , TimeUnit.MILLISECONDS );
			return this;
		}
		@Override
		public void run() {
			setRotation( 30 * facing );
			
			if ( needsFlipping ) 
				setFlip( views[facing / 3][0] , views[facing / 3][1] );
			
			facing = ( facing == 11 ) ? 0 : facing + 1 ;
			Gdx.graphics.requestRendering();
		}
		
		@Override
		public void write (Json json) {
			json.writeValue( "Shape" , getClass().getSimpleName() );
			json.writeValue( "x" , getX() );
			json.writeValue( "y" , getY() );
			json.writeValue( "colour" , colorString );
		}
		@Override
		public void read(Json json, JsonValue jsonData) {
			setX( jsonData.getFloat( "x" ) );
			setY( jsonData.getFloat( "y" ) );
			setupColors( jsonData.getString( "colour" ) );
		}
		public void setupColors(String color) {
			if ( !color.equals( "_" ) ) {
				colorString = color;
				type = ( type + color ).intern();
				selected = Colors.get( ( "LIGHT_" + color.toUpperCase() ).intern() );
				deselected = Colors.get( color.toUpperCase().intern() );
				deselect();
			}
		}
		public void setAnimation(boolean animate) {
			if ( animate && animatable ) 
				animate();
			else if ( animation != null ) 
				animation.cancel( false );
		}	
		protected Coords v(int x, int y) {
			return Coords.get( x , y );
		}
		public float checkMatch(int x, int y) {
			return isBlank() ? 0f 
							 : shapeMatch( x , y );
		}
		protected abstract float shapeMatch(int x, int y);
		protected abstract boolean hint4(boolean pairInS1, boolean pairInS2, boolean pairInS3, Coords...list);
		
		/** <p>Adds possible moves to the hintList based on the Shape's <b> currently displayed </b>
		 * position.</p><p> DO NOT call unless all Shapes are displayed at their correct places; use 
		 * {@link #addHintsToList(int, int)} instead.</p>*/
		public boolean addHintsToList() {
			return addHintsToList( getXi() , getYi() );
		}
		/** <p>Provides a safe way to add possible moves to the hintList when the ShapeTile Array is
		 * out of sync with the displayed positions of the Shapes.</p><p>  To achieve this, it needs to be 
		 * supplied with its current position in the Array.</p> */
		public boolean addHintsToList(int x, int y) {
			return ( !isBlank() && hintMatch( x , y ) );
		}
		/** <p>This method needs to be supplied with the current position on the ShapeTile Array, as the
		 * values supplied by getX() & getY() cannot be relied upon in all circumstances. */
		private boolean hintMatch(int x, int y) {
			for ( HintVisitor each : hintVisitorList ) 
				if ( each.greet( x , y , this ) ) 
					return true;
			return false; 
		}
		/** Return true if the Shape at the supplied coordinates returns true its {@link #isBlank()} call. */
		boolean isBlank(int x, int y) {
			return game.getShape( x , y ).isBlank();
		}
		/** Returns false, unless overridden. (It is overridden in the Blank child classes.) */
		protected boolean isBlank() {
			return false;
		}
		/** Returns true, unless overridden. (It is overridden in the Blank child classes.) */
		protected boolean isMobile() {
			return true;
		}
		protected Shape select() {
			return select( false );
		}
		protected Shape select(boolean andBaseTile) {
			setColor( selected );
			if ( andBaseTile ) selectTile();
			return this;	
		}
		protected Shape selectTile() {
			game.getBoardTile( this ).adjustAlpha( -0.4f );
			return this;
		}
		protected Shape deselect() {
			return deselect( false );
		}
		protected Shape deselect(boolean andBaseTile) {
			setColor( deselected );
			if ( andBaseTile ) deselectTile();
			return this;
		}
		protected Shape deselectTile() {
			game.getBoardTile( this ).restoreAlpha();
			return this;
		}
		void blink(long time) {
			blink( time , 1 , false , this );
		}
		void blink(long time, boolean andBaseTile) {
			blink( time , 1 , andBaseTile , this );
		}
		void blink(long time, int repeats) {
			blink( time , repeats , false , this );
		}
		void blink(long time, int repeats, boolean andBaseTile) {
			blink( time , repeats , andBaseTile , this );
		}
		static void blink(long time, int repeats, Array<Shape> list) {
			blink( time , repeats , false , list.toArray() );
		}
		static void blink(long time, int repeats, Shape...list) {
			blink( time , repeats , false , list );
		}
		static void blink(long time, int repeats, boolean andBaseTile, Array<Shape> list) {
			blink( time , repeats , andBaseTile , list.toArray() );
		}
		static void blink(long time, int repeats, boolean andBaseTile, Shape...list) {	
			for ( int i = 1; i<= repeats; i++ ) {
				for ( Shape each : list ) 
					each.select( andBaseTile );
				
				Gdx.graphics.requestRendering();
				Core.delay( time );
				
				for ( Shape each : list ) 
					each.deselect( andBaseTile );
				
				Gdx.graphics.requestRendering();
				Core.delay( time );
			}
		}
		static void blinkTile(int time, int repeats, Array<Shape> list) {
			blinkTile( time , repeats, list.toArray() );
		}
		static void blinkTile(long time, int repeats, Shape...list) {
			for ( int i = 1; i<= repeats; i++ ) {
				for ( Shape each : list )
					each.selectTile();
				
				Gdx.graphics.requestRendering();
				Core.delay(time);
				
				for ( Shape each : list )
					each.deselectTile();
				
				Gdx.graphics.requestRendering();
				Core.delay(time);
			}	
		}
	}
