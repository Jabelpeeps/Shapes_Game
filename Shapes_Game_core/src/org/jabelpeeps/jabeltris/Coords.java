package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/** <p>An object to represent a single pair of (x , y) coordinates, coupled with some 
 * convenience methods for interacting with them, and comparing them to others. </p>
 * <p>It is designed to be a lightweight - and also easily re-usable - object, and 
 * therefore contains its own internal static thread-safe object pool.  Use 
 * {@link Coords#get()} to obtain a new instance from the pool, and the {@link #free()} 
 * method on instances that you are finished with. (The Coords class has a private 
 * constructor, and so cannot be instantiated via the <b>new</b> command.)    */
public class Coords {
	
	private static final float ROUNDING_ERROR = 0.001f;
	private final static int POOLSIZE = 32;
	
	private static ThreadLocal<Coords[]> freeCoords = new ThreadLocal<Coords[]>() {
			@Override protected Coords[] initialValue() { 
				return new Coords[POOLSIZE]; 
	}};
	private static ThreadLocal<Integer> coordsFreed = new ThreadLocal<Integer>() {
			@Override protected Integer initialValue() { 
				return 0; 
	}};
	private Coords() {}

	public int xi = 10, yi = 10;
	protected float xf = -10f, yf = -10f;

	private Coords updateFloats() {
		xf = xi;
		yf = yi;
		return this;
	}
	private Coords updateInts() {
		xi = (int) xf;
		yi = (int) yf;
		return this;
	}
	private static Coords supply() {
		Coords tempRef;
		if ( coordsFreed.get() > 0 ) {
			int index = coordsFreed.get();
			coordsFreed.set(--index);
			tempRef = freeCoords.get()[index];
			freeCoords.get()[index] = null;
		} else 
			tempRef = new Coords();
		return tempRef;
	}
	/** <p> Returns an instance of Coords from static internal pool.</p>
	 * <p><b>NB</b> Coords are initially set to (-10f, -10f), use {@link Coords#get(float, float)} 
	 * or {@link Coords#get(int, int)} get an instance pre-set to specific values.</p>
	 * <p> You can also use {@link Coords#copy(Coords)} to get a new instance that is
	 * a copy of the supplied instance, or {@link Coords#copy(Shape)} to get an instance 
	 * pre-set with the x & y coordinates of the supplied Shape object. </p>
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(){
		return supply();
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(float x, float y) {
		return supply().set(x, y);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(int x, int y) {
		return supply().set(x, y);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param other - another instance of Coords whose values will be copied.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords copy(Coords other) {
		return supply().set(other);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param shape - a Shape object whose x & y values will be copied.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords copy(SpritePlus shape) {
		return supply().set( shape.getX() , shape.getY() );
	}
	/** Returns a new Coords instance set to values representing the centre of the group
	 * Coords provided as argument(s) 
	 * @return Use free() method on returned object when you are finished with it.*/
	public static Coords getCentre(Coords...list) {
			return supply().setToCentre(list);
	}
	public Coords setToCentre(Coords...list) {
		    set(0f,0f);
			for ( Coords each : list ) 
				add(each);					
						
			return div(list.length);
	}
	/** <p>Returns true if this Coords instance is set to the same Value type and has the 
	 * same values as the supplied Coords instance.</p> */
	public boolean isEqualTo(Coords other) {
		return isEqualTo(other, false);
	}
	public boolean isEqualTo(Coords other, boolean useFloats) {
		if ( useFloats )
			return 		Math.abs(xf - other.xf) < ROUNDING_ERROR
					&&  Math.abs(yf - other.yf) < ROUNDING_ERROR ;
		return xi == other.xi && yi == other.yi;
	}
	/** <p>Returns true if this Coords instance represents the values of a tile that is
	 * orthogonally adjacent to the tile represented by the supplied Coords instance.</p> 
	 * <p>NB the check runs only on the stored int values, as it is intended to be quick,
	 * crude and fast.</p>*/
	public boolean isAdjacentTo(Coords other) {
		return ( yi == other.yi && ( xi + 1 == other.xi || xi - 1 == other.xi ) ) 
			|| ( xi == other.xi && ( yi + 1 == other.yi || yi - 1 == other.yi ) ) ;
	}
	/** Sets the values of this Coords instance.  
	 * @param shape - a Shape or SpritePlus object whose x & y values will be copied. */
	public Coords set(SpritePlus shape) {
		return set( shape.getX() , shape.getY() );
	}
	/** Sets the values of this Coords instance.  
	 * @param other - another instance of Coords whose values will be copied. */
	public Coords set(Coords other) {
		return set(other.xf, other.yf).updateInts();
	}
	/** Sets the values of this Coords instance.   
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain. */
	public Coords set(int x, int y) {
		xi = x;
		yi = y;
		return updateFloats();
	}
	/** Sets the values of this Coords instance.   
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain. */
	public Coords set(float x, float y) {
		xf = x;
		yf = y;
		return updateInts();
	}
	public Coords add(SpritePlus sprite) {
		return set( xf + sprite.getX() , yf + sprite.getY() );
	}
	public Coords add(float f) {
		return set( xf + f , yf + f );
	}
	Coords add(Coords other) {
		return set( xf + other.xf , yf + other.yf );
	}
	public Coords sub(float f) {
		return set( xf - f , yf - f );
	}
	public Coords sub(Coords other) {
		return set( xf - other.xf , yf - other.yf );
	}
	public Coords div(int d) {
		return set( xf / d , yf / d );
	}
	public float angle() {
		float angle = MathUtils.atan2(yf, xf) * MathUtils.radiansToDegrees;
		return (angle < 0) ? angle + 360 : angle;
	}
	
	public static void freeAll(Array<Coords> list) {
		if ( list == null ) return;
		Coords.freeAll(list.toArray());
	}
	public static void freeAll(final Coords...list) {
		for ( Coords each : list )
			each.free();
	}
	/** Call this method on Coords objects when you are finished using them
	 * to return them to the pool. */
	public synchronized void free() {
		int index = coordsFreed.get();
		coordsFreed.set(++index);
		if ( --index < POOLSIZE ) {
			xf = -10f;
			yf = -10f;
			xi = -10;
			yi = -10;
			freeCoords.get()[index] = this;
		}
	}
	@Override
	public String toString() {
		return " (" + MathUtils.round( xf * 100f ) / 100f  + ", " + MathUtils.round( yf * 100f ) / 100f + ")";
	}
}