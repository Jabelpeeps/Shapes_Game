package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class TriangleBlue extends TriangleAbstract {

	public TriangleBlue() {
		super();
		type = type + "Blue";
		deselect();
	}
	@Override
	public void select() {
	this.setColor(Colors.get("DARK_BLUE"));
	}
	@Override
	public void deselect() {
	this.setColor(Colors.get("BLUE"));
	}
}
