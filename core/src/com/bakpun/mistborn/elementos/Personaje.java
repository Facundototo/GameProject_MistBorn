package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.Gdx;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Recursos;

public class Personaje {
	private final float VELOCIDAD_X = 400f, IMPULSO_Y = 12f,GRAVEDAD = -20f;
	private Animacion animacionQuieto,animacionCorrer;
	private Imagen spr;
	private float delta = 0f,x=100,y=200,velocidadImpulso = 0f;
	private boolean saltar,puedeMoverse,estaSaltando = false,estaCorriendo,estaQuieto;
	private Entradas entradas;
	private float duracionQuieto = 0.2f,duracionCorrer = 0.15f;
	
	public Personaje(String rutaPj) {
		entradas = new Entradas();
		Gdx.input.setInputProcessor(entradas);
 		spr = new Imagen(rutaPj);
 		spr.ajustarTamano(2);
		crearAnimaciones();
	}
	
	private void update() {		//Este metodo updatea que frame de la animacion se va a mostrar actualmente,lo llamo en draw().
		delta = Gdx.graphics.getDeltaTime();
		
		animacionQuieto.update(delta);
		animacionCorrer.update(delta);
	}
	
	
	public void draw() {
		
		update();
		
		saltar = (entradas.isEspacio() && !estaSaltando);
		puedeMoverse = (entradas.isIrDer() != entradas.isIrIzq());	//Si el jugador toca las 2 teclas a la vez no va a poder moverse.
		estaQuieto = ((!entradas.isIrDer() == !entradas.isIrIzq()) || !puedeMoverse);
		estaCorriendo = ((entradas.isIrDer() || entradas.isIrIzq()) && puedeMoverse);
		
		
		calcularSalto();	//Calcula el salto con la gravedad.
		calcularMovimiento();	//Calcula el movimiento. Tendria que cambiarlo por la clase Entradas (creo).
		calcularLimites();	//Calcula e impide que el jugador no se salga del plano visible.
		
		spr.setPosicion(x,y);
		
		animar();
	}
	
	private void animar() {
		if(estaQuieto) {	//Si esta quieto muestra el fotograma actual de la animacionQuieto. 
			spr.draw(animacionQuieto.getCurrentFrame());
		}else if(estaCorriendo) { 	//Si esta corriendo muestra el fotograma actual de la animacionCorrer.
			if(entradas.isIrDer()) {
				spr.draw(animacionCorrer.getCurrentFrame(),false);
			}else {
				spr.draw(animacionCorrer.getCurrentFrame(),true);
			}
		}	
	}

	public void dispose() {
		spr.getTexture().dispose();
	}
	
	private void crearAnimaciones() {
		animacionQuieto = new Animacion();
		animacionQuieto.create(Recursos.ANIMACION_QUIETO, 4,1, duracionQuieto);
		animacionCorrer = new Animacion();
		animacionCorrer.create(Recursos.ANIMACION_CORRER, 4,1, duracionCorrer);	
	}
	
	private void calcularLimites() {
		if(x >= Gdx.graphics.getWidth()) {
			x = Gdx.graphics.getWidth();
		}
		if(x <= 0){
			x = 0;
		}
	}
	private void calcularSalto() {
		if(saltar) {
			velocidadImpulso += IMPULSO_Y;
			y += velocidadImpulso * delta;
			estaSaltando = true;
		}	
		if(estaSaltando) {
			velocidadImpulso += GRAVEDAD * delta;
			y += velocidadImpulso;
			if(y <= 200) {
				y = 200;
				estaSaltando = false;
				velocidadImpulso = 0;
			}
		}
	}
	private void calcularMovimiento() {
		if(puedeMoverse) {
			if(entradas.isIrDer()) {
				x += VELOCIDAD_X * delta;
			} else if (entradas.isIrIzq()){
				x -= VELOCIDAD_X * delta;
			}
		}
	}
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
}
