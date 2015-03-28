package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOneInverted;
import org.jabelpeeps.jabeltris.shapes.CrossTwoInverted;
import org.jabelpeeps.jabeltris.shapes.HorizontalLineInverted;
import org.jabelpeeps.jabeltris.shapes.SquareInverted;
import org.jabelpeeps.jabeltris.shapes.TriangleInverted;
import org.jabelpeeps.jabeltris.shapes.VerticalLineInverted;

import com.badlogic.gdx.graphics.Color;

public class EndlessLevelDark extends EndlessGame {
	
	public EndlessLevelDark() {
		this(false);
	}
	public EndlessLevelDark(boolean initialise ) {
		super(initialise);
		baseColor = new Color(0.75f, 1f, 0.75f, 1f);
	}
	
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(9) + 1 ) {
			case 1: 
				return new HorizontalLineInverted();
			case 2:
			case 8:
				return new CrossOneInverted();
			case 3:
			case 9:
				return new CrossTwoInverted();				
			case 4: 
			case 5:
				return new TriangleInverted();				
			case 6: 
				return new SquareInverted();
			case 7:
				return new VerticalLineInverted();
		}
		return null;
	}
}
