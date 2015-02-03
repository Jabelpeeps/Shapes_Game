package org.jabelpeeps.jabeltris;

class Level1 extends Master {
	

	private static Shape newShape;
	
	@Override
	Shape makeNewShape(int x, int y) {
				
		int option = rand.nextInt(5) + 1;
		switch (option) {
			case 1:                       // line
				newShape = new Line();
				break;
			case 2:                       // crossOne ('+')
				newShape = new CrossOne();
				break;
			case 3:                       // crossTwo ('x')
				newShape = new CrossTwo();				
				break;
			case 4:                       // triangle
				newShape = new Triangle();				
				break;
			case 5:                       // square
				newShape = new Square();				
				break;
			default:    // should never be used, but stops errors in IDE.
				System.out.println("Default case in Master.makeNew() triggered.");
				newShape = null;
				break;
		}
		newShape = setOriginScaleAndBounds(newShape, x , y);
		return newShape;
	}

}
