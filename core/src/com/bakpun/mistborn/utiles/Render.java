package com.bakpun.mistborn.utiles;

import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.bakpun.mistborn.MistBorn;

public class Render {
	public static SpriteBatch batch;	//Unico SpriteBatch para el programa.
	public static MistBorn app;		//Para utilizar por ahora el setScreen() para cambiar las pantallas.
	public static I18NBundle bundle = I18NBundle.createBundle(Gdx.files.internal("locale/locale"));	//Un bundle para todas los textos del juego.

	public static void limpiarPantalla(float r, float g, float b) {	
		Gdx.gl.glClearColor(r,g,b,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
}
