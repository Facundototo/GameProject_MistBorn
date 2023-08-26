package com.bakpun.mistborn.utiles;

import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bakpun.mistborn.MistBorn;

public class Render {
	public static SpriteBatch batch;	//Unico SpriteBatch para el programa.
	public static MistBorn app;		//Para utilizar por ahora el setScreen() para cambiar las pantallas.

	public static void limpiarPantalla(float r, float g, float b) {	
		Gdx.gl.glClearColor(r,g,b,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
}
