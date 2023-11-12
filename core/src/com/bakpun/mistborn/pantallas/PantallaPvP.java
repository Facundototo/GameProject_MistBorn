package com.bakpun.mistborn.pantallas;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.Spawn;
import com.bakpun.mistborn.enums.TipoCliente;
import com.bakpun.mistborn.hud.Hud;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.objetos.CuerposMundo;
import com.bakpun.mistborn.objetos.GestorMonedas;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Red;
import com.bakpun.mistborn.utiles.Render;

public final class PantallaPvP implements Screen{

	private OrthographicCamera cam;
	private World mundo;
	private Entradas entradasPj1,entradasPj2;
	private Colision colisionMundo;
	private Imagen fondo;
	private Personaje pj1,pj2;
	private Viewport vw;
	private Box2DDebugRenderer db;
	private InputMultiplexer im;
	private Hud hud;
	private String nombrePj1, nombrePj2;
	private Pixmap cursor;
	private CuerposMundo entidades;
	private int nroOponente;
	
	public PantallaPvP(String clasePj1, String clasePj2,int nroOponente) {
		Render.audio.cancionBatalla.play();
		Render.audio.cancionBatalla.setLooping(true);
		this.nombrePj1 = clasePj1;  //Pasa el nombre de la clase del Personaje que eligio y lo creo con reflection.
		this.nombrePj2 = clasePj2;	
		this.nroOponente = nroOponente;
	}
	
	public void show() {
		colisionMundo = new Colision(); 	//Colision global, la unica en todo el juego.
		cam = new OrthographicCamera(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM);
		mundo = new World(new Vector2(0,-30f),true);
		creandoInputs();
		fondo = new Imagen(Recursos.FONDO_PVP);
		fondo.setTamano(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);	
		vw = new FillViewport(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM,cam);
		db = new Box2DDebugRenderer();
		hud = new Hud();
		pj1 = crearPersonaje(this.nombrePj1,entradasPj1,Spawn.IZQUIERDA,((nroOponente == 2)?TipoCliente.USUARIO:TipoCliente.OPONENTE));//Si el cliente es el pj1 el oponente es el pj2
		pj2 = crearPersonaje(this.nombrePj2,entradasPj2,Spawn.DERECHA,((nroOponente == 2)?TipoCliente.OPONENTE:TipoCliente.USUARIO));	//Si el cliente es el pj2 el oponente es el pj1.
		
		GestorMonedas.mundo = mundo;
		GestorMonedas.c = colisionMundo;
		
		entidades = new CuerposMundo(mundo,cam);
		entidades.crearPlataformas();	
		entidades.crearMetales();
		
		cursor = new Pixmap(Gdx.files.internal(Recursos.CURSOR_MOUSE));	//Cargamos un cursor.
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
	}

	public void render(float delta) {
		Render.limpiarPantalla(0,0,0);
		cam.update();	
		
		Red.chequearEstado();
		
		Render.batch.setProjectionMatrix(cam.combined);
		Render.batch.begin();
		
		fondo.draw();	//Dibujo el fondo.
		pj1.draw(delta); 	//Updateo al jugador.
		pj2.draw(delta);		//Updateo al jugador2.
		entidades.draw();		//Se dibujan las entidades del mundo.
		GestorMonedas.drawMonedas();	//Se dibujan las monedas.
		
		Render.batch.end();
		
		hud.draw(delta);	//Dibujo el hud.
		GestorMonedas.borrarBasura();		//Se llama siempre a este metodo static para que borre las monedas.
		
		mundo.step(1/60f, 6, 2);	//Updateo el mundo.
		db.render(mundo, cam.combined);		//Muestra los colisiones/cuerpos.
	}

	@Override
	public void resize(int width, int height) {
		vw.update(width, height,true);
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
		cursor.dispose();	
		entidades.dispose();
		Render.batch.dispose();		//SpriteBatch.
		this.dispose();
	}
	
	private void creandoInputs() {
		entradasPj1 = new Entradas();
		entradasPj2 = new Entradas();
		im = new InputMultiplexer();
		im.addProcessor(entradasPj1);		//Multiplexor porque hay mas de un input.
		im.addProcessor(entradasPj2);		//Creo 2 entradas, porque sino se superponen.
		
		Gdx.input.setInputProcessor(im);		//Seteo entradas.
		mundo.setContactListener(colisionMundo); 
	}

	private Personaje crearPersonaje(String clasePj,Entradas entrada ,Spawn spawn,TipoCliente tipoCliente) {	//Metodo para la creacion del pj, utilizando Reflection.
		Personaje pj = null;
	    try {
	    	//<?> no sabemos que significa pero si lo sacamos nos sale el mark amarillo.
	        Class<?> clase = Class.forName("com.bakpun.mistborn.personajes." + clasePj);
	        //boolean.class lo ponemos porque el booleano no tiene .getClass(), es lo mismo.
	        Constructor<?> constructor = clase.getConstructor(mundo.getClass(), entradasPj1.getClass(), colisionMundo.getClass(), cam.getClass(),Spawn.class, TipoCliente.class);
	        pj = (Personaje) constructor.newInstance(mundo, entrada, colisionMundo, cam, spawn, tipoCliente);
	    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
	        e.printStackTrace();
	    }
	    return pj;
	}
}
