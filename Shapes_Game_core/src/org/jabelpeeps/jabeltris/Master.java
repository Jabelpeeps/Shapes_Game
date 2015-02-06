package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.RandomXS128;

public abstract class Master {
	
	// This Class is the root of the tree of level classes, is 
	// therefore a convenient place to store references
	// to the textures for all the shape types. 
	
	public static AtlasRegion line;
	public static AtlasRegion square;
	public static AtlasRegion triangle;
	public static AtlasRegion crossone;
	public static AtlasRegion crosstwo;
	
	protected static RandomXS128 rand = new RandomXS128();
	
	protected Shape setOriginAndBounds(Shape s, int x, int y) {
		s.setOrigin(2, 2);
		s.setBounds(x*4, y*4, 4, 4);
		
		return s;
	}

	public abstract Shape makeNewShape(int i, int j);
}
