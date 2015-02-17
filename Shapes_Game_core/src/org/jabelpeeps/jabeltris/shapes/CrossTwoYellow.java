package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossTwoYellow extends CrossTwoAbstract {

	public CrossTwoYellow() {
		this.setRegion(LevelMaster.greycrtwo);
		type = "CrossTwoYellow";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(0.5f, 0.5f, 0, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 0, 1f);
	}
}
