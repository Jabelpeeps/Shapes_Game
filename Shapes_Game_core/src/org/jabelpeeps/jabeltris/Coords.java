package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ReflectionPool;

/** <p>An object to represent a single pair of (x , y) coordinates, coupled with some 
 * convenience methods for interacting with them, and comparing them to others. </p>
 * <p>It is designed to be a lightweight - and also easily re-usable - object, and 
 * therefore contains its own internal statically referenced object pool.  Use 
 * {@link Coords#get()} to obtain a new instance from the pool, and the {@link #free()} 
 * method on instances that you are finished with. (The Coords class has a private 
 * constructor, and so cannot be instantiated via the <b>new</b> command.)    */
public class Coords implements Poolable {
	
	private Coords() {
	}
	private static Pool<Coords> pool = new ReflectionPool<Coords>(Coords.class, 16); 
	private static Array<Values<Integer>> freeIntValues = new Array<Values<Integer>>(false, 16);
	private static Array<Values<Float>> freeFloatValues = new Array<Values<Float>>(false, 16);
	private static final float ROUNDING_ERROR = 0.001f;
	
	public Values<?> x;
	public Values<?> y;
	
	public static class Values<T extends Number> {
		private T t;
		protected Values() {}
		protected Values<T> set(T v) {
			t = v;
			return this;
		}
		public int i() {
			return t.intValue();
		}
		public float f() {
			return t.floatValue();
		}
	}
	public static int i(Values<?> n) {
		return n.t.intValue();
	}
	public static float f(Values<?> n) {
		return n.t.floatValue(); 
	}
	private static boolean isEqual(Values<?> n1, Values<?> n2) { 
		return Math.abs( f(n1) - f(n2) ) <= ROUNDING_ERROR; 
	}
	private static boolean isEqual(float n1, Values<?> n2) { 
		return Math.abs( n1 - f(n2) ) <= ROUNDING_ERROR; 
	}
	private static <T extends Number> Values<T> copy(Values<T> v) {
		return obtain(v.t);
	}
	private static <T extends Number> Values<T> copy(T v) {
		return obtain(v);
	}
	@SuppressWarnings("unchecked")
	private static <T extends Number> Values<T> obtain(T v) {
		Values<T> temp = null;
		while ( temp == null ) {
			if ( v instanceof Float )
				temp = (Values<T>) obtainFloat(); 
			else if ( v instanceof Integer )
				temp = (Values<T>) obtainInt();
		}
		return temp.set(v);
	}
	private static Values<Float> obtainFloat(){
		return ( freeFloatValues.size == 0 ) ? new Values<Float>()
											 : freeFloatValues.pop();
	}
	private static Values<Integer> obtainInt(){
		return ( freeIntValues.size == 0 ) ? new Values<Integer>()
										   : freeIntValues.pop();
	}
	
	private static <T extends Number> Coords supply(T v1, T v2) {
		Coords temp = supply();
		temp.x = obtain(v1);
		temp.y = obtain(v2);
		return temp;
	}
	private static Coords supply() {
		Coords temp;
		do { 
			temp = pool.obtain();
		} while ( temp == null );
		return temp;
	}
	/** <p> Returns an instance of Coords from static internal pool.</p>
	 * <p><b>NB</b> Coords are initially set to (-10f, -10f), use {@link Coords#get(float, float)} 
	 * or {@link Coords#get(int, int)} get an instance pre-set to specific values.</p>
	 * <p> You can also use {@link Coords#get(Coords)} to get a new instance that is
	 * a copy of the supplied instance, or {@link Coords#get(Shape)} to get an instance 
	 * pre-set with the x & y coordinates of the supplied Shape object. </p>
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get() {
		return get(-10f, -10f);
	}
	public static Coords ints(){
		return ints(-10, -10);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(float x, float y) {
		return supply(x, y);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords ints(int x, int y) {
		return supply(x, y);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param other - another instance of Coords whose values will be copied.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(Coords other) {
		return supply().set(other);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param shape - a Shape object whose x & y values will be copied.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(Shape shape) {
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
	/** <p>Returns true if this Coords instance is set to the same values as the supplied
	 * Coords instance.</p> */
	public boolean isEqualTo(Coords other) {
		return ( isEqual(x, other.x) && isEqual(y, other.y) );
	}
	/** <p>Returns true if this Coords instance represents the values of a tile that is
	 * orthogonally adjacent to the tile represented by the supplied Coords instance.</p> */
	public boolean isAdjacentTo(Coords other) {
		return ( isEqual(y, other.y) && ( isEqual(f(x) + 1, other.x) || isEqual(f(x) - 1, other.x) ) )
			|| ( isEqual(x, other.x) && ( isEqual(f(y) + 1, other.y) || isEqual(f(y) - 1, other.y) ) ) ;
	}
	/** Sets the values of this Coords instance.  
	 * @param shape - a Shape or SpritePlus object whose x & y values will be copied. */
	public Coords set(SpritePlus shape) {
		return set( shape.getX() , shape.getY() );
	}
	/** Sets the values of this Coords instance.  
	 * @param other - another instance of Coords whose values will be copied. */
	public Coords set(Coords other) {
		x = copy(other.x);
		y = copy(other.y);	
		return this;
	}
	/** Sets the values of this Coords instance.   
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain. */
	public <T extends Number> Coords set(T x, T y) {
		this.x = copy(x);
		this.y = copy(y);
		return this;
	}
	public Coords add(SpritePlus sprite) {
		return set( f(x) + sprite.getX() , f(y) + sprite.getY() );
	}
	public Coords add(float f) {
		return set( f(x) + f, f(y) + f);
	}
	public Coords add(Coords other) {
		return set( f(x) + f(other.x) , f(y) + f(other.y) );
	}
	public Coords sub(Coords other) {
		return set( f(x) - f(other.x) , f(y) - f(other.y) );
	}
	public Coords mul(int m) {
		return set( f(x) * m , f(y) * m );
	}
	public Coords div(int d) {
		return set( f(x) / d , f(y) / d );
	}
	public float angle() {
		float angle = MathUtils.atan2(f(y), f(x)) * MathUtils.radiansToDegrees;
		return (angle < 0) ? angle + 360 : angle;
	}
	public static void freeAll(final Array<Coords> list) {
		
		Core.threadPool.execute(new Runnable(){
			@Override
			public void run() {
				pool.freeAll(list);
			}
		});
	}
	public static void freeAll(final Coords...list) {
		
		Core.threadPool.execute(new Runnable(){
			@Override
			public void run() {
				for ( Coords each : list )
						each.free();
			}
		});
	}
	/** Call this method on Coords objects when you are finished using them
	 * to return them to the pool. */
	public synchronized void free() {
		pool.free(this);
	}
	/** Method called automatically when Coords are returned to their pool.*/
	@SuppressWarnings("unchecked")
	@Override
	public void reset() {
		if ( x.t instanceof Integer ) 
			freeIntValues.addAll((Values<Integer>) x, (Values<Integer>) y);
		else if ( x.t instanceof Float )
			freeFloatValues.addAll((Values<Float>) x, (Values<Float>) y);
	}
	@Override
	public String toString() {
		return " (" + MathUtils.round(f(x) * 100f) / 100f  + ", " + MathUtils.round(f(y) * 100f) / 100f + ")";
	}
}
