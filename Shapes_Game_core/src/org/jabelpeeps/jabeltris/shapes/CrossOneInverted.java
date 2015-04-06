package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class CrossOneInverted extends CrossOneAbstract {
	
	public CrossOneInverted() {
		setRegion(LevelMaster.invcrone);
		type = "CrossOneInverted";
		selected = new Color(0f, 1f, 0.5f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
