package org.jabelpeeps.jabeltris;

import org.jabelpeeps.jabeltris.levels.ChallengeLevel03;
import org.jabelpeeps.jabeltris.levels.EndlessLevelClassic;
import org.jabelpeeps.jabeltris.levels.EndlessLevelDark;
import org.jabelpeeps.jabeltris.levels.TrainingLevel1;
import org.jabelpeeps.jabeltris.levels.TrainingLevel2;
import org.jabelpeeps.jabeltris.levels.TrainingLevel3;
import org.jabelpeeps.jabeltris.levels.TrainingLevel4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class MainMenu extends Core implements Screen {

//--------------------------------------------------Fields---------	
	private final Core core;
	private Vector3 touch = new Vector3();
	private int menuScreen = 1;
	private int nextScreen = 0;
	private boolean confirmed = false;
	private String keyPressed = "none";
	private FileHandle savedLevel;
	private String confirmationText;
//-------------------------------------------------Constructors-----
	public MainMenu(final Core g) {
		this(g, 1);
	}
	public MainMenu(final Core g, int menu) {
		core = g;
		menuScreen = menu;
		if ( Gdx.files.isLocalStorageAvailable() ) {
			savedLevel = Gdx.files.local("savedLevel.sav");
		}
		Gdx.graphics.setContinuousRendering(true);
		Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() 
		
			{	// Anonymous class for GestureAdapter (an implementation of GestureListener)
				@Override
				public boolean tap(float x, float y, int count, int button) {
					keyPressed = "none";
					touch.set(x, y, 0);
					camera.unproject(touch);
					return true;
				}
			})
			{	// Anonymous class for GestureDetector (which extends InputAdapter)
				@Override
				public boolean keyUp(int typed) {
					keyPressed = "none";
					if ( typed == Keys.BACK || typed == Keys.BACKSPACE ) {
						keyPressed = "BACK";
						touch.set(0, 0, 0);
					}
					return true;
				}		
			});
		Gdx.input.setCatchBackKey(true);	
	}
	public MainMenu(final Core g, int menu, int next) {
		this(g, menu);
		nextScreen = next;
	}
//-------------------------------------------------Methods---------	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		switch ( menuScreen ) {
		case 0:	displayCase0();	break;
		case 1:	displayCase1();	break;
		case 2:	displayCase2(); break;
		case 3:	displayCase3(); break;
		case 4:	displayCase4(); break;
		case 5:	displayCase5();	break;
		case 6:	displayCase6();	break;
		case 7:	displayCase7();	break;
		}
		batch.end();
		touch.set(0, 0, 0);
		
		if ( delta < 0.1f ) delay((long) (100 - delta * 1000) );
	}
//--------------------------------------------MenuLevel 0------------
	private void displayCase0() {

		textCentre("<Insert Game Name Here> "
					+ "by Jabelpeeps Productions.", 48);
		
		font.setScale(0.3f);
		if ( buttonCentre("[GOLD]Quick Start[]", 22) ) core.setScreen(new ChallengeLevel03(false));
		if ( buttonCentre("[GOLD]Main Menu[]", 12) ) menuScreen = 1;
		
		font.setScale(0.2f);
		if ( buttonRight("[RED]Exit[]", -4) || keyPressed == "BACK" ) exit();
	}
//--------------------------------------------MenuLevel 1------------
	private void displayCase1() {

		textHeading("[GOLD]Main Menu", 48);
		
		int place = 7;
		font.setScale(0.25f);
		if ( buttonCentre("[]Full Game", 2 + 5*place--) ) core.setScreen(new TrainingLevel1(true));
		
		font.setScale(0.2f);
		if ( levelCompleted("TrainingLevel1") 
				&& buttonCentre("Replay Demo Levels", 5*place--) ) menuScreen = 3;
		
		if ( buttonCentre("Endless Play", 5*place--) )  menuScreen = 2;
		
		if ( savedLevel.exists() 
				&& buttonCentre("Load Saved Level", 5*place--) ) runSavedLevel();
		
		if ( buttonCentre("Wall-paper Mode", 5*place--) ) core.setScreen(new WallPaperMode());
		
		if ( buttonCentre("Settings", -2 + 5*place--) ) menuScreen = 5;
		
		if ( buttonRight("[RED]Exit[]", -4) || keyPressed == "BACK" ) exit();
	}
