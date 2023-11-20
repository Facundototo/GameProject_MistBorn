package com.bakpun.mistborn.poderes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Peltre extends Poder{
	//Poder que aumenta la velocidad, el salto y la resistencia a los golpes y disparos.
	private Random r = new Random();
	private float duracion = 0f;
	private boolean poderActivo = false;
	
	public Peltre(Personaje pj) {		
		super(Recursos.MARCO_PELTRE, Color.PURPLE,TipoPoder.PELTRE,pj);
	}

	@Override
	public void quemar() {
		
		if(!poderActivo) {
			if(super.energia >= 20f) {
				this.duracion = r.nextInt(3)+5;
				this.poderActivo = true;
				Listeners.activarPeltre(true);
				Listeners.reducirPoderPj(super.pj.getTipo(), super.tipo, 20f);
			}
		}else {
			this.duracion -= Gdx.graphics.getDeltaTime();
			Listeners.setDuracion((int)duracion);
			if((int)this.duracion == 0) {
				Listeners.activarPeltre(false);
				this.poderActivo = false;
				this.duracion = 0;
			}
		}	
	}
	
	public boolean isPoderActivo() {
		return this.poderActivo;
	}
	
}
