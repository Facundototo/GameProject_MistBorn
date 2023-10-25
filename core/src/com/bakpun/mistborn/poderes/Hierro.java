package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.utiles.Recursos;

public class Hierro extends Poder{
	
	//Poder que tira de objetos metalicos.

	public Hierro() {
		super(Recursos.MARCO_HIERRO, Color.GRAY);	
	}

	@Override
	public void quemar() {
		System.out.println("quemando hierro");
		
	}
	
}
