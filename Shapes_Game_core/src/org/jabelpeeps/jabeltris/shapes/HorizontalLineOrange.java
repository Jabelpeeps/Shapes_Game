package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class HorizontalLineOrange extends HorizontalLineAbstract {

	public HorizontalLineOrange() {
		super();
		type = type + "Orange";
		deselect();
	}
	@Override
	public void select() {
	this.setColor(Colors.get("DARK_ORANGE"));
	}
	@Override
	public void deselect() {
	this.setColor(Colors.get("ORANGE"));
	}
}
