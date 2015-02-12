package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOneOriginal;
import org.jabelpeeps.jabeltris.shapes.CrossTwoOriginal;
import org.jabelpeeps.jabeltris.shapes.LineOriginal;
import org.jabelpeeps.jabeltris.shapes.SquareOriginal;
import org.jabelpeeps.jabeltris.shapes.TriangleOriginal;

public class EndlessLevelClassic extends EndlessGame {

	private static Shape newShape;
	
	public EndlessLevelClassic(Core g) {
		super(g);
	}

	@Override
	public Shape makeNewShape(int x, int y) {
		
		int option = rand.nextInt(5) + 1;
		switch (option) {
			case 1:                       
				newShape = new LineOriginal();
				break;
			case 2:                       
				newShape = new CrossOneOriginal();
				break;
			case 3:                       
				newShape = new CrossTwoOriginal();				
				break;
			case 4:                       
				newShape = new TriangleOriginal();				
				break;
			case 5:                       
				newShape = new SquareOriginal();				
				break;
		}
		Shape tmpShape = setOriginAndBounds(newShape, x , y);
		return tmpShape;
	}

	@Override
	public LevelMaster nextLevel() {
		return null;
	}
	@Override
	public boolean IsFinished() {
		return false;
	}
}
