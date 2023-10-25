package com.bakpun.mistborn.personajes;

import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Acero;
import com.bakpun.mistborn.poderes.Hierro;
import com.bakpun.mistborn.poderes.Peltre;
import com.bakpun.mistborn.utiles.Recursos;

public final class Vin extends Personaje{
	
	public Vin(World mundo, Entradas entradas,Colision c,OrthographicCamera cam,boolean ladoDerecho) {
		super(Recursos.PERSONAJE_VIN,Recursos.SALTOS_VIN,Recursos.ANIMACIONES_ESTADOS_VIN, mundo, entradas,c,cam,ladoDerecho,TipoPersonaje.NACIDO_BRUMA);
	}

	@Override
	protected void crearPoderes(World mundo,OrthographicCamera cam,Colision c) {
		super.poderes[1] = new Acero(mundo,this,cam,c);		
		super.poderes[0] = new Hierro();	
		super.poderes[2] = new Peltre();
	}

}
