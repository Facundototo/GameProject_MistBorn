package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.OpcionAcero;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Acero extends Poder{

	//Poder que empuja objetos metalicos o disparo de monedas.
	private OpcionAcero opcion = OpcionAcero.DISPARO;
	
	public Acero(Personaje pj) {
		super(Recursos.MARCO_ACERO, Color.CYAN,TipoPoder.ACERO,pj); 
	}

	@Override
	public void quemar() {
		if(super.energia > 0f) {
			if(opcion == OpcionAcero.EMPUJE) {
				if(super.pj.isColMouseMetal()) {
					Listeners.reducirPoderPj(super.pj.getTipo(), super.tipo, 0.5f);
				}
			}
		}
	}
	
	public void cambiarOpcion() {
		this.opcion = (this.opcion == OpcionAcero.DISPARO)?OpcionAcero.EMPUJE:OpcionAcero.DISPARO;
	}
	
	public OpcionAcero getOpcion() {
		return this.opcion;
	}
}
