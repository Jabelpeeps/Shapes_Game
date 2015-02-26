package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class FourBarMagenta extends FourBarAbstract {

	public FourBarMagenta() {
		super();
		type = type + "Magenta";
		deselect();
	}
	@Override
	public void select() {
	this.setColor(Colors.get("DARK_MAGENTA"));
	}
	@Override
	public void deselect() {
	this.setColor(Colors.get("MAGENTA"));
	}
}
