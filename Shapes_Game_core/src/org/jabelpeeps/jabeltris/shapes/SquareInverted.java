package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareInverted extends SquareAbstract {

	public SquareInverted() {
		this.setRegion(LevelMaster.invsqr);
		type = "SquareInverted";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(0f, 0.5f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}

}