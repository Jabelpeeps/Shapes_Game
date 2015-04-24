package org.jabelpeeps.jabeltris;

import java.util.HashSet;
import java.util.Set;

import org.jabelpeeps.jabeltris.Core.HintVisitor;

import com.badlogic.gdx.input.GestureDetector.GestureListener;

public abstract class GameMechanic {
	
	protected Set<Shape> hintList = new HashSet<Shape>(32);
	protected HintVisitor visitor;
	protected GestureListener input;
	protected boolean active = false;
	
	protected abstract int searchForMoves();
	protected abstract void takeMove();
	
	protected void activate() {
		if ( visitor != null ) Shape.addHintVisitor( visitor );
		if ( input != null ) GestureMultiplexer.getInstance().addListener( input );
		active = true;
	}
	protected void deactivate() {
		active = false;
		if ( visitor != null ) Shape.removeHintVisitor( visitor );
		if ( input != null ) GestureMultiplexer.getInstance().removeListener( input );
	}
	protected void toggleActive() {
		if ( active )
			deactivate();
		else
			activate();
	}
	protected Set<Shape> getHintList() {
		return hintList;
	}
	protected void clearHints() {
		hintList.clear();
	}	
}
