package org.jabelpeeps.jabeltris.shapes;

public class MirrorEll extends MirrorEllAbstract {

	public MirrorEll() {
		super();
	}
	public MirrorEll(String color) {
		super();
		setupColors(color);
		deselect();
	}
}
