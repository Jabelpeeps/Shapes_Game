package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class TriangleInverted extends TriangleAbstract {
	
	public TriangleInverted() {
		setRegion(LevelMaster.invtri);
		type = "TriangleInverted";
		selected = new Color(0.25f, 0.5f, 1f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
