package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class HorizontalLineGreen extends HorizontalLineAbstract {

	public HorizontalLineGreen() {
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
