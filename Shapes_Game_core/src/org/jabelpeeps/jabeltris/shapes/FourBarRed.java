package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class FourBarRed extends FourBarAbstract {

	public FourBarRed() {
		super();
		type = type + "Red";
		selected = Colors.get("DARK_RED");
		deselected = Colors.get("RED");
		deselect();
	}
}