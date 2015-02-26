package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public final class MyColors {

	public static void setupColors() {
		Colors.reset();
		Colors.put("RED", new Color(0xDC143CFF));
		Colors.put("DARK_RED", new Color(0xB22222FF));
		
		Colors.put("GREEN", new Color(0x32CD32FF));
		Colors.put("DARK_GREEN", new Color(0x228B22FF));
		
		Colors.put("YELLOW", new Color(0xFFD700FF));
		Colors.put("DARK_YELLOW", new Color(0xBDB76BFF));
		
		Colors.put("BLUE", new Color(0, 0.5f, 1f, 1f));
		Colors.put("DARK_BLUE", new Color(0, 0.25f, 0.5f, 1f));
		
		Colors.put("ORANGE", new Color(0xFF8C00FF));
		Colors.put("DARK_ORANGE", new Color(0xB8860BFF));
		
		Colors.put("MAGENTA", new Color(0x9932CCFF));
		Colors.put("DARK_MAGENTA", new Color(0X800080FF));
		
		Colors.put("GOLD", new Color(0xFFD700FF));
	}
}
