package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class LineInverted extends LineAbstract {
	
	public LineInverted() {
		this.setRegion(LevelMaster.invline);
		type = "LineInverted";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(1f, 0f, 0f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
}
