package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class CrossTwoInverted extends CrossTwoAbstract {

	public CrossTwoInverted() {
		this.setRegion(LevelMaster.invcrtwo);
		type = "CrossTwoInverted";
		selected = new Color(0f, 0.75f, 0f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
