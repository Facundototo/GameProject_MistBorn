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
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.OpcionAcero;
import com.bakpun.mistborn.enums.TipoAudio;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.eventos.EventoReducirVida;
import com.bakpun.mistborn.eventos.EventoGestionMonedas;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Acero;
import com.bakpun.mistborn.poderes.Peltre;
import com.bakpun.mistborn.poderes.Poder;
import com.bakpun.mistborn.utiles.Render;

public abstract class Personaje implements EventoReducirVida,EventoGestionMonedas{
	
	private float velocidadX = 15f, impulsoY = 20f;
	
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
	private boolean reproducirSonidoCorrer,esOponente;
	private float duracionQuieto = 0.2f,duracionCorrer = 0.15f,tiempoMonedas = 0f;
	private int seleccion = 0;
	
	public Personaje(String rutaPj,String[] animacionSaltos,String[] animacionEstados,World mundo,Entradas entradas,Colision c,OrthographicCamera cam,boolean ladoDerecho,boolean esOponente,TipoPersonaje tipo) {
		this.animacionSaltos = animacionSaltos;
		this.animacionEstados = animacionEstados;
		this.ladoDerecho = ladoDerecho;
		this.c = c;
		this.entradas = entradas;
		this.poderes = new Poder[(tipo == TipoPersonaje.NACIDO_BRUMA)?3:1];
		this.tipo = tipo;
		this.monedas = 10;	//Monedas iniciales 10.
		this.esOponente = esOponente;
		movimiento = new Vector2();
		f = new Fisica();
		cm = new ColisionMouse(mundo,cam);
 		spr = new Imagen(rutaPj);
 		spr.setEscalaBox2D(12);
		crearAnimaciones();
		crearBody(mundo);
		if(!esOponente) {crearPoderes(mundo,cam,c);} 	//Si es oponente no se crean los poderes.
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
		pj.setFixedRotation(true);		//Para que el body no rote.
		f.getPolygon().dispose();
	}

	private void updateAnimacion(float delta) {		//Este metodo updatea que frame de la animacion se va a mostrar actualmente,lo llamo en draw().

		animacionQuieto.update(delta);
		animacionCorrer.update(delta);
	}
	
	public void draw(float delta) {		
		
		updateAnimacion(delta);
		
		calcularAcciones();	//Activa o desactiva las acciones del pj en base al input.
		calcularSalto();	//Calcula el salto con la gravedad.
		calcularMovimiento();	//Calcula el movimiento.
		
		pj.setLinearVelocity(movimiento);	//Aplico al pj velocidad lineal, tanto para correr como para saltar.
		spr.setPosicion(pj.getPosition().x, pj.getPosition().y);	//Le digo al Sprite que se ponga en la posicion del body.
		
		animar();	//Animacion del pj.
		reproducirSFX();	//Efectos de sonido.
		
		if(!esOponente){		//Si es oponente no se manipulan los poderes ya que no tiene.
			aumentarEnergia(delta);	//Aumento de los poderes.
			quemarPoder();	//Seleccion de poderes. Y demas acciones respecto a los mismos.
		}
	}
	private void aumentarEnergia(float delta) {
		this.tiempoMonedas += delta;	
		/*Inconvenientes de hacer esto: Cuando se termina la energia pero vos seguis manteniendo el disparo 
		con Acero, las monedas se disparan pero se caen al no haber energia, pasa esto porque se va aumentando y 
		permite que haya un minimo de energia para el disparo cuando la condicion del disparo es que energia > 0*/
		for (int i = 0; i < poderes.length; i++) {	
			if(poderes[i].getEnergia() < 100) {
				Listeners.aumentarPoderPj(this.tipo, poderes[i].getTipoPoder(), 0.05f);
			}
			if(this.tiempoMonedas > 2) { //Es el cooldown que tiene la regeneracion de las monedas.
				if(this.monedas < 10) {		//Si las monedas son 10 no se aumentan pero si se resetea el contador.
					Listeners.aumentarMonedas();
				}
				this.tiempoMonedas = 0;
			}
		}
	}

