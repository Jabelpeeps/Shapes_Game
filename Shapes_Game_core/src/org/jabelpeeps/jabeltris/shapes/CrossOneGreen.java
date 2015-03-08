package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class CrossOneGreen extends CrossOneAbstract {

	public CrossOneGreen() {
		super();
		type = type + "Green";
		selected = Colors.get("DARK_GREEN");
		deselected = Colors.get("GREEN");
		deselect();
	}
}
