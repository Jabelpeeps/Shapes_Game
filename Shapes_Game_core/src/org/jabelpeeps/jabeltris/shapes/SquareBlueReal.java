package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class SquareBlueReal extends SquareAbstract {

	public SquareBlueReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareBlueReal";
		selected = Colors.get("DARK_BLUE");
		deselected = Colors.get("BLUE");
		deselect();
	}
}
