package org.jabelpeeps.jabeltris;

/** Provides a simple looping iterator between the values of 0-3 (inclusive). */
public class IterateIn4 {
	
	private int i = 0;
	
	IterateIn4() {
	}
	IterateIn4(int i){
		this.i = i;
	}
	/** Returns the current value and increments it - for subsequent calls.*/
	public int get() {
		chkLoop();
		return i++;
	}
	/** Returns the current value without incrementing it.*/
	public int chk() {
		chkLoop();
		return i;
	}
	/** <p>Sets the value that will be returned by the next call to get(). </p>
	 * <p>Attempting to set a value of >= 4 will set a value of 0 </p> */
	public void set(int i) {
		this.i = i;
		chkLoop();
	}
	private void chkLoop() {
		if ( i >= 4 ) i = 0;
	}
}