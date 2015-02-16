package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class TriangleInverted extends TriangleAbstract {
	
	public TriangleInverted() {
		this.setRegion(LevelMaster.invtri);
		type = "TriangleInverted";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(0.25f, 0.5f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
}
