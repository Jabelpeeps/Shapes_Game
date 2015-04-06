package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

public class Triangle extends TriangleAbstract {

	public Triangle() {
		setRegion(LevelMaster.greytri);
		type = "Triangle";
	}
	public Triangle(String color) {
		this();
		setupColors(color);
		deselect();
	}
}
