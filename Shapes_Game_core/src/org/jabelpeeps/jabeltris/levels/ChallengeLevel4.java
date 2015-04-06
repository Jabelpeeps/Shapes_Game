package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.FourSwapInput;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.SelectShape;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossOne;
import org.jabelpeeps.jabeltris.shapes.CrossTwo;
import org.jabelpeeps.jabeltris.shapes.Square;
import org.jabelpeeps.jabeltris.shapes.Triangle;

import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel4 extends ChallengeLevelAbstract {
	
	public ChallengeLevel4() {
		super();
		title = "Challenge Level 4";
		firstMessage = title + "\n\n";
	}
	public ChallengeLevel4(boolean playNext) {
		this();
		playOn = playNext;
		game = new PlayArea(9, 9);
		game.baseColor = new Color(0.75f, 1f, 1f, 0.75f);
		game.initialise(this);
		Shape.addHintVisitor( new RotatingSquareHints() );
		logic = new InteractiveGameLogic(game);
		logic.waitForStartSignal();
		logic.setEndlessPlayMode(true);
		logic.start();
	}
// ---------------------------------------------Methods--------------
	@Override
	public Shape getNewShape() {
		
		switch ( rand.nextInt(4) + 1 ) {
			case 1:
				return new Square("Red");
			case 2:
				return new CrossTwo("Blue");
			case 3: 
				return new Triangle("Yellow");
			case 4:
				return new CrossOne("Green");
		}
		return null;
	}
	@Override
	protected void stage1Tasks() {
		levelStage++;
		setupInput(new BorderButtonsInput(game, logic), 
				   new FourSwapInput(game, logic),
				   new SelectShape(game, logic) );
		logic.sendStartSignal();
	}
	@Override
	protected void stage3Tasks(int matches) {
		Core.textCentre("Sets Matched:- " + matches + "/30", Core.topEdge - 10);
		if ( matches < 6 )
			Core.textCentre("", 2);
		else if ( matches < 11 )
			Core.textCentre("[GOLD]Pro Tip[]:- ", 4);
		else if ( matches < 16 )
			Core.textCentre("", 4);
		else if ( matches < 20 )
			Core.textCentre("", 2);
		else if ( matches > 29 ) {
			levelStage = 7;
			logic.setEndlessPlayMode(false);
		}	
	}
	@Override
	protected void nextLevel() {
		core.setScreen(new TrainingLevel3(true));
	}
}
