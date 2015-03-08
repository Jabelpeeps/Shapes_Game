package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class CrossTwoBlue extends CrossTwoAbstract {

	public CrossTwoBlue() {
		super();
		type = type + "Blue";
		selected = Colors.get("DARK_BLUE");
		deselected = Colors.get("BLUE");
		deselect();
	}
}
