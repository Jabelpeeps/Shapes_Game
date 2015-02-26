package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class HorizontalLineRed extends HorizontalLineAbstract {

	public HorizontalLineRed() {
		super();
		type = type + "Red";
		deselect();
	}
	@Override
	public void select() {
	this.setColor(Colors.get("DARK_RED"));
	}
	@Override
	public void deselect() {
	this.setColor(Colors.get("RED"));
	}
}
