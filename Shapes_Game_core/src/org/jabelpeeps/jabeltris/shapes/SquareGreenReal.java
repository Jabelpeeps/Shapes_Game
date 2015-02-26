package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareGreenReal extends SquareGreen {

	public SquareGreenReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareGreenReal";
		deselect();
	}
}
