package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class TriangleRed extends TriangleAbstract {

	public TriangleRed() {
		super();
		type = type + "Red";
		selected = Colors.get("DARK_RED");
		deselected = Colors.get("RED");
		deselect();
	}
}
