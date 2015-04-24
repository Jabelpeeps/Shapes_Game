package org.jabelpeeps.jabeltris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public final class MyColors {

	private static final Color lighten = new Color(0x404040FF);
	
	static void setupColors() {
		Colors.reset();
		Colors.put( "RED" , new Color( 0xDC143CFF ) );
		Colors.put( "DARK_RED" , new Color( 0xB22222FF ) );
		Colors.put( "LIGHT_RED" , Colors.get( "RED" ).cpy().add( 0.35f, 0.35f, 0.35f, 1f ) );
		
		Colors.put( "GREEN" , new Color( 0x32CD32FF ) );
		Colors.put( "DARK_GREEN" , new Color( 0x228B22FF ) );
		Colors.put( "LIGHT_GREEN" , Colors.get( "GREEN" ).cpy().add( lighten ).add( lighten ) );
		
		Colors.put( "YELLOW" , new Color( 0xFFD700FF ) );
		Colors.put( "DARK_YELLOW" , new Color( 0xBDB76BFF ) );
		Colors.put( "LIGHT_YELLOW" , Colors.get( "YELLOW" ).cpy().add( lighten ).add( lighten ).add( lighten ) );
		
		Colors.put( "BLUE" , new Color( 0x0080FFFF ) );
		Colors.put( "DARK_BLUE" , new Color( 0x004080FF ) );
		Colors.put( "LIGHT_BLUE" , Colors.get( "BLUE" ).cpy().add( lighten ) );
		
		Colors.put( "ORANGE" , new Color( 0xFF8C00FF ) );
		Colors.put( "DARK_ORANGE" , new Color( 0xB8860BFF ) );
		Colors.put( "LIGHT_ORANGE" , Colors.get( "ORANGE" ).cpy().add( lighten ) );
		
		Colors.put( "MAGENTA" , new Color( 0x9932CCFF ) );
		Colors.put( "DARK_MAGENTA" , new Color( 0X800080FF ) );
		Colors.put( "LIGHT_MAGENTA" , Colors.get( "MAGENTA" ).cpy().add( lighten ) );
		
		Colors.put( "GOLD" , new Color( 0xFFD700FF ) );
	}
}
