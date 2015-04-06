package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossTwo extends CrossTwoAbstract {

	public CrossTwo() {
		setRegion(LevelMaster.greycrtwo);
		type = "CrossTwo";
	}
	public CrossTwo(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
