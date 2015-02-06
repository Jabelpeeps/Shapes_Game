package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOne;
import org.jabelpeeps.jabeltris.shapes.CrossTwo;
import org.jabelpeeps.jabeltris.shapes.Line;
import org.jabelpeeps.jabeltris.shapes.Square;
import org.jabelpeeps.jabeltris.shapes.Triangle;

public class Level1 extends Master {
	
   	private static Shape newShape;
   	
//   	public Level1 (){
//   	}
	
	@Override
	public Shape makeNewShape(int x, int y) {
				
		int option = rand.nextInt(5) + 1;
		switch (option) {
			case 1:                       
				newShape = new Line();
				break;
			case 2:                       
				newShape = new CrossOne();
				break;
			case 3:                       
				newShape = new CrossTwo();				
				break;
			case 4:                       
				newShape = new Triangle();				
				break;
			case 5:                       
				newShape = new Square();				
				break;
			default:                              // should never be used, but stops errors in IDE.
				System.out.println("Default case in Level1.makeNew() triggered.");
				newShape = null;
				break;
		}
		newShape = setOriginAndBounds(newShape, x , y);
		return newShape;
	}
	
	Master nextLevel() {
			return null;
		}
}
