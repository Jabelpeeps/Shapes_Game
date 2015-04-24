package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GestureMultiplexer extends InputAdapter implements GestureListener {
	
	private final GestureDetector detector = new GestureDetector(this);
	private Array<GestureListener> listeners = new Array<GestureListener>(4);
	private Array<TouchListener> touchListeners = new Array<TouchListener>(4);
	
	private GestureMultiplexer () {	}
	private static GestureMultiplexer instance;
	static { instance = new GestureMultiplexer(); }
	
	public static GestureMultiplexer getInstance() {
		 return instance;
	}
	
	public void addListener (int index, GestureListener listener) {
		if ( listener == null ) throw new NullPointerException( "listener cannot be null" );
		listeners.insert( index, listener );
		
		if ( !(listener instanceof TouchListener) ) return;
		
		touchListeners.clear();
		for ( GestureListener each : getListeners() )
			if ( each instanceof TouchListener )
				touchListeners.add( (TouchListener) each );
	}
	public void removeListener (int index) {
		GestureListener tempRef = listeners.removeIndex( index );
		if ( tempRef instanceof TouchListener )
			touchListeners.removeValue( (TouchListener) tempRef, true );
	}
	public void addListener (GestureListener listener) {
		if ( listener == null ) throw new NullPointerException( "listener cannot be null" );
		listeners.add( listener );
		if ( listener instanceof TouchListener ) 
			touchListeners.add( (TouchListener) listener );
	}
	public void removeListener (GestureListener listener) {
		listeners.removeValue( listener, true );;
		if ( listener instanceof TouchListener )
			touchListeners.removeValue( (TouchListener) listener, true );
	}
	/** @return the number of listeners in this multiplexer */
	public int size () {
		return listeners.size;
	}
	public void clear () {
		listeners.clear();
		touchListeners.clear();
	}
	public void setListeners (Array<GestureListener> listeners) {
		clear();
		for ( GestureListener each : listeners )
			addListener( each );
	}
	public Array<GestureListener> getListeners () {
		return listeners;
	}
	
	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		for ( int i = 0, n = touchListeners.size; i < n; i++ ) 
			if ( touchListeners.get(i).touchDownFirst(x, y, pointer, button) ) return true;
		
		if ( detector.touchDown(x, y, pointer, button) ) return true;
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		for ( int i = 0, n = touchListeners.size; i < n; i++ ) 
			if ( touchListeners.get(i).touchUpFirst(x, y, pointer, button) ) {
				detector.cancel();
				return true;
			}
		if ( detector.touchUp(x, y, pointer, button) ) return true;
		
		for ( int i = 0, n = touchListeners.size; i < n; i++ ) 
			if ( touchListeners.get(i).touchUpSecond(x, y, pointer, button) ) return true;
		return false;
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		for ( int i = 0, n = touchListeners.size; i < n; i++ ) 
			if ( touchListeners.get(i).touchDraggedFirst(x, y, pointer) ) {
				detector.cancel();
				return true;
			}
		if ( detector.touchDragged(x, y, pointer) ) return true;
		
		for ( int i = 0, n = touchListeners.size; i < n; i++ ) 
			if ( touchListeners.get(i).touchDraggedSecond(x, y, pointer) ) return true;
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		for (int i = 0, n = listeners.size; i < n; i++) {
			if ( listeners.get(i).touchDown(x, y, pointer, button) ) return true;
			if ( listeners.get(i) instanceof TouchListener 
					&& ( (TouchListener) listeners.get(i) ).touchDownSecond( (int) x , (int) y , pointer , button ) ) return true;
		}
		return false;
	}
	@Override
	public boolean tap(float x, float y, int count, int button) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if ( listeners.get(i).tap(x, y, count, button) ) return true;
		return false;
	}
	@Override
	public boolean longPress(float x, float y) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if ( listeners.get(i).longPress(x, y) ) return true;
		return false;
	}
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if ( listeners.get(i).fling(velocityX, velocityY, button) ) return true;
		return false;
	}
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if ( listeners.get(i).pan(x, y, deltaX, deltaY) ) return true;
		return false;
	}
	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if ( listeners.get(i).panStop(x, y, pointer, button) ) return true;
		return false;
	}
	@Override
	public boolean zoom(float initialDistance, float distance) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if ( listeners.get(i).zoom(initialDistance, distance) ) return true;
		return false;
	}
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if ( listeners.get(i).pinch(initialPointer1, initialPointer2, pointer1, pointer2) ) return true;
		return false;
	}
	/**<p> Implement this interface in your GestureListeners, in addition to the GestureListener interface,
	 * to allow them direct access to the touchDown(), touchUp() and touchDragged() events.
	 * <p> Each method should obey the general contract for an input event, i.e. to return true if the event 
	 * has been fully handled, and false if it should be passed onto subsequent listeners/inputProcessors.
	 * <p> Each event has a corresponding ...First() and ...Second() method which allows implementations to 
	 * attempt to process the event either before or after it is passed to the GestureDetector.  
	 * <p> If touchUpFirst() or touchDraggedFirst() return true, the cancel() method on the GestureDetector will be
	 * automatically called by GestureMultiplexer.
	 * <p><b> Examples:-</b>
	 * <p><b> 1)</b> an implementation of a touchUpSecond() method will be called only after the event has been passed
	 * to the GestureDetector for possible handling, and only if the GestureDetector does  not return 'true'. 
	 * <p><b> 2)</b> an implementation of touchDraggedFirst() will be called before the GestureDetector receives the 
	 * event - if the implementation returns false.  If it returns true, the event chain will end there. */
	public static interface TouchListener {
	
		public boolean touchDownFirst(int x, int y, int pointer, int button);
		public boolean touchDownSecond(int x, int y, int pointer, int button);
		public boolean touchUpFirst(int x, int y, int pointer, int button);
		public boolean touchUpSecond(int x, int y, int pointer, int button);
		public boolean touchDraggedFirst(int x, int y, int pointer);
		public boolean touchDraggedSecond(int x, int y, int pointer);
	}
		
// -------------------------------------Methods that solely pass calls to the wrapped GestureDetector---------	
	/** No further gesture events will be triggered for the current touch, if any. */
	public void cancel () 									 { detector.cancel(); }
	/** @return whether the user touched the screen long enough to trigger a long press event. */
	public boolean isLongPressed () 						 { return detector.isLongPressed(); }
	/** @param duration
	 * @return whether the user touched the screen for as much or more than the given duration. */
	public boolean isLongPressed (float duration) 			 { return detector.isLongPressed(duration); }
	public boolean isPanning () 							 { return detector.isPanning(); }
	public void reset () 									 { detector.reset(); }
	/** The tap square will not longer be used for the current touch. */
	public void invalidateTapSquare () 						 { detector.invalidateTapSquare(); }
	public void setTapSquareSize (float halfTapSquareSize)   { detector.setTapSquareSize(halfTapSquareSize); }
	/** @param tapCountInterval time in seconds that must pass for two touch down/up sequences to be detected as consecutive taps. */
	public void setTapCountInterval (float tapCountInterval) { detector.setTapCountInterval(tapCountInterval); }
	public void setLongPressSeconds (float longPressSeconds) { detector.setLongPressSeconds(longPressSeconds); }
	public void setMaxFlingDelay (long maxFlingDelay) 		 { detector.setMaxFlingDelay(maxFlingDelay); }
}
