package com.bakpun.mistborn.personajes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Acero;
import com.bakpun.mistborn.utiles.Recursos;

public final class Dockson extends Personaje{

	public Dockson(World mundo, Entradas entradas,Colision c,OrthographicCamera cam,boolean ladoDerecho,boolean oponente) {
		super(Recursos.PERSONAJE_HAM,Recursos.SALTOS_HAM,Recursos.ANIMACIONES_ESTADOS_HAM, mundo, entradas, c, cam, ladoDerecho,oponente,TipoPersonaje.LANZAMONEDAS);
	}

	@Override
	protected void crearPoderes(World mundo,OrthographicCamera cam,Colision c) {
		super.poderes[0] = new Acero(mundo,this,cam,c);
	}

	
	
}
