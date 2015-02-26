package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.InteractiveGameLogic;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;
import org.jabelpeeps.jabeltris.shapes.CrossTwoBlue;
import org.jabelpeeps.jabeltris.shapes.CrossTwoGreen;
import org.jabelpeeps.jabeltris.shapes.CrossTwoMagenta;
import org.jabelpeeps.jabeltris.shapes.CrossTwoOrange;
import org.jabelpeeps.jabeltris.shapes.CrossTwoRed;
import org.jabelpeeps.jabeltris.shapes.CrossTwoYellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class ChallengeLevel2 extends ChallengeLevelAbstract {

// ---------------------------------------------Constructors--------	
	
	public ChallengeLevel2(Core c) {
		this(c, true);
	}
	public ChallengeLevel2(Core c, boolean playNext) {
		super(c);
		playOn = playNext;
		baseColor = new Color(0.75f, 1f, 1f, 1f);
		title = "Challenge Level 2\n";
		
		game = new PlayArea(this);
		logic = new InteractiveGameLogic(game);
		logic.waitForStartSignal();
		logic.setEndlessPlayMode(true);
		logic.start();
	}
// ---------------------------------------------Methods--------------
		@Override
		public Shape makeNewShape(int x, int y) {
			
			Shape newShape = null;	
			int option = rand.nextInt(6) + 1;
			switch (option) {
				case 1:                       
					newShape = new CrossTwoBlue();
					break;
				case 2:                       
					newShape = new CrossTwoGreen();
					break;
				case 3:                       
					newShape = new CrossTwoOrange();				
					break;
				case 4:
					newShape = new CrossTwoMagenta();
					break;
				case 5:
					newShape = new CrossTwoRed();
					break;
				case 6:
					newShape = new CrossTwoYellow();
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
						+ "\n"
						+ "\n"
						+ ""
						+ "", 48);
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
				messageCentered("Score:- " + score, 50);
				if ( levelStage == 3 ) {
					messageCentered("Sets Matched:- " + matches + "/30", 45);
					if ( matches < 6 ) {
						messageCentered("", 2);
					} else if ( matches < 11 ) {
						messageCentered("[#FFD700]Pro Tip[]:- ", -2);
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
