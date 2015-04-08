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
 * {@link Coords#floats()} to obtain a new instance from the pool, and the {@link #free()} 
 * method on instances that you are finished with. (The Coords class has a private 
 * constructor, and so cannot be instantiated via the <b>new</b> command.)    */
public class Coords implements Poolable {
	
	private Coords() {
	}
	private static final Pool<Coords> pool = new ReflectionPool<Coords>(Coords.class, 16, 1024);
	private static final float ROUNDING_ERROR = 0.001f;
	private Values v;
	
	private enum Values {
		FLOAT {
			@Override
			protected int i(int i, float f) { return (int) f; }
			@Override
			protected float f(int i, float f) { return f; }
			@Override
			protected boolean contains(Values other) {
				if ( INT.equals(other) ) 
					throw new RuntimeException("Cannot compare 'INT' with FLOAT");
				return true;
			}},
		INT {
			@Override
			protected int i(int i, float f) { return i; }
			@Override
			protected float f(int i, float f) { return i; }
			@Override
			protected boolean contains(Values other) {
				if ( FLOAT.equals(other) ) 
					throw new RuntimeException("Cannot compare 'FLOAT' with INT");
				return true;	
			}},
		BOTH{
			@Override
			protected int i(int i, float f) { return i; }
			@Override
			protected float f(int i, float f) { return f; }
			@Override
			protected boolean contains(Values other) { return true;	}
		};
		protected abstract int i(int i, float f);
		protected abstract float f(int i, float f);
		protected abstract boolean contains(Values other);
	};

	public int xi = -10;
	public int yi = -10;
	protected float xf = -10f;
	protected float yf = -10f;
	
	public int xi() { return v.i(xi, xf); }
	public int yi() { return v.i(yi, yf); }
	public float xf() { return v.f(xi, xf); }
	public float yf() { return v.f(yi, yf); }
	public String values() { return v.name(); }
	
	private static Coords supply(Values type) {
		Coords temp;
		do { 
			temp = pool.obtain();
		} while ( temp == null );
		temp.v = type;
		return temp;
	}
	/** <p> Returns an instance of Coords from static internal pool.</p>
	 * <p><b>NB</b> Coords are initially set to (-10f, -10f), use {@link Coords#get(float, float)} 
	 * or {@link Coords#get(int, int)} get an instance pre-set to specific values.</p>
	 * <p> You can also use {@link Coords#copy(Coords)} to get a new instance that is
	 * a copy of the supplied instance, or {@link Coords#copy(Shape)} to get an instance 
	 * pre-set with the x & y coordinates of the supplied Shape object. </p>
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords floats() {
		return supply(Values.FLOAT);
	}
	public static Coords ints(){
		return supply(Values.INT);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(float x, float y) {
		return supply(Values.FLOAT).set(x, y);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords get(int x, int y) {
		return supply(Values.INT).set(x, y);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param other - another instance of Coords whose values will be copied.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords copy(Coords other) {
		return supply(other.v).set(other);
	}
	/** Returns an instance of Coords from static internal pool.  
	 * @param shape - a Shape object whose x & y values will be copied.
	 * @return Use free() method on returned object when you are finished with it. */
	public static Coords copy(SpritePlus shape) {
		return supply(Values.FLOAT).set( shape.getX() , shape.getY() );
	}
	/** Returns a new Coords instance set to values representing the centre of the group
	 * Coords provided as argument(s) 
	 * @return Use free() method on returned object when you are finished with it.*/
	public static Coords getCentre(Coords...list) {
			return supply(Values.FLOAT).setToCentre(list);
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
		if ( v.contains(other.v) ) {
			switch ( v ) {
			case BOTH:
			case INT:
				return xi == other.xi && yi == other.yi;
			case FLOAT:
				return Math.abs(xf - other.xf) < ROUNDING_ERROR
					&& Math.abs(yf - other.yf) < ROUNDING_ERROR ;
			}
		}
		return false;
	}
	/** <p>Returns true if this Coords instance represents the values of a tile that is
	 * orthogonally adjacent to the tile represented by the supplied Coords instance.</p> 
	 * <p>NB the check runs only on the stored int values, as it is intended to be quick,
	 * crude and fast.</p><p>  If you must run it on Coords with float Values, use 
	 * {@link #updateAllValues()} first.*/
	public boolean isAdjacentTo(Coords other) {
		return ( yi == other.yi && ( xi + 1 == other.xi || xi - 1 == other.xi ) ) 
			|| ( xi == other.xi && ( yi + 1 == other.yi || yi - 1 == other.yi ) ) ;
	}
	public Coords updateAllValues() {
		switch ( v ) {
		case FLOAT:
			xi = (int) xf;
			yi = (int) yf;
			break;
		case INT:
			xf = xi;
			yf = yi;
			break;
		case BOTH:
			break;
		}
		v = Values.BOTH;
		return this;
	}
	/** Sets the values of this Coords instance.  
	 * @param shape - a Shape or SpritePlus object whose x & y values will be copied. */
	public Coords set(SpritePlus shape) {
		return set( shape.getX() , shape.getY() );
	}
	/** Sets the values of this Coords instance.  
	 * @param other - another instance of Coords whose values will be copied. */
	public Coords set(Coords other) {
		switch (other.v) {
		case FLOAT:
			return set(other.xf, other.yf);
		case INT:
			return set(other.xi, other.yi);
		case BOTH:
			return set(other.xf, other.yf).updateAllValues();
		}
		return this;
	}
	/** Sets the values of this Coords instance.   
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain. */
	public Coords set(int x, int y) {
		xf = x;
		yf = y;
		xi = x;
		yi = y;
		return this;
	}
	/** Sets the values of this Coords instance.   
	 * @param x - x value that the returned Coords will contain.
	 * @param y - y value that the returned Coords will contain. */
	public Coords set(float x, float y) {
		v = Values.FLOAT;
		xf = x;
		yf = y;
		return this;
	}
	public Coords add(SpritePlus sprite) {
		return set( xf + sprite.getX() , yf + sprite.getY() );
	}
	public Coords add(float f) {
		return set( xf + f, yf + f );
	}
	private Coords add(Coords other) {
		return set( xf + other.xf() , yf + other.yf() );
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
	@Override
	public void reset() {
		xf = -10f;
		yf = -10f;
		xi = -10;
		yi = -10;
	}
	@Override
	public String toString() {
		return " (" + MathUtils.round( xf() * 100f ) / 100f  + ", " + MathUtils.round( yf() * 100f ) / 100f + ")";
	}
}
