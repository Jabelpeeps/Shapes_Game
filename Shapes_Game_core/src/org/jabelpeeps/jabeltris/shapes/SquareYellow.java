package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class SquareYellow extends SquareAbstract {
	
	public SquareYellow() {
		super();
		type = type + "Yellow";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(Colors.get("DARK_YELLOW"));
	}
	@Override
	public void deselect() {
		this.setColor(Colors.get("YELLOW"));
	}
}
