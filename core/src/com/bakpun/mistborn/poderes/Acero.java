package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.elementos.Disparo;
import com.bakpun.mistborn.enums.OpcionAcero;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Recursos;

public class Acero extends Poder implements Disparable{

	//Poder que empuja objetos metalicos o disparo de monedas.
	private OpcionAcero opcion = OpcionAcero.DISPARO;
	
	public Acero(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		super(Recursos.MARCO_ACERO, Color.CYAN,TipoPoder.ACERO,pj); 
		crearDisparo(mundo,pj,cam,c);
	}

	@Override
	public void quemar() {
		if(super.energia > 0f) {
			if(this.opcion == OpcionAcero.DISPARO) {
				if(super.disparo.getCantMonedas() > 0) {		//Si tiene monedas dispara sino no puede.
					super.energia -= 0.5f;
					super.disparo.disparar(super.energia);
				}
			}else {
				if(super.pj.getColisionMouse().isColision()) {	//Si existe colision con un body en el rayo, se aplica la fuerza.
					super.disparo.actualizarDireccion(super.pj.getColisionMouse().getPuntoColision().x,super.pj.getColisionMouse().getPuntoColision().y);
					super.pj.aplicarFuerza(super.disparo.getFuerzaContraria());
					super.energia -= 0.5f;
					Listeners.reducirPoderPj(super.pj.getTipo(), super.tipo, 0.5f);
				}
			}
		}
	}

	@Override
	public void crearDisparo(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		super.disparo = new Disparo(mundo,pj,cam,c);
	}
	
	public void cambiarOpcion() {
		this.opcion = (this.opcion == OpcionAcero.DISPARO)?OpcionAcero.EMPUJE:OpcionAcero.DISPARO;
	}
	
	public OpcionAcero getOpcion() {
		return this.opcion;
	}
}
