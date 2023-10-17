package com.bakpun.mistborn.personajes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Acero;
import com.bakpun.mistborn.poderes.Hierro;
import com.bakpun.mistborn.poderes.Peltre;
import com.bakpun.mistborn.poderes.Poder;
import com.bakpun.mistborn.utiles.Recursos;

public final class Vin extends Personaje{

	public Vin(World mundo, Entradas entradas,Colision c,OrthographicCamera cam,boolean ladoDerecho) {
		//Le paso al constructor de Personaje, los saltos, y los estados (quieto,corriendo).
		super(Recursos.PERSONAJE_VIN,Recursos.SALTOS_VIN,Recursos.ANIMACIONES_ESTADOS_VIN, mundo, entradas,c,cam,ladoDerecho);
		Poder hierro = new Hierro();	
		Poder acero = new Acero();		//Probando el HUD.
		Poder peltre = new Peltre();
	}

}
