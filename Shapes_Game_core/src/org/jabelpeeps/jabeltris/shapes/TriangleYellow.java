package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class TriangleYellow extends TriangleAbstract {

	public TriangleYellow() {
		super();
		type = type + "Yellow";
		selected = Colors.get("DARK_YELLOW");
		deselected = Colors.get("YELLOW");
		deselect();
	}
}
