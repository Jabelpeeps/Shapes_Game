package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOneOriginal;
import org.jabelpeeps.jabeltris.shapes.CrossTwoOriginal;
import org.jabelpeeps.jabeltris.shapes.HorizontalLineOriginal;
import org.jabelpeeps.jabeltris.shapes.SquareOriginal;
import org.jabelpeeps.jabeltris.shapes.TriangleOriginal;
import org.jabelpeeps.jabeltris.shapes.VerticalLineOriginal;

import com.badlogic.gdx.graphics.Color;

public class EndlessLevelClassic extends EndlessGame {
	
	public EndlessLevelClassic() {
		this(false);
	}
	public EndlessLevelClassic(boolean initialise) {
		super(initialise);
		baseColor = new Color(0xDDA0DDFF);
	}

	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(9) + 1 ) {
		case 1: 
			return new HorizontalLineOriginal();
		case 2:
		case 8:
			return new CrossOneOriginal();
		case 3:
		case 9:
			return new CrossTwoOriginal();				
		case 4: 
		case 5:
			return new TriangleOriginal();				
		case 6: 
			return new SquareOriginal();
		case 7:
			return new VerticalLineOriginal();
		}
		return null;
	}
}
