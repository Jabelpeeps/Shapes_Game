package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class VerticalLineOrange extends VerticalLineAbstract {

	public VerticalLineOrange() {
		super();
		type = type + "Orange";
		selected = Colors.get("DARK_ORANGE");
		deselected = Colors.get("ORANGE");
		deselect();
	}
}
