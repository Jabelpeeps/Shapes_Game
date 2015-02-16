package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.levels.EndlessLevelClassic;
import org.jabelpeeps.jabeltris.levels.EndlessLevelDark;
import org.jabelpeeps.jabeltris.levels.TrainingLevel1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class MainMenu extends Core implements Screen {

// -------------------------------------------------Fields---------	
	private final Core game;
	Vector3 touch = new Vector3();
	char key;
	private int menuScreen = 1;
	LevelMaster level;
	private String keyPressed = "none";
// ------------------------------------------------Constructor-----
	public MainMenu(final Core g) {
		this(g, 1);
	}
	public MainMenu(final Core g, int menu) {
		game = g;
		menuScreen = menu;
		Gdx.graphics.requestRendering();	
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			@Override
			public boolean touchDown(int x, int y, int p, int b) {
				keyPressed = "none";
				touch.set(x, y, 0);
				camera.update();
				batch.setProjectionMatrix(camera.combined);
				Core.camera.unproject(touch);
				return switchCase();
			}
			@Override
			public boolean keyTyped(char typed) {
				keyPressed = "none";
				if ( typed == '' ) {
					keyPressed = "BACK";
					touch.set(0, 0, 0);
				}
				return switchCase();
			}
			
			private boolean switchCase() {
				switch ( menuScreen ) {
				case 1:
					optionsCase1();
					break;
				case 2:
					optionsCase2();
					break;
				case 5:
					optionsCase5();
					break;
				case 8:
					optionsCase8();
					break;
				default:
					break;
				}
				return true;
			}
		});
		Gdx.input.setCatchBackKey(true);	
	}
// ------------------------------------------------Methods---------	
	@Override
	public void render(float delta) {
		Gdx.graphics.setContinuousRendering(false);
		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		switch ( menuScreen ) {
		case 1:
			displayCase1();
			break;
		case 2:
			displayCase2();	
		    break;
		case 3:
			game.setScreen(new EndlessLevelClassic(game) );
			break;
		case 4:
			game.setScreen(new EndlessLevelDark(game) );
			break;
		case 5:
			displayCase5();	
		    break;
		case 6:
			game.setScreen(new TrainingLevel1(game));
			break;
		case 7:
			game.setScreen(new DemoMode(game));
			break;
		case 8:
			displayCase8();
			break;
		}
	}
// -------------------------------------------MenuLevel 1------------
	private void displayCase1() {
			batch.begin();
			font.draw(batch, "Welcome to ........", 4, 42);
			font.draw(batch, "Select option:", 4, 36);
			font.draw(batch, "- Learning Levels", 4, 30);
			font.draw(batch, "- Endless Play", 4, 26);
			font.draw(batch, "...further options TBC", 2, 0);
			batch.end();
	}
	private void optionsCase1() {
		if ( touchY(30) ) menuScreen = 5;
		if ( touchY(26) ) menuScreen = 2;
		if ( touchY(20) ) menuScreen = 7;
	}
// -------------------------------------------MenuLevel 2------------
	private void displayCase2() {
			batch.begin();
			font.draw(batch, "Endless Play", 6, 48);
			font.draw(batch, "______________", 6, 47);
			font.draw(batch, "For practice...", 4, 40);
			font.draw(batch, "...or meditation", 8, 36);
			font.draw(batch, "The board will be", 4, 30);
			font.draw(batch, "shuffled if no more", 4, 26);
			font.draw(batch, "swaps are possible.", 4, 22);
			font.draw(batch, "Select Shape set:-", 4, 16);
			font.draw(batch, "- Light", 8, 10);
			font.draw(batch, "- Dark", 8, 6);
			font.draw(batch, "Go Back", 22, -6);
			batch.end();
	}
	private void optionsCase2() {
		if ( touchY(10) ) menuScreen = 3;
		if ( touchY(6) ) menuScreen = 4;
		if ( touchY(-6) || keyPressed == "BACK" ) menuScreen = 1;
	}
// ------------------------------------------MenuLevel 5----------	
	private void displayCase5() {
			batch.begin();
			font.draw(batch, "Learning Levels", 6, 48);
			font.draw(batch, "__________________", 6, 47);
			font.draw(batch, "To Learn how the", 2, 40);
			font.draw(batch, "shapes move, before", 2, 36);
			font.draw(batch, "trying harder levels.", 2, 32);
			font.draw(batch, "Each level will start", 2, 26);
			font.draw(batch, "by showing you the", 2, 22);
			font.draw(batch, "matching pattern for", 2, 18);
			font.draw(batch, "each shape, before", 2, 14);
			font.draw(batch, "letting you practise.", 2, 10);
			font.draw(batch, "- Play Through All", 4, 4);
			font.draw(batch, "- Choose a Shape.", 4, 0);
			font.draw(batch, "Go Back", 22, -6);
			batch.end();
	}
	private void optionsCase5() {
		if ( touchY(4) ) menuScreen = 6;
		if ( touchY(0) ) menuScreen = 8;
		if ( touchY(-6) || keyPressed == "BACK" ) menuScreen = 1;
	}
// ------------------------------------------MenuLevel 8----------	
		private void displayCase8() {
				batch.begin();
				font.draw(batch, "Learning Levels", 6, 48);
				font.draw(batch, "__________________", 6, 47);
				Sprite square = new Sprite(LevelMaster.square);
				Sprite invsquare = new Sprite(LevelMaster.invsqr);
				square.setBounds(2, 37, 4, 4);
				invsquare.setBounds(4, 35, 4, 4);
				square.draw(batch);
				invsquare.draw(batch);
				font.draw(batch, "- Squares", 8, 40);
//				font.draw(batch, " ", 2, 36);
//				font.draw(batch, " ", 2, 32);
//				font.draw(batch, " ", 2, 26);
//				font.draw(batch, " ", 2, 22);
//				font.draw(batch, " ", 2, 18);
//				font.draw(batch, " ", 2, 14);
//				font.draw(batch, " ", 2, 10);
//				font.draw(batch, " ", 4, 4);
//				font.draw(batch, " ", 4, 0);
				
				
				
				
				batch.end();
		}
		private void optionsCase8() {
			if ( touchY(40) ) menuScreen = 6;
			//if ( touchY(0) ) menuScreen = 8;
			if ( touchY(-6) || keyPressed == "BACK" ) menuScreen = 5;
		}	
	
	
// -------------------------------------------------------------------
	private boolean touchY(int min) {
		return touchY(min, min+4);
	}
	private boolean touchY(int min, int max) {
		// incorporates corrections to align coordinates with batch drawn fonts.
		if ( touch.y > min-4 && touch.y < max-4) return true;
		return false;
	}
	@Override
	public void hide() {
	}
	@Override
	public void show() {
	}
}
