package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.elementos.Hud;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.elementos.Plataforma;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.personajes.Ham;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.personajes.Vin;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public final class PantallaPvP implements Screen{

	private Imagen fondo;
	private Personaje pj1,pj2;
	private OrthographicCamera cam;
	private Viewport vw;
	private World mundo;
	private Box2DDebugRenderer db;
	private Fisica f;
	private Body piso;
	private Entradas entradasPj1,entradasPj2;
	private Plataforma plataformas[] = new Plataforma[7];
	private Vector2[] posicionPlataformas = {new Vector2(400/Box2dConfig.PPM,325/Box2dConfig.PPM),new Vector2(1400/Box2dConfig.PPM,325/Box2dConfig.PPM)
			,new Vector2(1400/Box2dConfig.PPM,650/Box2dConfig.PPM),new Vector2(500/Box2dConfig.PPM,750/Box2dConfig.PPM),new Vector2(750/Box2dConfig.PPM,500/Box2dConfig.PPM),
			new Vector2(1750/Box2dConfig.PPM,500/Box2dConfig.PPM),new Vector2(1000/Box2dConfig.PPM,600/Box2dConfig.PPM)};
	private InputMultiplexer im;
	private Hud hud;
	private Colision colisionMundo;		//Colision global, la unica en todo el juego.

	
	//Para que quede bien, me faltaria adaptar las plataformas y los pj a las diferentes resoluciones.
	
	public void show() {
		mundo = new World(new Vector2(0,-30f),true);
		creandoInputs();
		hud = new Hud();
		f = new Fisica();
		fondo = new Imagen(Recursos.FONDO_PVP);
		fondo.setTamano(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM);
		cam = new OrthographicCamera(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);	
		vw = new FillViewport(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM,cam);
		db = new Box2DDebugRenderer();
		pj1 = new Vin(mundo,entradasPj1,colisionMundo,false);
		pj2 = new Ham(mundo,entradasPj2,colisionMundo,true);
		
		crearPlataformas();
		crearLimites();
	}

	public void render(float delta) {
		Render.limpiarPantalla(0,0,0);
		cam.update();	
		
		Render.batch.setProjectionMatrix(cam.combined);
		
		Render.batch.begin();
		fondo.draw();	//Dibujo el fondo.
		pj1.draw(); 	//Updateo al jugador.
		pj2.draw();		//Updateo al jugador2.
		for (int i = 0; i < plataformas.length; i++) {
			plataformas[i].draw(delta);		//Dibujo las plataformas.
		}
		Render.batch.end();
		
		hud.draw(delta);	//Dibujo el hud.
		
		mundo.step(1/60f, 6, 2);	//Updateo el mundo.
		db.render(mundo, cam.combined);		//Muestra los colisiones/cuerpos.
	}

	@Override
	public void resize(int width, int height) {
		vw.update(width, height);
		hud.getStage().getViewport().update(width, height, true);
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
		pj1.dispose();	//Texture.
		pj2.dispose();	//Texture.
		f.dispose();	
		Render.batch.dispose();		//SpriteBatch.
		this.dispose();
	}
	
	private void creandoInputs() {
		colisionMundo = new Colision();
		entradasPj1 = new Entradas();		//Creo 2 entradas, porque sino se superponen.
        entradasPj2 = new Entradas();
		im = new InputMultiplexer();
		
		im.addProcessor(entradasPj1);		//Multiplexor porque hay mas de un input.
		im.addProcessor(entradasPj2);
		
		Gdx.input.setInputProcessor(im);		//Seteo entradas.
		mundo.setContactListener(colisionMundo); 
	}

	private void crearPlataformas() {
		for (int i = 0; i < plataformas.length; i++) {
			plataformas[i] = new Plataforma((i>=4)?true:false,posicionPlataformas[i],mundo);
		}
	}
	
	private void crearLimites() {	
		//Limites Horizontales
		for (int i = 0; i < 2; i++) {
			f.setBody(BodyType.StaticBody, new Vector2(cam.viewportWidth/2,(i==0)?155/Box2dConfig.PPM:(Config.ALTO-20)/Box2dConfig.PPM));
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
			f.setBody(BodyType.StaticBody, new Vector2((i==0)?30/Box2dConfig.PPM:(Config.ANCHO-20)/Box2dConfig.PPM,cam.viewportHeight/2));
			f.createChain(new Vector2(0,-(((Config.ALTO/2)-155)/Box2dConfig.PPM)),new Vector2(0,(Config.ALTO/2)/Box2dConfig.PPM));
			f.setFixture(f.getChain(), 100, 1f, 0);
			f.createBody(mundo);
			f.getChain().dispose();
		}
		
	}
	
}
