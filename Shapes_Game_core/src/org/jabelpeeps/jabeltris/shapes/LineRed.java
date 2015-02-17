package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class LineRed extends LineAbstract {

	public LineRed() {
		this.setRegion(LevelMaster.greyline);
		type = "LineRed";
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
