package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class TriangleOrangeRealT extends TriangleAbstract {

	public TriangleOrangeRealT() {
		setRegion(LevelMaster.realt);
		type = "TriangleOrangeRealT";
		selected = Colors.get("DARK_ORANGE");
		deselected = Colors.get("ORANGE");
		deselect();
		animate();
	}
}
