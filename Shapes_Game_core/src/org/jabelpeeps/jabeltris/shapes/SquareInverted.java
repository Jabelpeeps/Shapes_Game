package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class SquareInverted extends SquareAbstract {

	public SquareInverted() {
		this.setRegion(LevelMaster.invsqr);
		type = "SquareInverted";
		selected = new Color(0f, 0.25f, 0.75f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
