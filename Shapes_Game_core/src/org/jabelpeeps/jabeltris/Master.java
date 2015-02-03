package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.RandomXS128;

abstract class Master {
	
	// This Class is the root of the tree of level classes, is 
	// therefore a convenient place to store references
	// to the textures for all the shape types. 
	
	static AtlasRegion line;
	static AtlasRegion square;
	static AtlasRegion triangle;
	static AtlasRegion crossone;
	static AtlasRegion crosstwo;
	
	protected static RandomXS128 rand = new RandomXS128();
	
	protected Shape setOriginScaleAndBounds(Shape s, int x, int y) {
		s.setOrigin(1.5f, 1.5f);
		s.setScale(0.9f);
		s.setBounds(x*3, y*3, 3f, 3f);
		return s;
	}

	abstract Shape makeNewShape(int i, int j);
}
