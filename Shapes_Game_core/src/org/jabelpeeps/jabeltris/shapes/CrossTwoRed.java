package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossTwoRed extends CrossTwoAbstract {

	public CrossTwoRed() {
		this.setRegion(LevelMaster.greycrtwo);
		type = "CrossTwoRed";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(0.5f, 0, 0, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 0, 0, 1f);
	}
}
