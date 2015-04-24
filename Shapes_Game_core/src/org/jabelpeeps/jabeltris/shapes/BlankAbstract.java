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
	protected Shape select() {
		return this;
	}
	@Override
	protected Shape deselect() {
		return this;
	}
	@Override
	protected boolean matches(Shape other) {
		free();
		return false;
	}
	@Override
	protected boolean isBlank() {
		free();
		return true;
	}
	@Override 
	protected abstract boolean isMobile();
	
	public void free() {
	}
}
