package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareBlue extends SquareAbstract {

	
	public SquareBlue() {
		this.setRegion(LevelMaster.greysqr);
		type = "SquareBlue";
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
