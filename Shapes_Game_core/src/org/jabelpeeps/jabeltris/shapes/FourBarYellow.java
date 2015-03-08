package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class FourBarYellow extends FourBarAbstract {

	public FourBarYellow() {
		super();
		type = type + "Yellow";
		selected = Colors.get("DARK_YELLOW");
		deselected = Colors.get("YELLOW");
		deselect();
	}
}