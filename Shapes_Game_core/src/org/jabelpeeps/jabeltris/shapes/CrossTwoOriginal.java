package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.LevelMaster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class CrossTwoOriginal extends CrossTwoAbstract {
	
	public CrossTwoOriginal() {
			this.setRegion(LevelMaster.crosstwo);
			type = "crosstwo";
			selected = new Color(0f, 0f, 1f, 1f);
			deselected = Colors.get("WHITE");
			deselect();
	}
}
