package com.bakpun.mistborn.pantallas;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bakpun.mistborn.elementos.Box2dConfig;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.Spawn;
import com.bakpun.mistborn.enums.TipoCliente;
import com.bakpun.mistborn.eventos.EventoTerminaPartida;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.hud.Hud;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.objetos.CuerposMundo;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Red;
import com.bakpun.mistborn.utiles.Render;

public final class PantallaPvP implements Screen,EventoTerminaPartida,EventListener{

	private OrthographicCamera cam;
	private Entradas entradasPj1,entradasPj2;
	private Imagen fondo;
	private Personaje pj1,pj2;
	private Viewport vw;
	private InputMultiplexer im;
	private Hud hud;
	private String nombrePj1, nombrePj2;
	private CuerposMundo entidades;
	private int nroOponente;
	private boolean flagTerminaPartida = false;
	
	public PantallaPvP(String clasePj1, String clasePj2,int nroOponente) {
		Render.audio.cancionBatalla.play();
		Render.audio.cancionBatalla.setLooping(true);
		this.nombrePj1 = clasePj1;  //Pasa el nombre de la clase del Personaje que eligio y lo creo con reflection.
		this.nombrePj2 = clasePj2;	
		this.nroOponente = nroOponente;
		Listeners.agregarListener(this);
	}
	
	public void show() {
		cam = new OrthographicCamera(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM);
		creandoInputs();
		fondo = new Imagen(Recursos.FONDO_PVP);
		fondo.setTamano(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);	
		vw = new FillViewport(Config.ANCHO/Box2dConfig.PPM,Config.ALTO/Box2dConfig.PPM,cam);
		hud = new Hud();
		pj1 = crearPersonaje(this.nombrePj1,entradasPj1,Spawn.IZQUIERDA,((nroOponente == 2)?TipoCliente.USUARIO:TipoCliente.OPONENTE));//Si el cliente es el pj1 el oponente es el pj2
		pj2 = crearPersonaje(this.nombrePj2,entradasPj2,Spawn.DERECHA,((nroOponente == 2)?TipoCliente.OPONENTE:TipoCliente.USUARIO));	//Si el cliente es el pj2 el oponente es el pj1.
		
		entidades = new CuerposMundo();
		entidades.crearPlataformas();	
		entidades.crearMetales();
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
		
		Render.batch.end();
		
		hud.draw(delta);	//Dibujo el hud.
		
		if(flagTerminaPartida && (entradasPj1.isEscape() || entradasPj2.isEscape())) {
			Render.audio.cancionBatalla.stop();
			Red.desconectar();
		}	
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
		entidades.dispose();
		this.dispose();
	}
	
	private void creandoInputs() {
		entradasPj1 = new Entradas();
		entradasPj2 = new Entradas();
		im = new InputMultiplexer();
		im.addProcessor(entradasPj1);		//Multiplexor porque hay mas de un input.
		im.addProcessor(entradasPj2);		//Creo 2 entradas, porque sino se superponen.
		
		Gdx.input.setInputProcessor(im);		//Seteo entradas.
	}

	private Personaje crearPersonaje(String clasePj,Entradas entrada ,Spawn spawn,TipoCliente tipoCliente) {	//Metodo para la creacion del pj, utilizando Reflection.
		Personaje pj = null;
	    try {
	    	//<?> no sabemos que significa pero si lo sacamos nos sale el mark amarillo.
	        Class<?> clase = Class.forName("com.bakpun.mistborn.personajes." + clasePj);
	        //boolean.class lo ponemos porque el booleano no tiene .getClass(), es lo mismo.
	        Constructor<?> constructor = clase.getConstructor(entradasPj1.getClass(),Spawn.class, TipoCliente.class);
	        pj = (Personaje) constructor.newInstance(entrada,spawn, tipoCliente);
	    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
	        e.printStackTrace();
	    }
	    return pj;
	}

	@Override
	public void terminarPartida(String texto, TipoCliente ganador) {
		flagTerminaPartida = true;	
	}
}
