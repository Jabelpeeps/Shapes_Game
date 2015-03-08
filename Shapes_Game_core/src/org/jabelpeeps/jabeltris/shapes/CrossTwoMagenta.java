package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class CrossTwoMagenta extends CrossTwoAbstract {

	public CrossTwoMagenta() {
		super();
		type = type + "Magenta";
		selected = Colors.get("DARK_MAGENTA");
		deselected = Colors.get("MAGENTA");
		deselect();
	}
}
