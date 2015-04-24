package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.PlayArea;
import org.jabelpeeps.jabeltris.Shape;

public class BlankMobile extends BlankAbstract {

	public BlankMobile() {
		type = "BlankMobile";
	}
	public BlankMobile(PlayArea g) {
		game = g;
	}
	@Override
	protected Shape select(boolean andBaseTile) {
		if ( andBaseTile ) selectTile();
		return this;	
	}
	@Override
	protected Shape selectTile() {
		game.getBoardTile( this ).setAlpha( +0.3f );
		return this;
	}
	@Override
	protected Shape deselect(boolean andBaseTile) {
		if ( andBaseTile ) deselectTile();
		return this;
	}
	@Override
	protected Shape deselectTile() {
		game.getBoardTile( this ).setAlpha( 0f );
		return this;
	}
	@Override
	protected boolean isMobile() {
		return true;
	}
}
