package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareReal extends SquareAbstract {

	public SquareReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareReal";
	}
	public SquareReal(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
