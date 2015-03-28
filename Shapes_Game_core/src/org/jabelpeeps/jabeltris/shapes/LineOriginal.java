package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class LineOriginal extends LineAbstract {
	
	public LineOriginal() {
		super();
		setRegion(LevelMaster.line);
		type = "line";
		selected = new Color(1f, 0f, 0f, 1f);
		deselected = Colors.get("WHITE");
		deselect();
	}
}
