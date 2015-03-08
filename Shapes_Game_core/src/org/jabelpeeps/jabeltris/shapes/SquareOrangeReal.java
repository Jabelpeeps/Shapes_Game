package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class SquareOrangeReal extends SquareAbstract {

	public SquareOrangeReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareOrangeReal";
		selected = Colors.get("DARK_ORANGE");
		deselected = Colors.get("ORANGE");
		deselect();
	}
}
