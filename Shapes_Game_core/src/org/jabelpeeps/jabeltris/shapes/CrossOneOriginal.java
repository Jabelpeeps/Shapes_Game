package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossOneOriginal extends CrossOneAbstract {
	
	public CrossOneOriginal() {
		this.setRegion(LevelMaster.crossone);
		type = "crossone";
	}
	
	@Override
	public void select() {
		this.setColor(0f, 1f, 0f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
}
