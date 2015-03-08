package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class SquareRedReal extends SquareAbstract {

	public SquareRedReal() {
		this.setRegion(LevelMaster.realsquare);
		type = "SquareRedReal";
		selected = Colors.get("DARK_RED");
		deselected = Colors.get("RED");
		deselect();
	}
}