	private void quemarPoder() {
		if(tipo == TipoPersonaje.NACIDO_BRUMA){		//Si es nacido de la bruma, puede seleccionar los poderes.
			if(entradas.isPrimerPoder()) {seleccion = 0;}
			else if(entradas.isSegundoPoder()) {seleccion = 1;}
			else if(Gdx.input.isKeyJustPressed(Keys.R) || ((Peltre)poderes[2]).isPoderActivo()) {poderes[2].quemar();}
		}//Si es violento y toco la R o si es violento y el poder esta activo se llama al metodo quemar(),logica tiene porque si esta activo, se esta quemando.
		if((tipo == TipoPersonaje.VIOLENTO) && (Gdx.input.isKeyJustPressed(Keys.R) || ((Peltre)poderes[seleccion]).isPoderActivo())){poderes[seleccion].quemar();}		
		
		// Este if le sirve tanto al nacido de la bruma como a atraedor y lanzamonedas.
		if(disparando && tipo != TipoPersonaje.VIOLENTO) {poderes[seleccion].quemar();} 
		
		// Chequea todo el tiempo calcularFuerzas() porque lo que pasa es que todo lo de Disparo no se puede chequear en Acero.
		if(poderes[seleccion].getTipoPoder() == TipoPoder.ACERO) {
			poderes[seleccion].getDisparo().calcularFuerzas(disparando);	
			if(Gdx.input.isKeyJustPressed(Keys.X)) {		//Si la seleccion es Acero y toca la X, se cambia la opcion.
				((Acero)poderes[seleccion]).cambiarOpcion();	//Lo casteo porque cambiarOpcion() es propia de Acero.
			}
		}
		//Si el poder seleccionado es hierro o acero pero con la opcion de empujar, se dibuja el puntero.
		if((poderes[seleccion].getTipoPoder() == TipoPoder.HIERRO) || (poderes[seleccion].getTipoPoder() == TipoPoder.ACERO && ((Acero)poderes[seleccion]).getOpcion() == OpcionAcero.EMPUJE)) {
			cm.dibujar(new Vector2(pj.getPosition().x,pj.getPosition().y), new Vector2(entradas.getMouseX()/Box2dConfig.PPM,entradas.getMouseY()/Box2dConfig.PPM));
		}
		
	}

	private void calcularAcciones() {
		//RECORDATORIO: Esto de ladoDerecho y de cambiarle las teclas es para probar las colisiones sin utilizar redes.	
		correrDerecha = ((!esOponente)?entradas.isIrDerD():false);
		correrIzquierda = ((!esOponente)?entradas.isIrIzqA():false);
		saltar = (((!esOponente)?Gdx.input.isKeyJustPressed(Keys.SPACE):false) && this.c.isPuedeSaltar(pj));
		puedeMoverse = (correrDerecha != correrIzquierda);	//Si el jugador toca las 2 teclas a la vez no va a poder moverse.
		estaQuieto = ((!correrDerecha == !correrIzquierda) || !puedeMoverse && this.c.isPuedeSaltar(pj));
		estaCorriendo = ((correrDerecha || correrIzquierda) && puedeMoverse && this.c.isPuedeSaltar(pj));
		primerSalto = (movimiento.y > impulsoY - 8 && movimiento.y <= impulsoY);
		segundoSalto = (movimiento.y > 0 && movimiento.y <= impulsoY - 8);
		caidaSalto = (movimiento.y < 0);
		apuntando =	(entradas.isBotonDer()); 	//este booleano se utiliza en el metodo drawLineaDisparo().
		disparando = (entradas.isBotonIzq() && apuntando);	
	}

	//Metodo que administra los sonidos de los pj.
	private void reproducirSFX() {
		if(estaCorriendo) {
			if(!reproducirSonidoCorrer) {
				Render.audio.pjCorriendo.play(Render.audio.getVolumen(TipoAudio.SONIDO));
				reproducirSonidoCorrer = true;
			}
		}else {
			if(reproducirSonidoCorrer) {
				Render.audio.pjCorriendo.stop();
				reproducirSonidoCorrer = false;
			}
		}
		if(saltar) {
			Render.audio.pjSalto.play(Render.audio.getVolumen(TipoAudio.SONIDO));
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
		f.dispose();
		cm.dispose();
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
			movimiento.y = impulsoY;
		}else {
			movimiento.y = pj.getLinearVelocity().y;	//Esto hace que actue junto a la gravedad del mundo.
		}
	}
	private void calcularMovimiento() {
		if(puedeMoverse) {
			if(correrDerecha) {
				movimiento.x = velocidadX;
			} else if (correrIzquierda){
				movimiento.x = -velocidadX;
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
	public void aplicarFuerza(Vector2 movimiento) {	//Metodo que se usa en la clase Disparo para los poderes, Acero y Hierro.
		pj.setLinearVelocity(movimiento);
	}
	public void aumentarVelocidad() {		//Metodo que se usa en el poder Peltre.
		velocidadX = velocidadX*2;	
		impulsoY = impulsoY*1.5f;	//x2 es mucho.
	}
	
	public void reducirVelocidad() { 	//Metodo que se usa en el poder Peltre.
		velocidadX = 15f;		//Se vuelven a las velocidades iniciales.
		impulsoY = 20f;
	}
	
	@Override
	public void reducirVida(float dano) {
		this.vida -= dano;
	}
	@Override
	public void restarMonedas() {
		this.monedas--;
	}
	@Override
	public void aumentarMonedas() {
		this.monedas++;
	}
	
	public int getCantMonedas() {
		return this.monedas;
	}
	public ColisionMouse getColisionMouse() {
		return this.cm;
	}
	public TipoPersonaje getTipo() {
		return this.tipo;
	}
	
}
