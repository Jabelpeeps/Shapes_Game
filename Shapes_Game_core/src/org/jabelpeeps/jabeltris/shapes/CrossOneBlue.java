package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;


public class CrossOneBlue extends CrossOneAbstract {

	public CrossOneBlue() {
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
