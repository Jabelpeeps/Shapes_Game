package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class VerticalLineYellow extends VerticalLineAbstract {

	public VerticalLineYellow() {
		super();
		type = type + "Yellow";
		selected = Colors.get("DARK_YELLOW");
		deselected = Colors.get("YELLOW");
		deselect();
	}
}
