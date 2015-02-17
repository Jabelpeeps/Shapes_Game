package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossTwoGreen extends CrossTwoAbstract {

	public CrossTwoGreen() {
		this.setRegion(LevelMaster.greycrtwo);
		type = "CrossTwoGreen";
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
