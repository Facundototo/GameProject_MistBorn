package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.Listeners;

public abstract class Poder{

	public Poder(String ruta,Color color,TipoPoder tipo) {
		Listeners.crearBarraHUD(ruta, color,tipo);	//Llama al evento para crear la barra especifica del Hud.
	}
	
	public abstract void quemar();
	
}
