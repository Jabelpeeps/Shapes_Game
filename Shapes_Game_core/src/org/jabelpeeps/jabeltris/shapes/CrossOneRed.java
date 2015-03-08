package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;


public class CrossOneRed extends CrossOneAbstract {

	public CrossOneRed() {
		super();
		type = type + "Red";
		selected = Colors.get("DARK_RED");
		deselected = Colors.get("RED");
		deselect();
	}
}
