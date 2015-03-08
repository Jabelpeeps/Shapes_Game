package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class TriangleGreen extends TriangleAbstract {

	public TriangleGreen() {
		super();
		type = type + "Green";
		selected = Colors.get("DARK_GREEN");
		deselected = Colors.get("GREEN");
		deselect();
	}
}
