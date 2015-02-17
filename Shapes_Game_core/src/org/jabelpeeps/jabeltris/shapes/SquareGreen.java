package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareGreen extends SquareAbstract {

	public SquareGreen() {
		this.setRegion(LevelMaster.greysqr);
		type = "SquareGreen";
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
