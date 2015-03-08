package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class SquareGreenReal extends SquareAbstract {

	public SquareGreenReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareGreenReal";
		selected = Colors.get("DARK_GREEN");
		deselected = Colors.get("GREEN");
		deselect();
	}
}
