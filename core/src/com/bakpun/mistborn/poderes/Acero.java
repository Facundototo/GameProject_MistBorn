package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.elementos.Disparo;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Acero extends Poder implements Disparable{

	//Poder que empuja objetos metalicos o disparo de monedas.
	private Disparo disparo;
	
	public Acero(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		super(Recursos.MARCO_ACERO, Color.CYAN,TipoPoder.ACERO); 
		disparo = new Disparo(mundo,pj,cam,c);
	}

	@Override
	public void quemar() {
		if(disparo.getCantMonedas() > 0) {		//Si tiene monedas dispara sino no puede.
			disparo.disparar();
		}
	}
	
	@Override
	public Disparo getDisparo(){
		return this.disparo;
	}
	
	
}
