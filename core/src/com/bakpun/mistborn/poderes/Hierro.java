package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.elementos.Disparo;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Hierro extends Poder implements Disparable{
	
	//Poder que tira de objetos metalicos.
	
	public Hierro(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		super(Recursos.MARCO_HIERRO, Color.GRAY,TipoPoder.HIERRO,pj);	
		crearDisparo(mundo,pj,cam,c);
	}

	@Override
	public void quemar() {
		if(super.energia > 0f) {
			//if(super.pj.getColisionMouse().isColision()) {
				//super.disparo.actualizarDireccion(super.pj.getColisionMouse().getPuntoColision().x,super.pj.getColisionMouse().getPuntoColision().y);
				//super.pj.aplicarFuerza(super.disparo.getMovimientoBala());
				Listeners.reducirPoderPj(super.pj.getTipo(), super.tipo, 1f);
			//}
		}
	}

	@Override
	public void crearDisparo(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		super.disparo = new Disparo(mundo,pj,cam,c);
	}
	
}
