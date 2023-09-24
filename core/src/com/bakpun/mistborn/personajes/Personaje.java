package com.bakpun.mistborn.personajes;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.elementos.Animacion;
import com.bakpun.mistborn.elementos.Audio;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.io.Entradas;

public abstract class Personaje {
	
	private final float VELOCIDAD_X = 15f, IMPULSO_Y = 20f;
	
	private Animacion animacionQuieto,animacionCorrer;
	private Imagen spr;
	private Entradas entradas;
	private Body pj;
	private Vector2 movimiento;
	private Fisica f;
	private Colision c;
	private TextureRegion saltos[] = new TextureRegion[3];
	private String animacionSaltos[] = new String[3];
	private String animacionEstados[] = new String[2];
	
	private boolean saltar,puedeMoverse,estaCorriendo,estaQuieto,primerSalto,segundoSalto,caidaSalto,ladoDerecho,correrDerecha,correrIzquierda;
	private boolean reproducirSonidoCorrer;
	private float duracionQuieto = 0.2f,duracionCorrer = 0.15f,delta = 0f;
	
	
	public Personaje(String rutaPj,String[] animacionSaltos,String[] animacionEstados,World mundo,Entradas entradas,Colision c,boolean ladoDerecho) {
		this.animacionSaltos = animacionSaltos;
		this.animacionEstados = animacionEstados;
		this.ladoDerecho = ladoDerecho;
		this.c = c;
		this.entradas = entradas;
		movimiento = new Vector2();
		f = new Fisica();
		Audio.setSonidoPjCorriendo();
 		spr = new Imagen(rutaPj);
 		spr.setEscalaBox2D(12);
		crearAnimaciones();
		crearBody(mundo);
	}
	
	private void crearBody(World mundo) {
		f.setBody(BodyType.DynamicBody,new Vector2((!ladoDerecho)?10:20,5));
		f.createPolygon(spr.getTexture().getWidth()/Box2dConfig.PPM, spr.getTexture().getHeight()/Box2dConfig.PPM);		//Uso la clase Fisica.
		f.setFixture(f.getPolygon(), 60, 0, 0);
		pj = mundo.createBody(f.getBody());
		pj.createFixture(f.getFixture());
		pj.setUserData(UserData.PJ);	//ID para la colision.
		pj.setFixedRotation(true);		//Para que el body no rote por culpa de las fuerzas.
	}

	private void update() {		//Este metodo updatea que frame de la animacion se va a mostrar actualmente,lo llamo en draw().
		delta = Gdx.graphics.getDeltaTime();
		
		animacionQuieto.update(delta);
		animacionCorrer.update(delta);
	}
	
	public void draw() {
		
		update();
		
		//RECORDATORIO: Esto de ladoDerecho y de cambiarle las teclas es para probar las colisiones sin utilizar redes.
		
		correrDerecha = ((!ladoDerecho)?entradas.isIrDerD():entradas.isIrDerRight());
		correrIzquierda = ((!ladoDerecho)?entradas.isIrIzqA():entradas.isIrIzqLeft());
		saltar = (((!ladoDerecho)?Gdx.input.isKeyJustPressed(Keys.SPACE):Gdx.input.isKeyJustPressed(Keys.UP)) && this.c.isPuedeSaltar(pj));
		puedeMoverse = (correrDerecha != correrIzquierda);	//Si el jugador toca las 2 teclas a la vez no va a poder moverse.
		estaQuieto = ((!correrDerecha == !correrIzquierda) || !puedeMoverse && this.c.isPuedeSaltar(pj));
		estaCorriendo = ((correrDerecha || correrIzquierda) && puedeMoverse && this.c.isPuedeSaltar(pj));
		primerSalto = (movimiento.y > IMPULSO_Y - 8 && movimiento.y <= IMPULSO_Y);
		segundoSalto = (movimiento.y > 0 && movimiento.y <= IMPULSO_Y - 8);
		caidaSalto = (movimiento.y < 0);
		
		calcularSalto();	//Calcula el salto con la gravedad.
		calcularMovimiento();	//Calcula el movimiento.
		
		pj.setLinearVelocity(movimiento);	//Aplico al pj velocidad lineal, tanto para correr como para saltar.
		
		spr.setPosicion(pj.getPosition().x, pj.getPosition().y);	//Le digo al Sprite que se ponga en la posicion del body.
		
		animar();
		reproducirSFX();
	}
	
	//Metodo que administra los sonidos de los pj, salto,golpe,disparo,etc.
	private void reproducirSFX() {
		if(estaCorriendo) {
			if(!reproducirSonidoCorrer) {
				Audio.pjCorriendo.play(0.2f);
				reproducirSonidoCorrer = true;
			}
		}else {
			if(reproducirSonidoCorrer) {
				Audio.pjCorriendo.stop();
				reproducirSonidoCorrer = false;
			}
		}
	}

	private void animar() {
		
		if(estaQuieto) {
			if(!c.isPuedeSaltar(pj)) {		//si estaQuieto pero salta, hace la animacion de salto.
				spr.draw(saltos[0]);
			}else {
				spr.draw(animacionQuieto.getCurrentFrame());
			}
		}else if(estaCorriendo) { 	//Si esta corriendo muestra el fotograma actual de la animacionCorrer.
			spr.draw(animacionCorrer.getCurrentFrame());
		}else if(primerSalto) {		
			spr.draw(saltos[0]);		
		}else if(segundoSalto) {		//saltos[] contiene las diferentes texturas, se van cambiando en base a la altura, o a la caida,
			spr.draw(saltos[1]);		//por eso no lo hice con la clase Animacion, porque no es constante esto.
		}else if(caidaSalto) {
			spr.draw(saltos[2]);
		}
		if(!estaQuieto) {			//cuando !estaQuieto va a poder flipearse el pj, porque sino se queda mirando para un lado que no es.
			spr.flip((correrDerecha)?false:true);
		}
	}

	public void dispose() {
		spr.getTexture().dispose();
	}
	
	private void crearAnimaciones() {
		animacionQuieto = new Animacion();
		animacionQuieto.create(animacionEstados[0], 4,1, duracionQuieto);
		animacionCorrer = new Animacion();
		animacionCorrer.create(animacionEstados[1], 4,1, duracionCorrer);	
		for (int i = 0; i < saltos.length; i++) {
			saltos[i] = new TextureRegion(new Texture((i==0)?animacionSaltos[i]:(i==1)?animacionSaltos[i]:animacionSaltos[i]));
		}
		
	}
	private void calcularSalto() {
		if(saltar) {
			movimiento.y = IMPULSO_Y;
		}else {
			movimiento.y = pj.getLinearVelocity().y;	//Esto hace que actue junto a la gravedad del mundo.
		}
	}
	private void calcularMovimiento() {
		if(puedeMoverse) {
			if(correrDerecha) {
				movimiento.x = VELOCIDAD_X;
			} else if (correrIzquierda){
				movimiento.x = -VELOCIDAD_X;
			}
		}
		if(estaQuieto) {	//Esto para que no se quede deslizando.
			movimiento.x = 0;
		}
	}
	public float getX() {
		return this.movimiento.x;
	}
	public float getY() {
		return this.movimiento.y;
	}
}
