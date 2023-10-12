package com.bakpun.mistborn.poderes;

import java.util.EventListener;

import com.badlogic.gdx.graphics.Color;

public interface EventoCrearBarra extends EventListener{
	void crearBarra(String ruta,Color color);
}
