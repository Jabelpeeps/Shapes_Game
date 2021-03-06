package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Splash extends Core implements Screen {
	
// -------------------------------------------------Fields---------	
	private final Core core;
	private Sprite logo;
	private int loadingStage = 1;
	private boolean nowLoaded = false;
	private float logoScale = 0.1f;
	private String logoScaling = "up";
// ------------------------------------------------Constructor-----
	Splash(final Core c) {
		core = c;

        logo = new Sprite(new Texture("ScreenShot.jpg"));  
		logo.setOrigin(20, 20);
		logo.setBounds(0, 0, 40, 40);
		logo.rotate(180);
	}
// ------------------------------------------------Methods---------	
	
	@Override
	public void render(float delta) {
		
		if ( manager.update() && !nowLoaded ) {
			
			switch (loadingStage++) {
			case 1:			
				// unpack the resources from the asset manager
				atlas = manager.get("pack.atlas");
				boardBase = manager.get("board10x10.jpg");
				LevelMaster.core = core;
				break;
			case 2:	
				LevelMaster.line = atlas.findRegion("line");
				LevelMaster.square = atlas.findRegion("square");
				LevelMaster.triangle = atlas.findRegion("triangle");
				break;
			case 3:
				LevelMaster.crossone = atlas.findRegion("crossone");
				LevelMaster.crosstwo = atlas.findRegion("crosstwo");
				LevelMaster.invcrone = atlas.findRegion("invcrone");
				break;
			case 4:
				LevelMaster.invcrtwo = atlas.findRegion("invcrtwo");
				LevelMaster.invline = atlas.findRegion("invline");
				LevelMaster.invsqr = atlas.findRegion("invsqr");
				break;
			case 5:
				LevelMaster.invtri = atlas.findRegion("invtri");
				LevelMaster.greysqr = atlas.findRegion("greysqr");
				LevelMaster.greytri = atlas.findRegion("greytri");
				break;
			case 6:
				LevelMaster.greyline = atlas.findRegion("greyline");
				LevelMaster.greycrone = atlas.findRegion("greycrone");
				LevelMaster.greycrtwo = atlas.findRegion("greycrtwo");
				break;
			case 7:
				LevelMaster.horizgreyline = atlas.findRegion("horizgreyline");
				LevelMaster.fourbar = atlas.findRegion("fourbar");
				LevelMaster.mirrorell = atlas.findRegion("mirrorell");
				break;
			case 8:
				LevelMaster.realell = atlas.findRegion("realell");
				LevelMaster.realsquare = atlas.findRegion("realsquare");
				LevelMaster.realt = atlas.findRegion("realt");
				break;
			case 9:	
				LevelMaster.zigleft = atlas.findRegion("zigleft");
				LevelMaster.zigright = atlas.findRegion("zigright");
				LevelMaster.horizline = atlas.findRegion("horizline");
				LevelMaster.horizinvline = atlas.findRegion("horizinvline");
				break;
			case 10:
				boardBaseTiles = TextureRegion.split(boardBase, 50, 50);
				break;
			case 11:
				// Font Loader
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ComfortaaRegular.ttf"));
				FreeTypeFontParameter parameter = new FreeTypeFontParameter();
				parameter.size = 18;
				parameter.characters = MY_CHARS;
				parameter.packer = null;
				parameter.genMipMaps = true;
				parameter.kerning = true;
				parameter.minFilter = TextureFilter.MipMapLinearLinear;
				parameter.magFilter = TextureFilter.Linear;
				parameter.borderWidth = 0.5f;
				parameter.borderColor = Color.BLACK;
				font = generator.generateFont(parameter); 
				
				generator.dispose();                                   // dispose to avoid memory leaks!
				font.setScale(0.2f);
				font.setUseIntegerPositions(false);
				font.setMarkupEnabled(true);
				nowLoaded = true;
				break;
			}
		} 	
	    
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if ( nowLoaded && logoScale < 0f ) { 

			core.setScreen(new MainMenu(core, 0));
			
		} else {
			logo.setScale(logoScale);
			logo.rotate(20);
			
		    if ( logoScale >= 1f ) 	logoScaling = "down";
		    
		    logoScale = ( logoScaling == "up" ) ? logoScale + 0.1f : logoScale - 0.1f;
		    
			Core.delay(60);
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			
			batch.begin();
			batch.disableBlending();
			logo.draw(batch);
			batch.enableBlending();
			batch.end();
		 }
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
// ----------------------------------------------End-of-Class--------
}
