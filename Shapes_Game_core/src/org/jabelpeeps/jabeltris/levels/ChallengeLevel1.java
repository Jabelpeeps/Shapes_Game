package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.SquareBlue;
import org.jabelpeeps.jabeltris.shapes.SquareRed;
import org.jabelpeeps.jabeltris.shapes.TriangleGreen;
import org.jabelpeeps.jabeltris.shapes.TriangleYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel1 extends ChallengeLevelAbstract {
		
// ---------------------------------------------Constructors--------	

	public ChallengeLevel1(Core c) {
		this(c, true);
	}
	public ChallengeLevel1(Core c, boolean playNext) {
		super(c);
		playOn = playNext;
		baseColor = new Color(0.75f, 1f, 1f, 1f);
		title = "Challenge Level 1\nMix Up!";
		
		game = new PlayArea(7, 7, this);
		logic = new InteractiveGameLogic(game);
		logic.waitForStartSignal();
		logic.setEndlessPlayMode(true);
		logic.start();
	}
// ---------------------------------------------Methods--------------
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
		return newShape;
	}
	
	@Override
	public void render(float delta) {
		
		if ( logic.getBackKeyWasPressed() ) {
			doWhenBackKeyWasPressed();	
			return;
		}
		
		prepScreenAndCamera();
		batch.begin();
		renderBoard(alpha);
		
		switch ( levelStage ) {
		case 1:
			messageCentered( title + "\n\n"
					+ "Ok.\n"
					+ "No demo this time.\n"
					+ "You know these shapes. "
					+ "Your target is 30 matches, and I'll be keeping score too...", 48);
			
			case1commonTasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 2:
			case2commonTasks();
			batch.end();
			Gdx.graphics.requestRendering();
			break;
		case 3:
			score = game.getScore();
		case 7:
			int matches = game.getTotalMatches();
			messageCentered("Score:- " + score, 48);
			if ( levelStage == 3 ) {
				messageCentered("Sets Matched:- " + matches + "/30", 42);
				if ( matches < 6 ) {
					messageCentered("", 2);
				} else if ( matches < 11 ) {
					messageCentered("[#FFD700]Pro Tip[]:- ", 4);
				} else if ( matches < 16 ) {
					messageCentered("", 4);
				} else if ( matches < 20 ) {
					messageCentered("", 2);
				} else if ( matches > 29 ) {
					levelStage = 7;
					logic.setEndlessPlayMode(false);
				}	
			    batch.end();
			    return;
			}
			if ( levelStage == 7 ) {
				finalMessages();
			    batch.end();
		    	if ( !levelIsFinished ) {
		    		levelNeedsFinishing();
				} else {
					alpha = (alpha > 0f) ? (alpha - 0.01f) : 0f ;
			    	
			    	if ( !logic.isAlive() && alpha <= 0f ) {
			    		Core.delay(200);
						if ( playOn ) {
							core.setScreen(new TrainingLevel3(core));
						} else {	
							core.setScreen(new MainMenu(core));
						}
						dispose();
					}
					Gdx.graphics.requestRendering();
				}
			}
		}
	}
}
