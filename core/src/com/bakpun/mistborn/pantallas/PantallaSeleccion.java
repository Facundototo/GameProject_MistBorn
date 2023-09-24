package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.enums.InfoPersonaje;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public class PantallaSeleccion implements Screen{
	private int seleccion = 0,cantMaxPersonajes = 4;
	private InputMultiplexer im;
	private Entradas entradas;
	private Stage stage;
	private Table tabla,botones,imagenPj,infoPj;
	private Skin skin = new Skin(Gdx.files.internal(Recursos.SKIN));
	private Image pj;
	private ImageButton botonesPj[] = new ImageButton[cantMaxPersonajes];
	private Window informacion;
	private Label txtInfo,nombrePj,avisoSeleccion;
	private InfoPersonaje pjSeleccionado;
	
	private boolean derecha,izquierda;
	private float tiempo = 0;
	
	//Terminar esta PantallaSeleccion (no estan ordenados por prioridad):
	//  - Hacer el enum para mostrar la informacion, aca tendria que ver lo de la traduccion. HECHO
	// - Tambien tendriamos que informarnos mas sobre el Table, porque no lo estamos utilizando del todo. HECHO
	//  - Hacer que los botones pasen a ser ImageButton. HECHO  
	//  - Crear un Label que diga TOQUE ENTER PARA ACEPTAR ELECCION, se mande la PantallaPvP. HECHO
	
	//  - Cambiar el tamano de las letras y el estilo.
	//  - Hacer los disenos de esta pantalla (fondo,fondoPj,Window de la informacionPj).
	//  - Reflection para la creacion de la instancia Personaje.
	// 	- Poner la cabeza Ham en el boton, y cambiar tamb la Imagen.

	
	public PantallaSeleccion() {
		stage = new Stage(new FillViewport(Config.ANCHO,Config.ALTO));
		tabla = new Table();
		botones = new Table();
		imagenPj = new Table();
		infoPj = new Table();
		entradas = new Entradas();
		im = new InputMultiplexer();
		txtInfo = new Label("",skin);
		nombrePj = new Label("",skin);
		avisoSeleccion = new Label(Render.bundle.get("seleccion.avisoenter"),skin);
		informacion = new Window(Render.bundle.get("seleccion.txtinfo"),skin);
		pj = new Image(new Texture(Recursos.PERSONAJE_VIN));
	}
	
	public void show() {
		im.addProcessor(entradas);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		
		tabla.setFillParent(true);		//La tabla ocupa toda la pantalla.
		botones.setFillParent(false);
		imagenPj.setFillParent(false);
		infoPj.setFillParent(false);
		
		tabla.columnDefaults(0).width(Config.ANCHO / 3f).height(Config.ALTO);
		tabla.columnDefaults(1).width(Config.ANCHO / 3f);
		tabla.columnDefaults(2).width(Config.ANCHO / 3f).height(Config.ALTO);
		
		crearTablaInformacion();
		crearTablaImagen();
		crearTablaBotones();
		
		tabla.add(imagenPj);
		tabla.add(botones).bottom().padBottom(30);
		tabla.add(infoPj);
		
		avisoSeleccion.setPosition(Config.ANCHO/2, Config.ALTO/1.2f,Align.center);
		stage.addActor(avisoSeleccion);
		stage.addActor(tabla);
		
	}
	private void crearTablaImagen() {
		imagenPj.add(pj).size(Config.ANCHO/4, Config.ALTO/3);
		imagenPj.row();
		imagenPj.add(nombrePj).padTop(20);
	}


	private void crearTablaInformacion() {
		txtInfo.setWrap(true);
		txtInfo.setAlignment(Align.top);
		infoPj.add(informacion).size(Config.ANCHO/4,Config.ALTO/2);
	}


	public void render(float delta) {
		Render.limpiarPantalla(0, 0, 0);
		
		calcularTeclas(delta);		//Metodo para el uso del teclado (entradas).
		marcarOpcionSeleccionada();	//Aplicar efectos para la opcion seleccionada.
		
		if(entradas.isEnter()) {
			Render.app.setScreen(new PantallaPvP());
		}
		
		stage.act(delta);
		stage.draw();
		
	}
	
	private void crearTablaBotones() {
		for (int i = 0; i < botonesPj.length; i++) {
			
			TextureRegionDrawable trd = new TextureRegionDrawable(new Texture(InfoPersonaje.values()[i].getRutaIcono()));
			ImageButtonStyle ibs = new ImageButtonStyle(skin.get(ButtonStyle.class));
			ibs.imageUp = trd;
			
			botonesPj[i] = new ImageButton(ibs);
			
			final int opc = i;	//Esto lo hago porque "i" no puede ser final.
			
			botonesPj[i].addListener(new ClickListener() {		//Evento que escucha cuando un boton es clickeado (solo mouse).
				 @Override
		         public void clicked(InputEvent event, float x, float y) {
		             seleccion = opc;
		             mostrarInformacion(seleccion);
				 } 
			});
			botones.add(botonesPj[i]).pad(10);
		}
		
	}
	
	private void marcarOpcionSeleccionada() {
		for (int i = 0; i < botonesPj.length; i++) {
			if(i == seleccion) {
				botonesPj[seleccion].setColor(Color.PURPLE);
				mostrarInformacion(seleccion);
			}else {
				botonesPj[i].setColor(Color.WHITE);
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
		pjSeleccionado = InfoPersonaje.values()[indice];
		
		txtInfo.setText(pjSeleccionado.getInfo());		//Agarro la informacion del pjSeleccionado,retorna la key del locale.
		nombrePj.setText(pjSeleccionado.getNombre());	//Aca tambien.
		
		informacion.clear();							//Limpio la informacion anterior.
        informacion.add(txtInfo).pad(20).expand().fill().top();		//Anado la otra info.
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
