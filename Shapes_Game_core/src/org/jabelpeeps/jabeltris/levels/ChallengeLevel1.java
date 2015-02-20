package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayAreaInput;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.SquareBlue;
import org.jabelpeeps.jabeltris.shapes.SquareRed;
import org.jabelpeeps.jabeltris.shapes.TriangleGreen;
import org.jabelpeeps.jabeltris.shapes.TriangleYellow;

import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel1 extends LevelMaster{

	private float alpha = 0.3f;
	private boolean playOn = true;
	private boolean levelIsFinished = false;
	
// ---------------------------------------------Constructors--------	
	
	public ChallengeLevel1(Core c) {
		this(c, true);
	}

	public ChallengeLevel1(Core c, boolean playNext) {
		super(c);
		playOn = playNext;
		baseColor = new Color(1f, 1f, 1f, 1f);
		x = 7;
		y = 7;
		initPlayArea();
		logic = new InteractiveGameLogic(game);
		logic.setEndlessPlayModeOn();
		setupInput(new BorderButtonsInput(game, logic), new PlayAreaInput(game, logic));
		logic.start();
	}

	@Override
	public void render(float delta) {
		if ( logic.getBackKeyWasPressed() && !logic.isAlive() ) {
			core.setScreen(new MainMenu(core));	
			dispose();
			return;
		}

		prepScreenAndCamera();
		renderBoard();
	}

	@Override
	public Shape makeNewShape(int x, int y) {
		Shape newShape = null;	
		int option = rand.nextInt(4) + 1;
		switch (option) {
			case 1:                       
				newShape = new SquareRed();
				break;
			case 2:                       
				newShape = new SquareBlue();
				break;
			case 3:                       
				newShape = new TriangleYellow();				
				break;
			case 4:
				newShape = new TriangleGreen();
				break;
		}
		newShape.setOriginAndBounds(x , y);
		return newShape;
	}
	@Override
	public boolean IsFinished() {
		return levelIsFinished;
	}
}
