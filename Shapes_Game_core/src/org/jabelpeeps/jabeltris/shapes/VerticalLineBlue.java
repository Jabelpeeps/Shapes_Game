package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class VerticalLineBlue extends VerticalLineAbstract {

	public VerticalLineBlue() {
		super();
		type = type + "Blue";
		selected = Colors.get("DARK_BLUE");
		deselected = Colors.get("BLUE");
		deselect();
	}
}
