package com.bakpun.mistborn.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.box2d.ColisionMouse;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.elementos.Animacion;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.Accion;
import com.bakpun.mistborn.enums.Movimiento;
import com.bakpun.mistborn.enums.OpcionAcero;
import com.bakpun.mistborn.enums.Spawn;
import com.bakpun.mistborn.enums.TipoAudio;
import com.bakpun.mistborn.enums.TipoCliente;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.EventoGestionMonedas;
import com.bakpun.mistborn.eventos.EventoInformacionPj;
import com.bakpun.mistborn.eventos.EventoReducirVida;
import com.bakpun.mistborn.eventos.EventoTerminaPartida;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Acero;
import com.bakpun.mistborn.poderes.Peltre;
import com.bakpun.mistborn.poderes.Poder;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public abstract class Personaje implements EventoTerminaPartida,EventoReducirVida,EventoGestionMonedas, EventoInformacionPj{
	
	private float velocidadX = 15f, impulsoY = 20f;
	
	private float x,y;
	
	private Animacion animacionQuieto,animacionCorrer;
	private Imagen spr,cursor;
	private Entradas entradas;
	//private Body pj;
	private Fisica f;
	private ColisionMouse cm;
	private TextureRegion saltos[] = new TextureRegion[3];
	private String animacionSaltos[] = new String[3];
	private String animacionEstados[] = new String[2];
	private Vector2 colMouse;
	protected Poder poderes[];
	private int monedas;
	private TipoPersonaje tipoPj;
	private TipoCliente tipoCliente;
	private Movimiento estadoAnima;
	
	private boolean saltar,puedeMoverse,estaSaltando,estaQuieto,apuntando,disparando,correrDerecha,correrIzquierda,golpear;
	private boolean reproducirSonidoCorrer,flagDanoRecibido,flagBloquearEntradas;
	private float duracionQuieto = 0.2f,duracionCorrer = 0.15f,tiempoMonedas = 0f, tiempoColor = 0f;
	private int seleccion = 0,frameIndex = 0;
	
	public Personaje(String rutaPj,String[] animacionSaltos,String[] animacionEstados,World mundo,Entradas entradas,Colision c,OrthographicCamera cam,Spawn spawn,TipoCliente tipoCliente,TipoPersonaje tipoPj) {
		this.animacionSaltos = animacionSaltos;
		this.animacionEstados = animacionEstados;
		this.entradas = entradas;
		this.poderes = new Poder[(tipoPj == TipoPersonaje.NACIDO_BRUMA)?3:1];
		this.tipoPj = tipoPj;
		this.monedas = 10;	//Monedas iniciales 10.
		this.tipoCliente = tipoCliente;
		f = new Fisica();
		cm = new ColisionMouse(mundo,cam);
		colMouse = new Vector2();
 		spr = new Imagen(rutaPj);
 		cursor = new Imagen(Recursos.CURSOR_COLISIONMOUSE);
 		cursor.setEscalaBox2D(24);
 		spr.setEscalaBox2D(12);
		crearAnimaciones();
		if(this.tipoCliente == TipoCliente.USUARIO) {crearPoderes(mundo,cam,c);} 	//Si es oponente no se crean los poderes.
		Listeners.agregarListener(this);
	}
	
	protected abstract void crearPoderes(World mundo,OrthographicCamera cam,Colision c);

	private void updateAnimacion(float delta) {		//Este metodo updatea que frame de la animacion se va a mostrar actualmente,lo llamo en draw().

		animacionQuieto.update(delta);
		animacionCorrer.update(delta);
	}
	
	public void draw(float delta) {		
		
		updateAnimacion(delta);
		
		calcularAcciones();	//Activa o desactiva las acciones del pj en base al input.
		if(tipoCliente == TipoCliente.USUARIO && !flagBloquearEntradas){		//Si es oponente no se calcula ni el mov,salto y poderes ya que se genera un conflicto con el server.
			Listeners.ejecutar(((golpear)?Accion.GOLPE:(disparando)?Accion.DISPARANDO:(Gdx.input.isKeyJustPressed(Keys.X))?Accion.TOCA_X:Accion.NADA));
			Listeners.posMouse(this.entradas.getMouseX()/Box2dConfig.PPM,this.entradas.getMouseY()/Box2dConfig.PPM);
			calcularSalto();	//Calcula el salto con la gravedad.
			calcularMovimiento();	//Calcula el movimiento.
			//aumentarEnergia(delta);	//Aumento de los poderes.
			quemarPoder();	//Seleccion de poderes. Y demas acciones respecto a los mismos.
		}
		
		spr.setPosicion(this.x, this.y);	//Le digo al Sprite que se ponga en la posicion del body.
		
		if(flagDanoRecibido){colorearGolpe(delta);}
		
		animar();	//Animacion del pj.
		reproducirSFX();	//Efectos de sonido.
	}
	
	
	private void aumentarEnergia(float delta) {
		this.tiempoMonedas += delta;	
		/*Inconvenientes de hacer esto: Cuando se termina la energia pero vos seguis manteniendo el disparo 
		con Acero, las monedas se disparan pero se caen al no haber energia, pasa esto porque se va aumentando y 
		permite que haya un minimo de energia para el disparo cuando la condicion del disparo es que energia > 0*/
		for (int i = 0; i < poderes.length; i++) {	
			if(poderes[i].getEnergia() < 100) {
				Listeners.aumentarPoderPj(this.tipoPj, poderes[i].getTipoPoder(), 0.05f);
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
		if(tipoPj == TipoPersonaje.NACIDO_BRUMA){		//Si es nacido de la bruma, puede seleccionar los poderes.
			if(entradas.isPrimerPoder()) {Listeners.seleccionPoder(TipoPoder.ACERO);}
			else if(entradas.isSegundoPoder()) {Listeners.seleccionPoder(TipoPoder.HIERRO);}
			else if(Gdx.input.isKeyJustPressed(Keys.R) || ((Peltre)poderes[2]).isPoderActivo()) {poderes[2].quemar();}
		}//Si es violento y toco la R o si es violento y el poder esta activo se llama al metodo quemar(),logica tiene porque si esta activo, se esta quemando.
		if((tipoPj == TipoPersonaje.VIOLENTO) && (Gdx.input.isKeyJustPressed(Keys.R) || ((Peltre)poderes[seleccion]).isPoderActivo())){poderes[seleccion].quemar();}		
		
		// Este if le sirve tanto al nacido de la bruma como a atraedor y lanzamonedas.
		if(disparando && tipoPj != TipoPersonaje.VIOLENTO) {poderes[seleccion].quemar();} 
		
		// Chequea todo el tiempo calcularFuerzas() porque lo que pasa es que todo lo de Disparo no se puede chequear en Acero.
		if(poderes[seleccion].getTipoPoder() == TipoPoder.ACERO) {
			//poderes[seleccion].getDisparo().calcularFuerzas(disparando);	
			if(Gdx.input.isKeyJustPressed(Keys.X)) {		//Si la seleccion es Acero y toca la X, se cambia la opcion.
				((Acero)poderes[seleccion]).cambiarOpcion();	//Lo casteo porque cambiarOpcion() es propia de Acero.
			}
		}
		//Si el poder seleccionado es hierro o acero pero con la opcion de empujar, se dibuja el puntero.
		if((poderes[seleccion].getTipoPoder() == TipoPoder.HIERRO) || (poderes[seleccion].getTipoPoder() == TipoPoder.ACERO && ((Acero)poderes[seleccion]).getOpcion() == OpcionAcero.EMPUJE)) {
			cursor.setPosicion(colMouse.x, colMouse.y);
			cursor.draw();
		}
		
	}

	private void calcularAcciones() {
		//RECORDATORIO: Esto de ladoDerecho y de cambiarle las teclas es para probar las colisiones sin utilizar redes.	
		correrDerecha = ((tipoCliente == TipoCliente.USUARIO)?entradas.isIrDerD():false);
		correrIzquierda = ((tipoCliente == TipoCliente.USUARIO)?entradas.isIrIzqA():false);
		saltar = (((tipoCliente == TipoCliente.USUARIO)?Gdx.input.isKeyJustPressed(Keys.SPACE):false));
		puedeMoverse = (correrDerecha != correrIzquierda);	//Si el jugador toca las 2 teclas a la vez no va a poder moverse.
		estaQuieto = ((!correrDerecha == !correrIzquierda) || !puedeMoverse);
		apuntando =	(entradas.isBotonDer()); 	//este booleano se utiliza en el metodo drawLineaDisparo().
		disparando = (entradas.isBotonIzq() && apuntando);	
		golpear = Gdx.input.isButtonJustPressed(Buttons.LEFT);
	}

	//Metodo que administra los sonidos de los pj.
	private void reproducirSFX() {
		if(estadoAnima != Movimiento.QUIETO && !estaSaltando) {
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
		if(saltar && !estaSaltando) {
			Render.audio.pjSalto.play(Render.audio.getVolumen(TipoAudio.SONIDO));
		}
	}

	private void animar() {
		if(!estaSaltando) {
			if(estadoAnima == Movimiento.QUIETO) {		
				spr.draw(animacionQuieto.getFrames()[frameIndex]);
			}else if((estadoAnima == Movimiento.DERECHA || estadoAnima == Movimiento.IZQUIERDA)) { 	//Si esta corriendo muestra el fotograma actual de la animacionCorrer.
				spr.draw(animacionCorrer.getFrames()[frameIndex]);
			}
		}else{		
			spr.draw(saltos[frameIndex]);		//saltos[] contiene las diferentes texturas, se van cambiando en base a la altura, o a la caida,																	
		}										//por eso no lo hice con la clase Animacion, porque no es constante esto.
		
		if(estadoAnima != Movimiento.QUIETO) {			//cuando !estaQuieto va a poder flipearse el pj, porque sino se queda mirando para un lado que no es.
			spr.flip((estadoAnima == Movimiento.DERECHA)?false:true);
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
			Listeners.mover(Movimiento.SALTO);
		}
	}
	private void calcularMovimiento() {
		if(puedeMoverse) {
			if(correrDerecha) {
				Listeners.mover(Movimiento.DERECHA);
			} else if (correrIzquierda){
				Listeners.mover(Movimiento.IZQUIERDA);
			}
		}
		if(estaQuieto) {	//Esto para que no se quede deslizando.
			Listeners.mover(Movimiento.QUIETO);
		}
	}
	public float getX() {
		//return this.pj.getPosition().x;
		return this.x;
	}
	public float getY() {
		//return this.pj.getPosition().y;
		return this.y;
	}
	//public Body getBody() {
		//return this.pj;
	//}
	
	public Entradas getInput() {
		return this.entradas;
	}
	public void aplicarFuerza(Vector2 movimiento) {	//Metodo que se usa en la clase Disparo para los poderes, Acero y Hierro.
		//pj.setLinearVelocity(movimiento);
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
	public void reducirVida(float dano, TipoCliente cliente) {
		if(this.tipoCliente == cliente) {
			flagDanoRecibido = true;	
		}
	}
	
	private void colorearGolpe(float delta) {
		tiempoColor += delta;
		if(tiempoColor >= 0.4f) {
			spr.setColor(Color.WHITE);
			tiempoColor = 0f;
			flagDanoRecibido = false;
		}else {
			spr.setColor(Color.RED);
		}
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
		return this.tipoPj;
	}

	@Override
	public void actualizarPos(TipoCliente tipoCliente, float x, float y) {
		if(this.tipoCliente == tipoCliente){
			this.x = x;
			this.y = y;	
		}	
	}

	@Override
	public void actualizarAnima(TipoCliente tipoCliente, int frameIndex, Movimiento mov, boolean saltando) {
		if(this.tipoCliente == tipoCliente){
			this.frameIndex = frameIndex;
			this.estadoAnima = mov;
			this.estaSaltando = saltando;
		}	
	}
	
	@Override
	public void terminarPartida(String texto,TipoCliente ganador) {
		if(ganador != this.tipoCliente) {	//Si vos sos diferente al ganador vos te moris porque perdiste.
			spr.setAngulo(90);
			spr.setColor(Color.RED);
		}
		this.flagBloquearEntradas = true;
	}
	
	public void actualizarColisionPj(float x, float y) {
		this.colMouse.set(x, y);
	}
	
}
