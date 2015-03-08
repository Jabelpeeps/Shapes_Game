package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class SquareOriginal extends SquareAbstract {

	public SquareOriginal() {
		this.setRegion(LevelMaster.square);
		type = "Square";
		selected = new Color(0f, 0.5f, 1f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
