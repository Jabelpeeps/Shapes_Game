package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOneOriginal;
import org.jabelpeeps.jabeltris.shapes.CrossTwoOriginal;
import org.jabelpeeps.jabeltris.shapes.LineOriginal;
import org.jabelpeeps.jabeltris.shapes.SquareOriginal;
import org.jabelpeeps.jabeltris.shapes.TriangleOriginal;

import com.badlogic.gdx.graphics.Color;

public class EndlessLevelClassic extends EndlessGame {
	
	public EndlessLevelClassic() {
		super();
		baseColor = Color.valueOf("B0C4DE");
	}

	public EndlessLevelClassic(Core g) {
		super(g);
		baseColor = Color.valueOf("B0C4DE");
	}

	@Override
	public Shape makeNewShape(int x, int y) {
		
		switch ( rand.nextInt(5) + 1 ) {
			case 1:
				return new LineOriginal();
			case 2: 
				return new CrossOneOriginal();
			case 3:
				return new CrossTwoOriginal();	
			case 4:
				return new TriangleOriginal();
			case 5:
				return new SquareOriginal();
		}
		return null;
	}
}
