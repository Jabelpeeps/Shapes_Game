package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossTwoOriginal extends CrossTwoAbstract {
	
	public CrossTwoOriginal() {
			this.setRegion(LevelMaster.crosstwo);
			type = "crosstwo";
			deselect();
	}
	@Override
	public void select() {
		this.setColor(0f, 0f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
}
