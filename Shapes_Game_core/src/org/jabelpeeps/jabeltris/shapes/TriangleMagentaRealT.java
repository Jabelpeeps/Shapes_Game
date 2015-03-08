package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Colors;

public class TriangleMagentaRealT extends TriangleAbstract {

	public TriangleMagentaRealT() {
		setRegion(LevelMaster.realt);
		type = "TriangleMagentaRealT";
		selected = Colors.get("DARK_MAGENTA");
		deselected = Colors.get("MAGENTA");
		deselect();
		animate();
	}
}
