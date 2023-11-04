package com.bakpun.mistborn.objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.utiles.Config;

public class CuerposMundo {		//Creamos esta clase para tener mas organizado PantallaPvP con el tema de la creacion de las plataformas,metales y limites.

	private Plataforma plataformas[] = new Plataforma[5];	
	private Metal[] metales = new Metal[3];
	private World mundo;
	private Fisica f;
	private Body piso,pared;
	private boolean plataformasCreadas = false,metalesCreados = false;
	
	public CuerposMundo(World mundo,OrthographicCamera cam) {
		this.mundo = mundo;
		this.f = new Fisica();
		crearLimites(cam);	//Los limites se crean si o si.
	}
	
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
			plataformas[i] = new Plataforma(true,posicionPlataformas[i],mundo);
		}
		
		this.plataformasCreadas = true;
	}
	
	public void crearMetales() {
		Vector2[] posMetales = {new Vector2((Config.ANCHO/2)/Box2dConfig.PPM,(Config.ALTO-20)/Box2dConfig.PPM),new Vector2((Config.ANCHO-20)/Box2dConfig.PPM,(Config.ALTO/2)/Box2dConfig.PPM)
				,new Vector2((20)/Box2dConfig.PPM,(Config.ALTO/2)/Box2dConfig.PPM)};
		int[] grados = {90,0,0};
		
		for (int i = 0; i < metales.length; i++) {
			metales[i] = new Metal(mundo,posMetales[i],grados[i]);
		}
		
		this.metalesCreados = true;
	}
	
	private void crearLimites(OrthographicCamera cam) {
		//Limites Horizontales
		for (int i = 0; i < 2; i++) {
			f.setBody(BodyType.StaticBody, new Vector2(cam.viewportWidth/2,(i==0)?155/Box2dConfig.PPM:(Config.ALTO-20)/Box2dConfig.PPM));
			f.createChain(new Vector2(-((Config.ANCHO/2)/Box2dConfig.PPM),0), new Vector2((Config.ANCHO/2)/Box2dConfig.PPM,0));
			f.setFixture(f.getChain(), 100, 1f, 0);
			pared = mundo.createBody(f.getBody());
			pared.createFixture(f.getFixture());
			pared.setUserData(UserData.SALTO_P);
			f.getChain().dispose();
			
		}
		//Limites Verticales
		for (int i = 0; i < 2; i++) {
			f.setBody(BodyType.StaticBody, new Vector2((i==0)?20/Box2dConfig.PPM:(Config.ANCHO-20)/Box2dConfig.PPM,cam.viewportHeight/2));
			f.createChain(new Vector2(0,-(((Config.ALTO/2)-155)/Box2dConfig.PPM)),new Vector2(0,(Config.ALTO/2)/Box2dConfig.PPM));
			f.setFixture(f.getChain(), 100, 1f, 0);
			piso = mundo.createBody(f.getBody());
			piso.createFixture(f.getFixture());
			piso.setUserData(UserData.PARED);
			f.getChain().dispose();
		}
	}
	
	public void dispose() {
		for (int i = 0; i < metales.length; i++) {
			metales[i].dispose();
		}
		for (int i = 0; i < plataformas.length; i++) {
			plataformas[i].dispose();
		}
		f.dispose();
	}
	
}
