package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.elementos.Disparo;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;

public abstract class Poder{
	
	protected Personaje pj;
	private TipoPoder tipo;
	protected Disparo disparo;
	
	public Poder(String ruta,Color color,TipoPoder tipo,Personaje pj) {
		Listeners.crearBarraHUD(ruta, color,tipo);	//Llama al evento para crear la barra especifica del Hud.
		this.pj = pj;
		this.tipo = tipo;
	}
	
	public abstract void quemar();
	
	public TipoPoder getTipoPoder() {
		return this.tipo;
	}
	
	public Disparo getDisparo(){
		return this.disparo;
	}
	
}
