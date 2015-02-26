package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class VerticalLineGreen extends VerticalLineAbstract {

	public VerticalLineGreen() {
		super();
		type = type + "Green";
		deselect();
	}
	@Override
	public void select() {
	this.setColor(Colors.get("DARK_GREEN"));
	}
	@Override
	public void deselect() {
	this.setColor(Colors.get("GREEN"));
	}
}
