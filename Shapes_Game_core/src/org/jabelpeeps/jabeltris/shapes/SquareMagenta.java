package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class SquareMagenta extends SquareAbstract {

	public SquareMagenta() {
		super();
		type = type + "Magenta";
		selected = Colors.get("DARK_MAGENTA");
		deselected = Colors.get("MAGENTA");
		deselect();
	}
}
