package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOneInverted;
import org.jabelpeeps.jabeltris.shapes.CrossTwoInverted;
import org.jabelpeeps.jabeltris.shapes.LineInverted;
import org.jabelpeeps.jabeltris.shapes.SquareInverted;
import org.jabelpeeps.jabeltris.shapes.TriangleInverted;

import com.badlogic.gdx.graphics.Color;

public class EndlessLevelDark extends EndlessGame {

	public EndlessLevelDark(Core g) {
		super(g);
		baseColor = new Color(0.75f, 1f, 0.75f, 1f);
	}
	
	@Override
	public Shape makeNewShape(int x, int y) {
		Shape newShape = null;	
		int option = rand.nextInt(5) + 1;
		switch (option) {
			case 1:                       
				newShape = new LineInverted();
				break;
			case 2:                       
				newShape = new CrossOneInverted();
				break;
			case 3:                       
				newShape = new CrossTwoInverted();				
				break;
			case 4:                       
				newShape = new TriangleInverted();				
				break;
			case 5:                       
				newShape = new SquareInverted();				
				break;
		}
		newShape.setOriginAndBounds(x , y);
		return newShape;
	}
}
