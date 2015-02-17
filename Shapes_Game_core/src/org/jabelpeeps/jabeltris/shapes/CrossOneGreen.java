package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossOneGreen extends CrossOneAbstract {

	public CrossOneGreen() {
		this.setRegion(LevelMaster.greycrone);
		type = "CrossOneGreen";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(0, 0.5f, 0, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(0, 1f, 0, 1f);
	}
}
