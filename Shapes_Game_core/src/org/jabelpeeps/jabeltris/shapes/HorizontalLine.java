package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class HorizontalLine extends HorizontalLineAbstract {

	public HorizontalLine() {
		setRegion(LevelMaster.horizgreyline);
		type = "HorizontalLine";
	}
	public HorizontalLine(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
