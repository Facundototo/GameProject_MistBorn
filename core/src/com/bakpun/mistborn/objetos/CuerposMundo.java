package com.bakpun.mistborn.objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.bakpun.mistborn.elementos.Box2dConfig;
import com.bakpun.mistborn.utiles.Config;

public class CuerposMundo {		//Creamos esta clase para tener mas organizado PantallaPvP con el tema de la creacion de las plataformas,metales y limites.

	private Plataforma plataformas[] = new Plataforma[5];	
	private Metal[] metales = new Metal[3];
	private boolean plataformasCreadas = false,metalesCreados = false;
	
	
	public void draw() {
		if(metalesCreados) {		// Si se crearon los metales se dibujan
			for (int i = 0; i < metales.length; i++) {
				metales[i].draw();
			}
		}
		if(plataformasCreadas) {	// Si se crearon las plataformas se dibujan.
			for (int i = 0; i < plataformas.length; i++) {
				plataformas[i].draw(Gdx.graphics.getDeltaTime());
			}
		}
	}
	
	public void crearPlataformas() {
		Vector2[] posicionPlataformas = {new Vector2((Config.ANCHO/4)/Box2dConfig.PPM,(Config.ALTO/1.3f)/Box2dConfig.PPM),new Vector2((Config.ANCHO/1.3f)/Box2dConfig.PPM,(Config.ALTO/1.3f)/Box2dConfig.PPM)
				,new Vector2(Config.ANCHO/2/Box2dConfig.PPM,Config.ALTO/1.7f/Box2dConfig.PPM),new Vector2((Config.ANCHO/1.3f)/Box2dConfig.PPM,(Config.ALTO/2.5f)/Box2dConfig.PPM),new Vector2((Config.ANCHO/4)/Box2dConfig.PPM,(Config.ALTO/3f)/Box2dConfig.PPM)};
				
		for (int i = 0; i < plataformas.length; i++) {
			plataformas[i] = new Plataforma(true,posicionPlataformas[i]);
		}
		
		this.plataformasCreadas = true;
	}
	
	public void crearMetales() {
		Vector2[] posMetales = {new Vector2((Config.ANCHO/2)/Box2dConfig.PPM,(Config.ALTO-20)/Box2dConfig.PPM),new Vector2((Config.ANCHO-20)/Box2dConfig.PPM,(Config.ALTO/2)/Box2dConfig.PPM)
				,new Vector2((20)/Box2dConfig.PPM,(Config.ALTO/2)/Box2dConfig.PPM)};
		int[] grados = {90,0,0};
		
		for (int i = 0; i < metales.length; i++) {
			metales[i] = new Metal(posMetales[i],grados[i]);
		}
		
		this.metalesCreados = true;
	}
	
	public void dispose() {
		for (int i = 0; i < metales.length; i++) {
			metales[i].dispose();
		}
		for (int i = 0; i < plataformas.length; i++) {
			plataformas[i].dispose();
		}
	}
	
}
