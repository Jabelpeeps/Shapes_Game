package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareRedReal extends SquareRed {

	public SquareRedReal() {
		this.setRegion(LevelMaster.realsquare);
		type = "SquareRedReal";
		deselect();
	}
}
