package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class TriangleOriginal extends TriangleAbstract {
	
	public TriangleOriginal() {
		this.setRegion(LevelMaster.triangle);
		type = "triangle";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(1f, 0f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
}
