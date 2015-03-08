package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class SquareMagentaReal extends SquareAbstract {

	public SquareMagentaReal() {
		setRegion(LevelMaster.realsquare);
		type = "SquareMagentaReal";
		selected = Colors.get("DARK_MAGENTA");
		deselected = Colors.get("MAGENTA");
		deselect();
	}

}
