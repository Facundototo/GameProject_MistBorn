package com.bakpun.mistborn.pantallas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bakpun.mistborn.elementos.Fisica;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.elementos.Personaje;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Box2dConfig;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public class PantallaPvP implements Screen{
	//Tenemos que hacer las plataformas, ya sea la textura,animacion y el codigo.
	//Tambien tengo que ver como poner la ventana Opciones cuando se toca ESCAPE,creo que hay que hacer otra camara.
	private Imagen fondo;
	private Personaje pj;
	private OrthographicCamera cam;
	private Viewport vw;
	private World mundo;
	private Box2DDebugRenderer db;
	private Fisica f;
	private Body piso,plataforma;
	private VentanaOpciones ventanaOpc;
	private Entradas entradas;
	private boolean mostrarOpciones;
	
	public void show() {
		mundo = new World(new Vector2(0,-30f),true);
		f = new Fisica();
		fondo = new Imagen(Recursos.FONDO_PVP);
		cam = new OrthographicCamera(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);	//Para posicionar la camara en el centro del fondo. La camara como default esta con coordenadas negativas.
		cam.update();
		vw = new FillViewport(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM,cam);
		db = new Box2DDebugRenderer();
		crearColisiones();
		entradas = new Entradas();
		Gdx.input.setInputProcessor(entradas);
		pj = new Personaje(Recursos.PERSONAJE_VIN,mundo,entradas);	
		ventanaOpc = new VentanaOpciones(true, cam,entradas);
		fondo.escalarImagen(Box2dConfig.PPM);
	}

	public void render(float delta) {
		Render.limpiarPantalla(1,1,1);
		
		cam.update();	//Updateo la camara.
		Render.batch.setProjectionMatrix(cam.combined);
		
		Render.batch.begin();
		fondo.draw();	//Dibujo el fondo.
		pj.draw(); 	//Updateo al jugador.
		Render.batch.end();
		
		mundo.step(1/60f, 6, 2);	//Updateo el mundo.
		db.render(mundo, cam.combined);		//Muestra los colisiones/cuerpos.
	}
	
	private void crearColisiones() {	
		//Limites Horizontales
		for (int i = 0; i < 2; i++) {
			f.setBody(BodyType.StaticBody, new Vector2(cam.viewportWidth/2,(i==0)?155/Box2dConfig.PPM:1080/Box2dConfig.PPM));
			f.createChain(new Vector2(-((Config.ANCHO/2)/Box2dConfig.PPM),0), new Vector2((Config.ANCHO/2)/Box2dConfig.PPM,0));
			f.setFixture(f.getChain(), 100, 1f, 0);
			if(i==0) {
				piso = mundo.createBody(f.getBody());
				piso.createFixture(f.getFixture());
				piso.setUserData(UserData.SALTO_P); //ID para la colision.
			}else {
				f.createBody(mundo);
			}
			f.getChain().dispose();
			
		}
		//Limites Verticales
		for (int i = 0; i < 2; i++) {
			f.setBody(BodyType.StaticBody, new Vector2((i==0)?1/Box2dConfig.PPM:1920/Box2dConfig.PPM,cam.viewportHeight/2));
			f.createChain(new Vector2(0,-(((Config.ALTO/2)-155)/Box2dConfig.PPM)),new Vector2(0,(Config.ALTO/2)/Box2dConfig.PPM));
			f.setFixture(f.getChain(), 100, 1f, 0);
			f.createBody(mundo);
			f.getChain().dispose();
		}
		//Plataforma.
		f.setBody(BodyType.KinematicBody,new Vector2(cam.viewportWidth/2,300/Box2dConfig.PPM));
		f.createPolygon(80/Box2dConfig.PPM, 16/Box2dConfig.PPM);
		f.setFixture(f.getPolygon(), 10, 0f, 0);
		plataforma = mundo.createBody(f.getBody());
		plataforma.createFixture(f.getFixture());
		plataforma.setUserData(UserData.SALTO_P);	//ID para la colision.
		
	}

	@Override
	public void resize(int width, int height) {
		vw.update(width, height);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
	    cam.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		fondo.getTexture().dispose();	//Texture
		pj.dispose();	//Texture.
		f.dispose();
		Render.batch.dispose();		//SpriteBatch.
		this.dispose();
	}
}
