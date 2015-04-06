package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class TriangleRealT extends TriangleAbstract {

	public TriangleRealT() {
		setRegion(LevelMaster.realt);
		type = "TriangleRealT";
		animatable = true;
		animate();
	}
	public TriangleRealT(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
