package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class LineYellow extends LineAbstract {

	public LineYellow() {
		this.setRegion(LevelMaster.greyline);
		type = "LineYellow";
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
