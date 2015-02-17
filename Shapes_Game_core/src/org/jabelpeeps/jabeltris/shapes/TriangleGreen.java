package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class TriangleGreen extends TriangleAbstract {

	public TriangleGreen() {
		this.setRegion(LevelMaster.greytri);
		type = "TriangleGreen";
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
