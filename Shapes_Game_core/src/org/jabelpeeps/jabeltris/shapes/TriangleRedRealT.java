package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class TriangleRedRealT extends TriangleRed {

	public TriangleRedRealT() {
		this.setRegion(LevelMaster.realt);
		type = "TriangleRedRealT";
		selected = Colors.get("DARK_RED");
		deselected = Colors.get("RED");
		deselect();
		animate();
	}
}
