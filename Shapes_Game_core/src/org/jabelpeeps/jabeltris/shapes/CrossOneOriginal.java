package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class CrossOneOriginal extends CrossOneAbstract {
	
	public CrossOneOriginal() {
		setRegion(LevelMaster.crossone);
		type = "crossone";
		selected = new Color(0f, 1f, 0f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
