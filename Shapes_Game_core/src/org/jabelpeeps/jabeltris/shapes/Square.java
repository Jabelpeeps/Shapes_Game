package org.jabelpeeps.jabeltris.shapes;

import org.jabelpeeps.jabeltris.Master;
import org.jabelpeeps.jabeltris.Shape;

public class Square extends Shape {
	
	public Square() {
		this.setRegion(Master.square);
		type = "square";
	}			

	@Override
	public void select() {
		this.setColor(0f, 1f, 1f, 1f);
	}
	@Override
	public void deselect() {
		this.setColor(1f, 1f, 1f, 1f);
	}
	@Override
	public boolean checkMatch() {
		boolean matchmade = false;
		int x = (int) getX();
		int y = (int) getY();
		
		if ( m3(x-1, y, x, y-1, x-1, y-1, this) ) matchmade = true;
		if ( m3(x+1, y, x, y+1, x+1, y+1, this) ) matchmade = true;
		if ( m3(x-1, y, x, y+1, x-1, y+1, this) ) matchmade = true;
		if ( m3(x+1, y, x, y-1, x+1, y-1, this) ) matchmade = true;
		
		return matchmade; 
	}	

	@Override		
	public void findHint() {
		// method to trigger shapes to search for possible matches if they 
		// were to be swapped in each of the four cardinal directions.
		
	}
}
