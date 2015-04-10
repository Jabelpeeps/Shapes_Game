package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public class SpritePlus extends Sprite {
	
	protected PlayArea game;
	protected int x_offset, y_offset;
	private Coords savedXY = Coords.get();
	private Coords newXY = Coords.get();
	private Coords savedOrigin = Coords.get();
	
	public SpritePlus setPlayArea(PlayArea p) {
		game = p;
		return this;
	}
	public SpritePlus setOffsets(int x_off, int y_off) {
		x_offset = x_off;
		y_offset = y_off;
		return this;
	}
	public SpritePlus setOriginAndBounds() {
		return setOriginAndBounds( getX() , getY() );
	}
	public SpritePlus setOriginAndBounds(float f, float g) {
		setBounds( f * 4 + x_offset , g * 4 + y_offset , 4 , 4 );
		setOriginCenter();
		setScale(0.9f);
		setAlpha(0f);
		return this;
	}
	/**<p>NB This method has a slightly different action to the method it overrides.</p>
	*  <p>It sets origin of the Shape relative to the playArea rather than the Shape
	*  itself.  The coordinates are therefore calibrated in PlayArea tiles, not pixels.</p>*/
	@Override
	public void setOrigin(float x, float y) {      
		super.setOrigin( ( x - getX() ) * 4 + 2 , ( y - getY() ) * 4 + 2 );
	}
	@Override
	public float getOriginX() {
		return super.getOriginX() / 4 - 0.5f + getX();
	}
	@Override
	public float getOriginY() {
		return super.getOriginY() / 4 - 0.5f + getY();
	}
	public SpritePlus saveCurrentOrigin() {
		return saveNewOrigin( getOriginX() , getOriginY() );
	}
	public SpritePlus saveNewOrigin(float x, float y) {
		savedOrigin.set(x, y);
		return this;
	}
	public SpritePlus setOriginToSavedOrigin() {
		setOrigin(savedOrigin.xf, savedOrigin.yf);
		return this;
	}
	public SpritePlus rotateInSquare(float angle, Coords centre) {
		float radius = ( angle % 90 > 45 ) ? angle % 45 
										   : 45 - angle % 45;
		radius = 0.7f / MathUtils.cosDeg(radius);
		return rotateAbout(angle, radius, centre);
	}
	public SpritePlus rotateAbout(float angle, float radiusadjustment, Coords centre) {
		float adjX = savedXY.xf - centre.xf ;
		float adjY = savedXY.yf - centre.yf ;
		setPosition( centre.xf + (adjX * MathUtils.cosDeg(angle) - adjY * MathUtils.sinDeg(angle)) * radiusadjustment, 
					 centre.yf + (adjX * MathUtils.sinDeg(angle) + adjY * MathUtils.cosDeg(angle)) * radiusadjustment );
		return this;
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
	public void setX(float x) {
		super.setX( x * 4 + x_offset );
	}
	@Override
	public void setY(float y) {
		super.setY( y * 4 + y_offset );
	}
	/** Returns a new Instance of Coords (from Coords's static internal pool) that has 
	 * been pre-set with the x & y values of this Shape.
	 * @return Use free() method on returned object when you are finished with it. */
	public Coords getXY() {
		return Coords.get( getX() , getY() );
	}
	public int getXi() {
		return (int) getX();
	}
	public int getYi() {
		return (int) getY();
	}
	public SpritePlus resetPosition() {
		return setPosition(savedXY);
	}
	public SpritePlus setPositionToNew() {
		return setPosition(newXY);
	}
	public SpritePlus setPosition(Coords xy) {
		setPosition( xy.xf , xy.yf );
		return this;
	}
	@Override
	public void setPosition(float x, float y) {
		super.setPosition( x * 4 + x_offset , y * 4 + y_offset );
	}
	/** Saves current position in a {@link Coords} instance for later retrieval by {@link #getSavedXY()} */
	public SpritePlus saveXY() {
		savedXY.set( getX() , getY() );
		return this;
	}
	/** <p>Returns a reference to the Shape's internal {@link Coords}, that were previously saved by {@link #saveXY()}.</p>
	 * @return NB Do NOT use the free() method on the returned reference, as the object is still referenced in the Shape. */
	public Coords getSavedXY() {
		return savedXY;
	}
	/** Saves current position in a {@link Coords} instance for later retrieval by {@link #getNewXY()} */
	public SpritePlus setNewXY() {
		return setNewXY( getX() , getY() );
	}
	/** Saves the position specified in the supplied {@link Coords} for later retrieval by {@link #getNewXY()} */
	public SpritePlus setNewXY(Coords xy) {
		newXY.set( xy );
		return this;
	}
	/** Saves the position specified in a {@link Coords} instance for later retrieval by {@link #getNewXY()} */
	public SpritePlus setNewXY(float x, float y) {
		newXY.set( x , y );
		return this;
	}
	/** <p>Returns a reference to the Shape's internal {@link Coords}, that were previously saved by {@link #setNewXY()}, 
	 * {@link #setNewXY(Coords)}, or {@link #setNewXY(float, float)}</p>
	 * @return NB Do NOT use the free() method on the returned reference, as the object is still referenced in the Shape. */
	public Coords getNewXY() {
		return newXY;
	}
	public SpritePlus setNewAlpha(float alpha) {
		super.setAlpha(alpha);
		return this;
	}
	@Override
	protected void finalize() {
		Coords.freeAll(savedXY, newXY, savedOrigin);
	}
	@Override
	public String toString() {
		return 
				getClass().getSimpleName() + 
				" (" + (MathUtils.round(getX() * 100) / 100f) + ", " + (MathUtils.round(getY() * 100) / 100f) + ")"
				;
	}
}
