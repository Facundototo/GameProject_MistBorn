package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.io.Colision;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Box2dConfig;
import com.bakpun.mistborn.utiles.Recursos;

public class Personaje {
	private final float VELOCIDAD_X = 15f, IMPULSO_Y = 20f;
	private Animacion animacionQuieto,animacionCorrer;
	private Imagen spr;
	private Entradas entradas;
	private Body pj;
	private Vector2 movimiento;
	private Fisica f;
	private Colision c;
	private boolean saltar,puedeMoverse,estaCorriendo,estaQuieto;
	private float duracionQuieto = 0.2f,duracionCorrer = 0.15f,delta = 0f;

	
	public Personaje(String rutaPj,World mundo,Entradas entradas) {
		movimiento = new Vector2();
		this.entradas = entradas;
		f = new Fisica();
		Gdx.input.setInputProcessor(this.entradas);
 		spr = new Imagen(rutaPj);
 		spr.escalarImagen(12);
		crearAnimaciones();
		crearBody(mundo);
		c = new Colision();				
		mundo.setContactListener(c);
	}
	
	private void crearBody(World mundo) {
		f.setBody(BodyType.DynamicBody,new Vector2(10,5));
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
		
		saltar = (Gdx.input.isKeyJustPressed(Keys.SPACE) && c.isPuedeSaltar());
		puedeMoverse = (entradas.isIrDer() != entradas.isIrIzq());	//Si el jugador toca las 2 teclas a la vez no va a poder moverse.
		estaQuieto = ((!entradas.isIrDer() == !entradas.isIrIzq()) || !puedeMoverse);
		estaCorriendo = ((entradas.isIrDer() || entradas.isIrIzq()) && puedeMoverse);
		
		calcularSalto();	//Calcula el salto con la gravedad.
		calcularMovimiento();	//Calcula el movimiento.
		
		pj.setLinearVelocity(movimiento);	//Aplico al pj velocidad lineal, tanto para correr como para saltar.
	
		spr.setPosicion(pj.getPosition().x, pj.getPosition().y);	//Le digo al Sprite que se ponga en la posicion del body.
		
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
	private void calcularSalto() {
		if(saltar) {
			movimiento.y = IMPULSO_Y;
		}else {
			movimiento.y = pj.getLinearVelocity().y;	//Esto hace que actue junto a la gravedad del mundo.
		}
	}
	private void calcularMovimiento() {
		if(puedeMoverse) {
			if(entradas.isIrDer()) {
				movimiento.x = VELOCIDAD_X;
			} else if (entradas.isIrIzq()){
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
