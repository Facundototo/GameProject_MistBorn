package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.eventos.Listeners;

public abstract class Poder {

	public Poder(String ruta,Color color) {
		Listeners.crearBarraHUD(ruta, color);	//Llama al evento para crear la barra especifica del Hud.
	}
}
