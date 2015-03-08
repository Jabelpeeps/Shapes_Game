package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class TriangleOriginal extends TriangleAbstract {
	
	public TriangleOriginal() {
		setRegion(LevelMaster.triangle);
		type = "triangle";
		selected = new Color(1f, 0f, 1f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
