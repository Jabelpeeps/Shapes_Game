package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareYellowReal extends SquareYellow {

	public SquareYellowReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareYellowReal";
		deselect();
	}
}
