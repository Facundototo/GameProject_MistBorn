package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public class PantallaSeleccion implements Screen{
	private InputMultiplexer im;
	private Entradas entradas;
	private Stage stage;
	private Table tabla;
	private Skin skin = new Skin(Gdx.files.internal(Recursos.SKIN));
	private Image pj;
	private Window informacion;
	private final TextButton[] b = {new TextButton("PERSONAJE 1",skin),new TextButton("PERSONAJE 2",skin),new TextButton("PERSONAJE 3",skin),new TextButton("PERSONAJE 4",skin)};;
	private int seleccion = 0,cantMaxPersonajes = 4;
	private boolean derecha,izquierda;
	private float tiempo = 0;
	private final String[] _textosinfo = {"Vin es una Nacida de la Bruma","Ham es un Violento","Sazed es un Tira Monedas","Zane es un support"};
	private final String[] _nombresPj = {"Vin","Ham","LestiBournes","Dockson"};
	private Label txtInfo,nombrePj;
	
	//Terminar esta PantallaSeleccion (no estan ordenados por prioridad):
	//  - Hacer el enum para mostrar la informacion, aca tendria que ver lo de la traduccion.
	//  - Cambiar el tamano de las letras y el estilo.
	//  - Hacer los disenos de esta pantalla (fondo,fondoPj,Window de la informacionPj).
	//  - Hacer que los botones pasen a ser ImageButton.
	//  - Reflection para la creacion de la instancia Personaje.
	//  - Y por ultimo crear un boton ACEPTAR ELECCION, se mande la PantallaPvp.
	// - Tambien tendriamos que informarnos mas sobre el Table, porque no lo estamos utilizando del todo.
	
	
	public PantallaSeleccion() {
		stage = new Stage(new FillViewport(Config.ANCHO,Config.ALTO));
		tabla = new Table().debug();
		entradas = new Entradas();
		im = new InputMultiplexer();
		txtInfo = new Label("",skin);
		nombrePj = new Label("",skin);
		informacion = new Window("Informacion",skin);
		pj = new Image(new Texture(Recursos.PERSONAJE_VIN));
	}
	
	
	public void show() {
		im.addProcessor(entradas);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		
		
		txtInfo.setWrap(true);
		
		nombrePj.setPosition(250, 150);
		
		
		pj.setSize(500, 500);
		pj.setPosition(100, 300);
		
		informacion.setPosition(1300, 300);
		informacion.setSize(500, 700);
		informacion.setMovable(false);
		
		stage.addActor(nombrePj);
		stage.addActor(informacion);
		stage.addActor(pj);
		
		
		for (int i = 0; i < b.length; i++) {
			final int opc = i;	//Esto lo hago porque i no puede ser final.
			b[i].addListener(new ClickListener() {		//Evento que escucha cuando un boton es clickeado (solo mouse).
				 @Override
		         public void clicked(InputEvent event, float x, float y) {
		             seleccion = opc;
		             mostrarInformacion(seleccion);
				 } 
			});
			
			tabla.add(b[i]).pad(10);
		}
		
		tabla.bottom().padBottom(10);
		tabla.setFillParent(true);		//La tabla ocupa toda la pantalla.
		stage.addActor(tabla);
		
	}
	
	public void render(float delta) {
		Render.limpiarPantalla(0, 0, 0);
		
		calcularTeclas(delta);		//Metodo para el uso del teclado (entradas).
		marcarOpcionSeleccionada();	//Aplicar efectos para la opcion seleccionada.
		
		stage.act(delta);
		stage.draw();
		
	}
	
	private void marcarOpcionSeleccionada() {
		for (int i = 0; i < b.length; i++) {
			if(i == seleccion) {
				b[seleccion].setColor(Color.RED);
				mostrarInformacion(seleccion);
			}else {
				b[i].setColor(Color.WHITE);
			}
		}
	}


	private void calcularTeclas(float delta) {
		
		tiempo += delta;
		
		derecha = (entradas.isIrDerD() || entradas.isIrDerRight());
		izquierda = (entradas.isIrIzqA() || entradas.isIrIzqLeft());
		
			
			if(derecha) {
				if(tiempo >= 0.2f) {	
					seleccion = (seleccion == cantMaxPersonajes-1)?0:seleccion+1;
					tiempo = 0;
				}
			}else if(izquierda) {
				if(tiempo >= 0.2f) {
					seleccion = (seleccion == 0)?cantMaxPersonajes-1:seleccion-1;
					tiempo = 0;
				}
			}
	}
	
	private void mostrarInformacion(int indice) {		//Metodo que muestra la informacion en base a la opcion seleccionada.
		txtInfo.setText(_textosinfo[indice]);
		nombrePj.setText(_nombresPj[indice]);
		
		informacion.clear();
        informacion.add(txtInfo).pad(20).expand().fill().top();
	}


	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	public void dispose() {
		stage.dispose();
		
	}

	
	
	
	
}
