package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossOneBlue extends CrossOneAbstract {

	public CrossOneBlue() {
		this.setRegion(LevelMaster.greycrone);
		type = "CrossOneBlue";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(0, 0.25f, 0.5f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(0, 0.5f, 1f, 1f);
	}
}
