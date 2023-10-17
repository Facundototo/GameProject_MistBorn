package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.personajes.Personaje;

public final class Disparo {

	private World mundo;
	private OrthographicCamera cam;
	private ShapeRenderer linea;
	private Personaje pj;
	private Fisica f;
	private Body moneda;
	private Vector2 direccion,posIniBala;
	private final float _amplitud = 1.5f;
	
	public Disparo(World mundo,Personaje pj,OrthographicCamera cam) {
		this.mundo = mundo;
		this.pj = pj;
		this.cam = cam;
		linea = new ShapeRenderer();
		f = new Fisica();
		direccion = new Vector2();
		posIniBala = new Vector2();
	}
	
	public void drawLinea() {
		linea.setProjectionMatrix(cam.combined);		//Linea para debuggear el disparo.
		linea.begin(ShapeType.Line);
		linea.setColor(Color.CYAN);
		linea.line(pj.getX(), pj.getY(),pj.getInput().getMouseX()/Box2dConfig.PPM, pj.getInput().getMouseY()/Box2dConfig.PPM);
		linea.end();
	}
	
	public void disparar() {
		direccion.set(pj.getInput().getMouseX()/Box2dConfig.PPM - pj.getX(), pj.getInput().getMouseY()/Box2dConfig.PPM - pj.getY()); //Calcula solo la direccion no la distancia. con el .nor()
		direccion.nor(); // direccion se normaliza para asegurarse de que tenga una longitud de 1, lo que significa que indica solo la dirección sin importar la distancia.
		
	    posIniBala.set(pj.getX() + _amplitud * direccion.x, pj.getY() + _amplitud * direccion.y);	//Agarra la pos del pj y la suma con la direccion(normalizada es igual a 1) por la amplitud(radio).
		
		f.setBody(BodyType.DynamicBody,posIniBala);
		f.createPolygon(6/Box2dConfig.PPM, 4/Box2dConfig.PPM);	
		f.setFixture(f.getPolygon(), 5, 0, 0);
		
		moneda = mundo.createBody(f.getBody());	
		moneda.createFixture(f.getFixture());
		moneda.setBullet(true);		//Identifico al body como bullet(bala),esto porque Box2D hace chequeos mas rigurosos con los bodies que tienen mucha velocidad.
		//moneda.setLinearVelocity(direccion.scl(50.0f));	//Escalo la direccion y lo utilizo como velocidad.
	}
	
	public void calcularFuerzas() {
		moneda.setLinearVelocity(direccion.scl(50.0f));
	}
	
	
	
	
	
	
}
