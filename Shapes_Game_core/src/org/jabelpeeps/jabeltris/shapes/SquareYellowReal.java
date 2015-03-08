package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class SquareYellowReal extends SquareAbstract {

	public SquareYellowReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareYellowReal";
		selected = Colors.get("DARK_YELLOW");
		deselected = Colors.get("YELLOW");
		deselect();
	}
}
