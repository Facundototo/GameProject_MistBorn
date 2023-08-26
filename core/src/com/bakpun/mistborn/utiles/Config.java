package com.bakpun.mistborn.utiles;

import com.badlogic.gdx.Gdx;

public class Config {
	public static final int ALTO = Gdx.graphics.getHeight();
	public static final int ANCHO = Gdx.graphics.getWidth();
	public static float volumen = 1f;
	
	public static boolean isFullScreen() {		//Para chequear en PantallaMenu().
;		return Gdx.graphics.isFullscreen();
	}
	public static boolean isWindowed() {
		return (isFullScreen())?false:true;
	}
}
