package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.levels.ChallengeLevel2;
import org.jabelpeeps.jabeltris.levels.EndlessLevelClassic;
import org.jabelpeeps.jabeltris.levels.EndlessLevelDark;
import org.jabelpeeps.jabeltris.levels.TrainingLevel1;
import org.jabelpeeps.jabeltris.levels.TrainingLevel2;
import org.jabelpeeps.jabeltris.levels.TrainingLevel3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class MainMenu extends Core implements Screen {

// -------------------------------------------------Fields---------	
	private final Core core;
	Vector3 touch = new Vector3();
	private int menuScreen = 1;
	LevelMaster level;
	private String keyPressed = "none";
// ------------------------------------------------Constructor-----
	public MainMenu(final Core g) {
		this(g, 1);
	}
	public MainMenu(final Core g, int menu) {
		core = g;
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
			public boolean keyUp(int typed) {
				keyPressed = "none";
				if ( typed == Keys.BACK || typed == Keys.BACKSPACE ) {
					keyPressed = "BACK";
					touch.set(0, 0, 0);
				}
				return switchCase();
			}
			private boolean switchCase() {
				switch ( menuScreen ) {
				case 0:
					optionsCase0();
					break;
				case 1:
					optionsCase1();
					break;
				case 2:
					optionsCase2();
					break;
				case 3:
					optionsCase3();
					break;
				case 4:
					optionsCase4();
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
		case 0:
			displayCase0();
			break;
		case 1:
			displayCase1();
			break;
		case 2:
			displayCase2();	
		    break;
		case 3:
			displayCase3();	
		    break;
		case 4:
			displayCase4();
			break;
		}
	}
// -------------------------------------------MenuLevel 0------------
	private void displayCase0() {
		batch.begin();
		messageCentered("<Insert Name of Game Here[]> "
					+ "by Jabelpeeps Productions.", 48);
		messageLeft("Select either:-", 24);
		messageCentered("- [#FFD700]Quick Start[]", 18);
		messageCentered("- [#FFD700]Main Menu[]", 12);
		messageRight("Exit", -4);
		batch.end();
	}
	private void optionsCase0() {
		if ( touchY(18) ) core.setScreen(new ChallengeLevel2(core, false));
		if ( touchY(12) ) menuScreen = 1;
		if ( touchY(-4) ) Gdx.app.exit();
	}
// -------------------------------------------MenuLevel 1------------
	private void displayCase1() {
			batch.begin();
			messageCentered("Main Menu", 48);
			messageCentered("_____________", 47);
			messageLeft("Select option:\n"
							+ "- Full Game\n"
							+ "- Learning Levels\n"
							+ "- Endless Play\n"
							+ "- Wall-paper Mode.", 32);
			messageRight("Exit", 0);
			batch.end();
	}
	private void optionsCase1() {
		if ( touchY(28) ) core.setScreen(new TrainingLevel1(core));
		if ( touchY(24) ) menuScreen = 3;
		if ( touchY(20) ) menuScreen = 2;
		if ( touchY(16) ) core.setScreen(new DemoMode(core));
		if ( touchY(0) ) Gdx.app.exit();
	}
// -------------------------------------------MenuLevel 2------------
	private void displayCase2() {
			batch.begin();
			messageCentered("Endless Play", 48);
			messageCentered("______________", 47);
			messageLeft("For practice...", 40);
			messageRight("...or meditation", 36);
			messageCentered("The board will be shuffled if no more swaps are possible.", 30);
			font.draw(batch, "Select Shape set:-", 4, 16);
			font.draw(batch, "- Light", 8, 10);
			font.draw(batch, "- Dark", 8, 6);
			font.draw(batch, "Go Back", 22, -6);
			batch.end();
	}
	private void optionsCase2() {
		if ( touchY(10) ) core.setScreen(new EndlessLevelClassic(core) );
		if ( touchY(6) ) core.setScreen(new EndlessLevelDark(core) );
		if ( touchY(-6) || keyPressed == "BACK" ) menuScreen = 1;
	}
// ------------------------------------------MenuLevel 3----------	
	private void displayCase3() {
			batch.begin();
			font.draw(batch, "Learning Levels", 6, 48);
			font.draw(batch, "__________________", 6, 47);
			messageCentered("Forgotten how the shapes match?\n"
					+ "Here you can replay demonstration levels that "
					+ "you have already reached in the full game.", 40);
			font.draw(batch, "- Play Through All", 4, 4);
			font.draw(batch, "- Choose a Shape.", 4, 0);
			font.draw(batch, "Go Back", 22, -6);
			batch.end();
	}
	private void optionsCase3() {
		if ( touchY(4) ) core.setScreen(new TrainingLevel1(core, true, true));
		if ( touchY(0) ) menuScreen = 4;
		if ( touchY(-6) || keyPressed == "BACK" ) menuScreen = 1;
	}
// ------------------------------------------MenuLevel 4----------	
		private void displayCase4() {
				batch.begin();
				font.draw(batch, "Learning Levels", 6, 48);
				font.draw(batch, "__________________", 6, 47);
				
				Sprite line = new Sprite(LevelMaster.line);
				Sprite invline = new Sprite(LevelMaster.invline);
				line.setBounds(2, 25, 3, 3);
				invline.setBounds(3, 24, 3, 3);
				line.draw(batch);
				invline.draw(batch);
				font.draw(batch, "- Lines", 8, 28);				
				
				Sprite triangle = new Sprite(LevelMaster.triangle);
				Sprite invtriangle = new Sprite(LevelMaster.invtri);
				triangle.setBounds(2, 31, 3, 3);
				invtriangle.setBounds(3, 30, 3, 3);
				triangle.draw(batch);
				invtriangle.draw(batch);
				font.draw(batch, "- Triangles", 8, 34);
				
				Sprite square = new Sprite(LevelMaster.square);
				Sprite invsquare = new Sprite(LevelMaster.invsqr);
				square.setBounds(2, 37, 3, 3);
				invsquare.setBounds(3, 36, 3, 3);
				square.draw(batch);
				invsquare.draw(batch);
				font.draw(batch, "- Squares", 8, 40);

				font.draw(batch, "Go Back", 22, -6);
				batch.end();
		}
		private void optionsCase4() {
			if ( touchY(40) ) core.setScreen(new TrainingLevel1(core, false));
			if ( touchY(34) ) core.setScreen(new TrainingLevel2(core, false));
			if ( touchY(28) ) core.setScreen(new TrainingLevel3(core, false));
			
			if ( touchY(-6) || keyPressed == "BACK" ) menuScreen = 3;
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
	protected void messageCentered(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.CENTER);
	}
	protected void messageLeft(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.LEFT);
	}
	protected void messageRight(String message, float y) {
		font.drawWrapped(batch, message, 2, y, 36, BitmapFont.HAlignment.RIGHT);
	}
	@Override
	public void hide() {
	}
	@Override
	public void show() {
	}
	@Override
	public void dispose() {
	}
}
