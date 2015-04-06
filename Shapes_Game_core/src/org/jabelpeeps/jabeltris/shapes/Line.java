package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class Line extends LineAbstract {

	public Line() {
		super();
		setRegion(LevelMaster.greyline);
		type = "Line";
	}
	public Line(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
