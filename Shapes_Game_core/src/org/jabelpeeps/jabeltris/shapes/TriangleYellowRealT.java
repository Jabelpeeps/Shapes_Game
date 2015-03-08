package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class TriangleYellowRealT extends TriangleAbstract {

	public TriangleYellowRealT() {
		setRegion(LevelMaster.realt);
		type = "TriangleYellowRealT";
		selected = Colors.get("DARK_YELLOW");
		deselected = Colors.get("YELLOW");
		deselect();
		animate();
	}
}
