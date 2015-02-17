package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class LineBlue extends LineAbstract {

	public LineBlue() {
		this.setRegion(LevelMaster.greyline);
		type = "LineBlue";
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
