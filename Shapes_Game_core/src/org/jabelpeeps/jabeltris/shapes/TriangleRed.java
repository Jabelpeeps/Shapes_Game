package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class TriangleRed extends TriangleAbstract {

	public TriangleRed() {
		this.setRegion(LevelMaster.greytri);
		type = "TriangleRed";
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
