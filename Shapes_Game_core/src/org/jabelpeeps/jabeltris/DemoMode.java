package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.shapes.CrossOneInverted;
import org.jabelpeeps.jabeltris.shapes.CrossTwoInverted;
import org.jabelpeeps.jabeltris.shapes.LineInverted;
import org.jabelpeeps.jabeltris.shapes.SquareInverted;
import org.jabelpeeps.jabeltris.shapes.TriangleInverted;

public class DemoMode extends LevelMaster {
	
	private Shape newShape;
	
	public DemoMode(Core g) {
		super(g);
		setLogic();
		startDemoLogicThread();
		
	}
	@Override
	public void render(float delta) {
		prepScreenAndCamera();	
		
		renderBoard();  
	}
	
	@Override
	public Shape makeNewShape(int x, int y) {
		
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
