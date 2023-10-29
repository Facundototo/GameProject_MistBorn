package com.bakpun.mistborn.personajes;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.box2d.ColisionMouse;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.elementos.Animacion;
import com.bakpun.mistborn.elementos.Audio;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.eventos.EventoReducirVida;
import com.bakpun.mistborn.eventos.EventoRestarMonedas;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Disparable;
import com.bakpun.mistborn.poderes.Poder;

public abstract class Personaje implements EventoReducirVida,EventoRestarMonedas{
	
	private final float VELOCIDAD_X = 15f, IMPULSO_Y = 20f;
	
	private float vida = 100f;
	
	private Animacion animacionQuieto,animacionCorrer;
	private Imagen spr;
	private Entradas entradas;
	private Body pj;
	private Vector2 movimiento;
	private Fisica f;
	private Colision c;
	private ColisionMouse cm;
	private TextureRegion saltos[] = new TextureRegion[3];
	private String animacionSaltos[] = new String[3];
	private String animacionEstados[] = new String[2];
	protected Poder poderes[];
	private int monedas;
	private TipoPersonaje tipo;
	
	private boolean saltar,puedeMoverse,estaCorriendo,estaQuieto,apuntando,disparando,primerSalto,segundoSalto,caidaSalto,ladoDerecho,correrDerecha,correrIzquierda;
	private boolean reproducirSonidoCorrer;
	private float duracionQuieto = 0.2f,duracionCorrer = 0.15f,delta = 0f;
	private int seleccion = 0;
	
	public Personaje(String rutaPj,String[] animacionSaltos,String[] animacionEstados,World mundo,Entradas entradas,Colision c,OrthographicCamera cam,boolean ladoDerecho,TipoPersonaje tipo) {
		this.animacionSaltos = animacionSaltos;
		this.animacionEstados = animacionEstados;
		this.ladoDerecho = ladoDerecho;
		this.c = c;
		this.entradas = entradas;
		this.poderes = new Poder[(tipo == TipoPersonaje.NACIDO_BRUMA)?3:1];
		this.tipo = tipo;
		this.monedas = 10;	//Monedas iniciales 10.
		movimiento = new Vector2();
		f = new Fisica();
		cm = new ColisionMouse(mundo,cam);
		Audio.setSonidoPjCorriendo();
 		spr = new Imagen(rutaPj);
 		spr.setEscalaBox2D(12);
		crearAnimaciones();
		crearBody(mundo);
		crearPoderes(mundo,cam,c);
		Listeners.agregarListener(this);
	}
	
	protected abstract void crearPoderes(World mundo,OrthographicCamera cam,Colision c);
	
	private void crearBody(World mundo) {
		f.setBody(BodyType.DynamicBody,new Vector2((!ladoDerecho)?10:20,5));
		f.createPolygon(spr.getTexture().getWidth()/Box2dConfig.PPM, spr.getTexture().getHeight()/Box2dConfig.PPM);		//Uso la clase Fisica.
		f.setFixture(f.getPolygon(), 60, 0, 0);
		pj = mundo.createBody(f.getBody());
		pj.createFixture(f.getFixture());
		pj.setUserData(UserData.PJ);	//ID para la colision.
		pj.setFixedRotation(true);		//Para que el body no rote por culpa de las fuerzas.
		f.getPolygon().dispose();
	}

	private void updateAnimacion() {		//Este metodo updatea que frame de la animacion se va a mostrar actualmente,lo llamo en draw().
		delta = Gdx.graphics.getDeltaTime();
		
		animacionQuieto.update(delta);
		animacionCorrer.update(delta);
	}
	
	public void draw() {
		/* Cosas que hay que hacer como principal para el disparo:

		 * Hacer el sonido de la moneda cuando se dispara.
		 */
		
		updateAnimacion();
		
		calcularAcciones();	//Activa o desactiva las acciones del pj en base al input.
		calcularSalto();	//Calcula el salto con la gravedad.
		calcularMovimiento();	//Calcula el movimiento.
		
		
		pj.setLinearVelocity(movimiento);	//Aplico al pj velocidad lineal, tanto para correr como para saltar.
		
		spr.setPosicion(pj.getPosition().x, pj.getPosition().y);	//Le digo al Sprite que se ponga en la posicion del body.
		
		animar();
		reproducirSFX();
		
		quemarPoder();	//Seleccion de poderes.

	}
	private void quemarPoder() {
		if(tipo == TipoPersonaje.NACIDO_BRUMA){		//Si es nacido de la bruma, puede seleccionar los poderes.
			if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {seleccion = 0;}
			else if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {seleccion = 1;}
			else if(Gdx.input.isKeyJustPressed(Keys.R)) {poderes[2].quemar();}
		}
		else if(tipo == TipoPersonaje.VIOLENTO && Gdx.input.isKeyJustPressed(Keys.R)){poderes[seleccion].quemar();}		
		
		// Este if le sirve tanto al nacido de la bruma como a atraedor y lanzamonedas.
		if(disparando && tipo != TipoPersonaje.VIOLENTO) {
			poderes[seleccion].quemar();
		} 
		
		// Chequea todo el tiempo calcularFuerzas() porque lo que pasa es que todo lo de Disparo no se puede chequear en Acero.
		if(poderes[seleccion].getTipoPoder() == TipoPoder.ACERO) {
			poderes[seleccion].getDisparo().calcularFuerzas(disparando);	
		}
		
		if((poderes[seleccion].getTipoPoder() == TipoPoder.HIERRO)) {
			cm.dibujar(new Vector2(pj.getPosition().x,pj.getPosition().y), new Vector2(entradas.getMouseX()/Box2dConfig.PPM,entradas.getMouseY()/Box2dConfig.PPM));
		}
		
	}

	private void calcularAcciones() {
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
		apuntando =	(entradas.isBotonDer()); 	//este booleano se utiliza en el metodo drawLineaDisparo().
		disparando = (entradas.isBotonIzq() && apuntando);	
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
		return this.pj.getPosition().x;
	}
	public float getY() {
		return this.pj.getPosition().y;
	}
	public Body getBody() {
		return this.pj;
	}
	
	public Entradas getInput() {
		return this.entradas;
	}
	public void aplicarFuerza(Vector2 movimiento) {
		pj.setLinearVelocity(movimiento);
	}
	
	@Override
	public void reducirVida(float dano) {
		this.vida -= dano;
	}
	@Override
	public void restarMonedas() {
		this.monedas--;
	}
	
	public int getCantMonedas() {
		return this.monedas;
	}
	public ColisionMouse getColisionMouse() {
		return this.cm;
	}
	
}
