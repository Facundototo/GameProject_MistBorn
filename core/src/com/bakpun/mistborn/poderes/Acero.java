package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.elementos.Disparo;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Acero extends Poder{

	//Poder que empuja objetos metalicos o disparo de monedas.
	private Disparo disparo;
	private boolean balaDisparada = false;
	
	public Acero(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		super(Recursos.MARCO_ACERO, Color.CYAN); 	//Falta disenar las barras para ponerselo como parametro.
		disparo = new Disparo(mundo,pj,cam,c);
	}

	@Override
	public void quemar() {
		if(!balaDisparada) {
			balaDisparada = true;
			disparo.disparar();
		}
		disparo.borrarMonedas();
			
		if(balaDisparada) {
			balaDisparada = disparo.calcularFuerzas();		//Me quede aca con un problema de quemar() y calcularFuerzas().
		}
		
	}
	
	
}
