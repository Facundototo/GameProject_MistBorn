package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.bakpun.mistborn.utiles.Recursos;

public class Audio {
	//Clase para organizar todos los sonidos del videojuego, no se usa como instancia(me genera ciertos problemas).
	//Esta todo static, me da bronca eso.
	
	public static Sound sonidoMenu;
	public static Music cancionMenu;
	public static float volumen = 1f;
	
	public static void setVolumen(float num) {
		volumen += num;
		cancionMenu.setVolume(volumen);
		sonidoMenu.setVolume(sonidoMenu.play(),volumen);
	}
	
	public static void setCancionMenu() {
		cancionMenu = Gdx.audio.newMusic(Gdx.files.internal(Recursos.CANCION_MENU));
	}
	public static void setSonidoMenu() {
		sonidoMenu = Gdx.audio.newSound(Gdx.files.internal(Recursos.SONIDO_OPCION_MENU));
	}
	public static float getVolumen() {
		return volumen;
	}
	
	
}
