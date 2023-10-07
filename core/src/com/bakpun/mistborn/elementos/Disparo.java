package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.personajes.Personaje;

public class Disparo {

	private World mundo;
	private OrthographicCamera cam;
	private ShapeRenderer linea;
	private Personaje pj;
	
	public Disparo(World mundo,Personaje pj,OrthographicCamera cam) {
		this.mundo = mundo;
		this.pj = pj;
		this.cam = cam;
		linea = new ShapeRenderer();

		
	}
	
	public void drawLinea() {
		linea.setProjectionMatrix(cam.combined);		//Linea para debuggear el disparo.
		linea.begin(ShapeType.Line);
		linea.setColor(Color.CYAN);
		linea.line(pj.getX(), pj.getY(),pj.getInput().getMouseX()/Box2dConfig.PPM, pj.getInput().getMouseY()/Box2dConfig.PPM);
		linea.end();
	}
	
	public void disparar() {
		
	}
	
	
	
	
	
	
	
}
