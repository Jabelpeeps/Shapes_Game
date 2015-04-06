package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Blank extends BlankAbstract {
	
	private static Pool<Blank> pool = Pools.get(Blank.class);
	private boolean freeable;

	private Blank() {
	}
	public static Blank get(PlayArea game, boolean freeable) {
		return pool.obtain().init(game, freeable);
	}
	private Blank init(PlayArea g, boolean f) {
		type = "blank";
		game = g;
		freeable = f;
		return this;
	}
	@Override
	protected boolean matches(Shape other) {
		free();
		return false;
	}
	@Override
	protected boolean isBlank() {
		free();
		return true;
	}
	@Override
	public void free() {
		if ( freeable ) pool.free(this);
	}
}
