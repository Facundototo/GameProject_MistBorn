package com.bakpun.mistborn.utiles;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bakpun.mistborn.MistBorn;

public class Render {
	public static SpriteBatch batch;	//Unico SpriteBatch para el programa.
	public static MistBorn app;		//Para utilizar por ahora el setScreen() para cambiar las pantallas.
	public static Music cancionMenu = Gdx.audio.newMusic(Gdx.files.internal(Recursos.CANCION_MENU)); /* Para utilizar esta cancion en la pantalla de carga y la
																									y la de menu. La puse en Render porque es static*/
	public static float volumen = 1;
	
	public static void subirVolumen(float num) {
		volumen += num;
		cancionMenu.setVolume(volumen);					//Para modificar el volumen, tendria que hacer una clase Audio para manipular esto.
	}
	public static void bajarVolumen(float num) {
		volumen -= num;
		cancionMenu.setVolume(volumen);
	}
	
	public static void limpiarPantalla(float r, float g, float b) {	
		Gdx.gl.glClearColor(r,g,b,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
}