//--------------------------------------------MenuLevel 2------------
	private void displayCase2() {

		textHeading("Endless Play", 48);
		
		textLeft("[GREEN]For practice...", 40);
		textRight("...or meditation[]", 36);
		
		textCentre("No Targets,[GREEN] No Scores,[] No Stress.", 30);
		textCentre("[GREEN]Just match & relax![]", 18);
		
		textLeft("Select Shape set:-", 12);
		
		if ( buttonCentre("- Light", 6) ) core.setScreen(new EndlessLevelClassic(true) );
		
		if ( buttonCentre("- Dark", 2) ) core.setScreen(new EndlessLevelDark(true) );
		
		if ( buttonRight("[GREEN]Go Back[]", -6) || keyPressed == "BACK" ) menuScreen = 1;
	}
//-------------------------------------------MenuLevel 3----------	
	private void displayCase3() {
		
		textHeading("[BLUE]Demo Levels", 48);
		
		textCentre("[]Forgotten how the shapes match?\n"
			+ "Here you can replay demo levels that "
			+ "you have reached in the full game.", 40);
		
		if ( buttonCentre("- Play Through All", 4) ) core.setScreen(new TrainingLevel1(true, true));
		
		if ( buttonCentre("- Choose a Shape.", 0) ) menuScreen = 4;
				
		if ( buttonRight("[BLUE]Go Back[]", -6) || keyPressed == "BACK" ) menuScreen = 1;
	}
//-------------------------------------------MenuLevel 4----------	
	private void displayCase4() {

		textHeading("[BLUE]Demo Levels", 48);
		
		if ( levelCompleted("TrainingLevel1") ) {;
			draw(new Sprite(LevelMaster.square), 2, 37);
			draw(new Sprite(LevelMaster.invsqr), 3, 36);
			
			draw("[]- Squares", 8, 40);
			if ( touchY(40) ) core.setScreen(new TrainingLevel1(false));
		}
		
		if ( levelCompleted("TrainingLevel2") ) {			
			draw(new Sprite(LevelMaster.triangle), 2, 31);
			draw(new Sprite(LevelMaster.invtri), 3, 30);
			
			draw("- Triangles", 8, 34);
			if ( touchY(34) ) core.setScreen(new TrainingLevel2(false));
		}
		
		if ( levelCompleted("TrainingLevel3") ) {
			draw(new Sprite(LevelMaster.line), 2, 25);
			draw(new Sprite(LevelMaster.invline), 3, 24);
			
			draw("- Lines", 8, 28);	
			if ( touchY(28) ) core.setScreen(new TrainingLevel3(false));
		}
		
		if ( levelCompleted("TrainingLevel4") ) {
			draw(new Sprite(LevelMaster.crossone), 2, 19);
			draw(new Sprite(LevelMaster.invcrone), 3, 18);
			
			draw("- Crosses", 8, 22);
			if ( touchY(22) ) core.setScreen(new TrainingLevel4(false));
		}
		
		if ( buttonRight("[BLUE]Go Back[]", -6) || keyPressed == "BACK" ) menuScreen = 3;
	}
//-------------------------------------------------MenuScreen 5 -------	
	private void displayCase5() {
		
		textHeading("[ORANGE]Settings[]", 48);
		
		textLeft("[GOLD]Preview Mode[]", 30);
		if ( buttonCentre(previewMode? "Turn Off"
									 : "Turn On", 25) ) setPreviewMode(!previewMode);
		
		textLeft("[GOLD]Clear achievements[]", 20);
		if ( buttonCentre("Press here", 15) ) {
			confirmationText = "This will delete all your progress so far.";
			nextScreen = 5;
			menuScreen = 7;
		}
		if ( confirmed ) {
			prefs.clear();
			prefs.flush();
			confirmed = false;
		}
		
		if ( buttonRight("[ORANGE]Go Back[]", -6) || keyPressed == "BACK" ) menuScreen = 1;
	}	
