package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossOne extends CrossOneAbstract {

	public CrossOne() {
		setRegion(LevelMaster.greycrone);
		type = "CrossOne";
	}
	public CrossOne(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
