package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class LineGreen extends LineAbstract {

	public LineGreen() {
		super();
		type = type + "Green";
		selected = Colors.get("DARK_GREEN");
		deselected = Colors.get("GREEN");
		deselect();
	}
}
