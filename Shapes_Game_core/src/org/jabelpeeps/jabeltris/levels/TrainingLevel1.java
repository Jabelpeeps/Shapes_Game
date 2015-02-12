package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.PlayAreaInput;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.SquareBlue;
import org.jabelpeeps.jabeltris.shapes.SquareRed;
import org.jabelpeeps.jabeltris.shapes.SquareYellow;

import com.badlogic.gdx.math.Matrix4;

public class TrainingLevel1 extends LevelMaster {

	private Shape newShape;
	private final Matrix4 coreCameraSettings = Core.initialMatrix;
	
// ---------------------------------------------Constructor(s)--------	
	public TrainingLevel1(Core g) {
		super(g);
		x = 7;
		y = 7;
		camera.position.set(2*x, 2*y, 0);              // NB this changes screen coordinates for e.g. displaying text 
		setLogic();
		setupInput(new BorderButtonsInput(logic), new PlayAreaInput(logic, x, y));
		startInteractiveLogicThread();
	}
// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
		
		if ( !gameLogic.isAlive() ) {
			camera.combined.set(coreCameraSettings);
		}
		prepScreenAndCamera();		
		batch.begin();
		font.draw(batch, "Sets wiped:- " + logic.getTotalMatches(), 0, 40);
	    batch.end();
		renderBoard();                 
	}
	
	@Override
	public Shape makeNewShape(int x, int y) {
				
		int option = rand.nextInt(3) + 1;
		switch (option) {
			case 1:                       
				newShape = new SquareRed();
				break;
			case 2:                       
				newShape = new SquareBlue();
				break;
			case 3:                       
				newShape = new SquareYellow();				
				break;
		}
		Shape tmpShape = setOriginAndBounds(newShape, x , y);
		return tmpShape;
	}
	@Override
	public boolean IsFinished() {
		if ( logic.getNumberOfHints() <= 0 ) {
			return true;
		}
		return false;
	}
	@Override
	public LevelMaster nextLevel() {
			return null;
	}
}
