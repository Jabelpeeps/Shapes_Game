package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class Square extends SquareAbstract {

	public Square() {
		setRegion(LevelMaster.greysqr);
		type = "Square";
	}
	public Square(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
