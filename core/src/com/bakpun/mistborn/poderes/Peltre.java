package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Peltre extends Poder{
	//Poder que aumenta la velocidad, el salto y la resistencia a los golpes y disparos.
	
	public Peltre(Personaje pj) {		
		super(Recursos.MARCO_PELTRE, Color.PURPLE,TipoPoder.PELTRE,pj);
	}

	@Override
	public void quemar() {
		
	}
}
