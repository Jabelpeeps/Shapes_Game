package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class TriangleYellow extends TriangleAbstract {

	public TriangleYellow() {
		this.setRegion(LevelMaster.greytri);
		type = "TriangleYellow";
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
