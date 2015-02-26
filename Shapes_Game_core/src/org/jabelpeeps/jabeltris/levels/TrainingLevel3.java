package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.HorizontalLineBlue;
import org.jabelpeeps.jabeltris.shapes.HorizontalLineRed;
import org.jabelpeeps.jabeltris.shapes.VerticalLineGreen;
import org.jabelpeeps.jabeltris.shapes.VerticalLineYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class TrainingLevel3 extends TrainingLevelAbstract {

// ---------------------------------------------Constructors--------	
	public TrainingLevel3(Core c) {
		this(c, true, false);
	}
	public TrainingLevel3(Core c, boolean playNext) {	
		this(c, playNext, false);
	}
	public TrainingLevel3(Core c, boolean playNext, boolean learningLevels) {
		super(c);
		playOn = playNext;
		playingLearningLevels = learningLevels;
		baseColor = new Color(1f, 1f, 1f, 1f);
		title = "Demo Level 3\nLines";
		
		game = new PlayArea(8, 8, this);
		logic = new DemoGameLogic(game);
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}
// ---------------------------------------------Methods----------
	@Override
	public Shape makeNewShape(int x, int y) {
		
		Shape newShape = null;	
		int option = rand.nextInt(4) + 1;
		switch (option) {
			case 1:                       
				newShape = new HorizontalLineRed();
				break;
			case 2:                       
				newShape = new HorizontalLineBlue();
				break;
			case 3:                       
				newShape = new VerticalLineYellow();				
				break;
			case 4:
				newShape = new VerticalLineGreen();
				break;
		}
		return newShape;
	}
	
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			doWhenBackKeyPressed();
			return;
		}
		
		prepScreenAndCamera();
		renderBoard(alpha);
		
		switch ( demoStage ) {
		case 1:
			batch.begin();
			messageCentered( title + "\n\n"
					+ "Our Lines match in groups of 3, but only in their "
					+ "preferred direction. (The way the shape "
					+ "is pointing.)", 48);
			case1commonTasks();
			batch.end();
			break;
		case 2:
			batch.begin();
			case2commonTasks();
			batch.end();
			break;
		case 3:
			batch.begin();
			case3commonTasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 4:
			batch.begin();
			case4commonTasks();
			batch.end();
			Core.delay(500);
			Gdx.graphics.requestRendering();
			break;
		case 5:
			case5commonTasks();
			break;
		case 6:
		case 7:
			int matches = game.getTotalMatches();
			batch.begin();
			messageCentered("Sets Matched:- " + matches, 46);
			if ( demoStage == 6 ) {
				messageCentered("Target:- 20", 40);
				if ( matches < 6 ) {
					messageCentered("Touch and drag shapes where you want them to go.", 2);
				} else if ( matches < 11 ) {
					messageCentered("[#FFD700]Pro Tip[]:- Look for moves that make more than one match.", 4);
				} else if ( matches < 16 ) {
					messageCentered("Multiple matches will score [#FFD700]extra points[] in the scoring levels.", 4);
				} else if ( matches < 20 ) {
					messageCentered("Almost there...", 2);
				} else if ( matches > 19 ) {
					demoStage = 7;
					logic.setEndlessPlayMode(false);
				}
				batch.end();
				return;
			}
			if ( demoStage == 7 ) {
				finalMessages();
				batch.end();
		    	if ( !levelIsFinished ) {
					levelNeedsFinishing();
		    	} else {
					alpha = (alpha > 0f) ? (alpha - 0.01f) : 0f ;
					
					if ( !logic.isAlive() && alpha <= 0f ) {
			    		Core.delay(200);
						if ( playingLearningLevels ) {
							core.setScreen(new TrainingLevel1(core, true, true));
		//				} else if ( playOn ) {
		//					core.setScreen(new ChallengeLevel2(core, true));
						} else {	
							core.setScreen(new MainMenu(core, 4));
						}
						dispose();
					}
					Gdx.graphics.requestRendering();
		    	}
			}
		}
	}
}
