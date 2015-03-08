package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class SquareGreen extends SquareAbstract {

	public SquareGreen() {
		super();
		type = type + "Green";
		selected = Colors.get("DARK_GREEN");
		deselected = Colors.get("GREEN");
		deselect();
	}
}
