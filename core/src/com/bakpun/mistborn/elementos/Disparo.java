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

public class Disparo {

	private World mundo;
	private OrthographicCamera cam;
	private ShapeRenderer linea;
	private Personaje pj;
	private Fisica f;
	private Body moneda;
	private Vector2 direccion;
	
	public Disparo(World mundo,Personaje pj,OrthographicCamera cam) {
		this.mundo = mundo;
		this.pj = pj;
		this.cam = cam;
		linea = new ShapeRenderer();
		f = new Fisica();
		direccion = new Vector2();
	}
	
	public void drawLinea() {
		linea.setProjectionMatrix(cam.combined);		//Linea para debuggear el disparo.
		linea.begin(ShapeType.Line);
		linea.setColor(Color.CYAN);
		linea.line(pj.getX(), pj.getY(),pj.getInput().getMouseX()/Box2dConfig.PPM, pj.getInput().getMouseY()/Box2dConfig.PPM);
		linea.end();
	}
	
	public void disparar() {
		direccion.set(pj.getInput().getMouseX()/Box2dConfig.PPM - pj.getX(), pj.getInput().getMouseY()/Box2dConfig.PPM - pj.getY());
		f.setBody(BodyType.DynamicBody,new Vector2((pj.getInput().getMouseX()/Box2dConfig.PPM > 0)?pj.getX()+0.5f:pj.getX()-6,pj.getY()));
		f.createPolygon(6/Box2dConfig.PPM, 4/Box2dConfig.PPM);	
		f.setFixture(f.getPolygon(), 5, 0, 0);
		moneda = mundo.createBody(f.getBody());		//No anda disparar para la izq.
		moneda.createFixture(f.getFixture());
		moneda.setBullet(true);
		moneda.setLinearVelocity(direccion.scl(3.0f));
	}
	
	
	
	
	
	
}
