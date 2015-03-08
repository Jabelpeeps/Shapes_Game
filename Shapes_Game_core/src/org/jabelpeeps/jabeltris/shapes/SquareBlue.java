package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class SquareBlue extends SquareAbstract {

	public SquareBlue() {
		super();
		type = type + "Blue";
		selected = Colors.get("DARK_BLUE");
		deselected = Colors.get("BLUE");
		deselect();
	}
}
