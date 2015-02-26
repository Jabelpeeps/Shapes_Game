package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class CrossTwoYellow extends CrossTwoAbstract {

	public CrossTwoYellow() {
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
