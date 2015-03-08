package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class TriangleBlueRealT extends TriangleAbstract {

	public TriangleBlueRealT() {
		setRegion(LevelMaster.realt);
		type = "TriangleBlueRealT";
		selected = Colors.get("DARK_BLUE");
		deselected = Colors.get("BLUE");
		deselect();
		animate();
	}
}
