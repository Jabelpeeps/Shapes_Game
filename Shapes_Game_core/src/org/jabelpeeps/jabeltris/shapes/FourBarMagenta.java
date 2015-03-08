package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class FourBarMagenta extends FourBarAbstract {

	public FourBarMagenta() {
		super();
		type = type + "Magenta";
		selected = Colors.get("DARK_MAGENTA");
		deselected = Colors.get("MAGENTA");
		deselect();
	}
}
