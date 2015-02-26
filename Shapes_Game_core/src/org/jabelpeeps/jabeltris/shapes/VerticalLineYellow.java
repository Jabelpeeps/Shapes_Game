package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class VerticalLineYellow extends VerticalLineAbstract {

	public VerticalLineYellow() {
		super();
		type = type + "Yellow";
		deselect();
	}
	@Override
	public void select() {
	this.setColor(Colors.get("DARK_YELLOW"));
	}
	@Override
	public void deselect() {
	this.setColor(Colors.get("YELLOW"));
	}
}
