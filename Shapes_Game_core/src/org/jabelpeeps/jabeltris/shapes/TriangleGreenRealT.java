package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class TriangleGreenRealT extends TriangleAbstract {

	public TriangleGreenRealT() {
		setRegion(LevelMaster.realt);
		type = "TriangleGreenRealT";
		selected = Colors.get("DARK_GREEN");
		deselected = Colors.get("GREEN");
		deselect();
		animate();
	}
}
