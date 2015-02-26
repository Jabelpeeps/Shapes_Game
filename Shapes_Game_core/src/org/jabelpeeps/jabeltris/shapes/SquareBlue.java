package org.jabelpeeps.jabeltris.shapes;

import com.badlogic.gdx.graphics.Colors;

public class SquareBlue extends SquareAbstract {

	public SquareBlue() {
		super();
		type = type + "Blue";
		deselect();
	}
	@Override
	public void select() {
		this.setColor(Colors.get("DARK_BLUE"));
	}
	@Override
	public void deselect() {
		this.setColor(Colors.get("BLUE"));
	}
}
