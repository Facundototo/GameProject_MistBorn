package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.EventoGestionPoderes;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;

public abstract class Poder implements EventoGestionPoderes{
	
	protected TipoPoder tipo;
	protected Personaje pj;
	protected float energia = 100f;
	
	public Poder(String ruta,Color color,TipoPoder tipo,Personaje pj) {
		Listeners.agregarListener(this);
		Listeners.crearBarraHUD(ruta, color,tipo);	//Llama al evento para crear la barra especifica del Hud.
		this.pj = pj;
		this.tipo = tipo;
	}
	
	public abstract void quemar();
	
	public TipoPoder getTipoPoder() {
		return this.tipo;
	}
	
	public float getEnergia() {
		return this.energia;
	}
	
	@Override
	public void reducirPoder(TipoPersonaje tipoPj,TipoPoder tipoPoder,float energia) {
		if(tipo == tipoPoder) {
			this.energia -= energia;
		}	
	}
	@Override
	public void aumentarPoder(TipoPersonaje tipoPj,TipoPoder tipoPoder, float energia) {
		if(tipo == tipoPoder) {
			this.energia += energia;
		}
	}
	
	
}
