package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class SquareOrangeReal extends SquareOrange {

	public SquareOrangeReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareOrangeReal";
		deselect();
	}
}
