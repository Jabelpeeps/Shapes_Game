package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Coords;
import org.jabelpeeps.jabeltris.Shape;

public abstract class BlankAbstract extends Shape {
 
	@Override
	protected float shapeMatch(int x, int y) {
		return 0f;
	}
	@Override
	protected boolean hint4(boolean pairS1, boolean pairS2, boolean pairS3, Coords...list) {
		return false;
	}
	@Override
	public Shape select() {
		return this;
	}
	@Override
	public Shape deselect() {
		return this;
	}
	public abstract void free();
}