//-------------------------------------------------MenuScreen 6 (pause)--	
	private void displayCase6() {
		
		textHeading("[GOLD]Game Paused", 45);
		
		// game is actually already saved, so this menu offers you a different choice than it appears to.
		textCentre("[]Save game, or exit without saving?", 34);
		
		font.setScale(0.3f);
		if ( buttonCentre("[GREEN]Save & Exit[]", 20) ) menuScreen = 1;
		
		if ( buttonCentre("[RED]Exit[]", 10) ) {
				menuScreen = nextScreen;
				if ( savedLevel.exists() ) savedLevel.delete();
		}
		font.setScale(0.25f);
		if ( buttonCentre("Back to Game", 0) || keyPressed == "BACK" ) runSavedLevel();
		font.setScale(0.2f);
	}
//--------------------------------------------------MenuScreen 7 (confirmation)
	private void displayCase7() {
		
		textHeading("[RED]Confirmation[]", 46);
		textHeading("[RED]Needed[]", 40);
		
		textCentre(confirmationText, 30);
		
		font.setScale(0.25f);
		textCentre("[GOLD]Are you Sure?[]", 10);
		font.setScale(0.3f);
		if ( buttonLeft("[GREEN]YES[]", 0) ) confirmed = true;
		if ( buttonRight("[RED]NO[]", 0) || confirmed ) menuScreen = nextScreen ;
	}
//--------------------------------------------------------------------
	private void draw(String text, int x, int y) {
		font.draw(batch, text, x, y);
	}
	private void draw(Sprite sprite, int x, int y) {
		sprite.setBounds(x, y, 3, 3);
		sprite.draw(batch);
	}
	protected boolean buttonLeft(String text, float y) {
		textLeft(text, y);
		y = y - font.getCapHeight();
		TextBounds bounds = font.getBounds(text);
		return ( buttonTouched(4, 4 + bounds.width, y, y + bounds.height) );
	}
	protected boolean buttonRight(String text, float y) {
		textRight(text, y);
		y = y - font.getCapHeight();
		TextBounds bounds = font.getBounds(text);
		return ( buttonTouched(38 - bounds.width, 38, y, y + bounds.height) );
	}
	protected boolean buttonCentre(String text, float y) {
		textCentre(text, y);
		y = y - font.getCapHeight();
		TextBounds bounds = font.getBounds(text);
		float xMin = (42 - bounds.width) / 2;
		return ( buttonTouched(xMin, 42 - xMin, y, y + bounds.height) );
	}
	protected boolean buttonTouched(float xMin, float xMax, float yMin, float yMax) {
		return ( touch.x > xMin && touch.x < xMax && touch.y > yMin && touch.y < yMax );
	}
	private boolean touchY(int min) {
		return touchY(min, min+4);
	}
	private boolean touchY(int min, int max) {
		return ( touch.y > min - 4 && touch.y < max - 4 );
	}
	private void setPreviewMode(boolean mode) {
		previewMode = mode;
		prefs.putBoolean("previewMode", mode);
		prefs.flush();
	}
	private void runSavedLevel() {
		Json json = new Json();
		JsonReader jsonReader = new JsonReader();
		JsonValue loaded = jsonReader.parse(savedLevel.readString());
		savedLevel.delete();
		
		try {
			Class<?> level = ClassReflection.forName(Core.PACKAGE + "levels." + loaded.getString("level"));
			LevelMaster levelmaster = (LevelMaster) ClassReflection.newInstance(level);
			levelmaster.read(json, loaded);
			
			core.setScreen(levelmaster);
			
		} catch (ReflectionException e) { e.printStackTrace(); }	
	}
	@Override
	public void hide() {
		dispose();
	}
	@Override
	public void show() {
	}
	@Override
	public void dispose() {
	}
}
