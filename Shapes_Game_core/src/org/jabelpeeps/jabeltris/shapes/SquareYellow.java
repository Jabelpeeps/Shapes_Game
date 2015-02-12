package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareYellow extends SquareAbstract {
	
	public SquareYellow() {
		this.setRegion(LevelMaster.greysqr);
		type = "SquareYellow";
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
