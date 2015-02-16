package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class CrossTwoInverted extends CrossTwoAbstract {

	public CrossTwoInverted() {
		this.setRegion(LevelMaster.invcrtwo);
		type = "CrossTwoInverted";
		deselect();
}
@Override
public void select() {
	this.setColor(0f, 0.75f, 0f, 1f);
}
@Override
public void deselect() {
	this.setColor(1f, 1f, 1f, 1f);
}
}
