package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Hierro extends Poder{
	
	//Poder que tira de objetos metalicos.
	
	public Hierro(Personaje pj) {
		super(Recursos.MARCO_HIERRO, Color.GRAY,TipoPoder.HIERRO,pj);	
	}

	@Override
	public void quemar() {
		if(super.energia > 0f) {
			if(super.pj.isColMouseMetal()) {
				Listeners.reducirPoderPj(super.pj.getTipo(), super.tipo, 1f);
			}
		}
	}	
}
