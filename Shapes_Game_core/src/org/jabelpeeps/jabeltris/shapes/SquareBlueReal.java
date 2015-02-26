package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareBlueReal extends SquareBlue {

	public SquareBlueReal() {
		this.setRegion(LevelMaster.realsquare);
		type = "SquareBlueReal";
		deselect();
	}
}
