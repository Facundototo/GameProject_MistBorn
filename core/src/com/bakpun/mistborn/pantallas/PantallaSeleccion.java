package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
import com.bakpun.mistborn.elementos.Audio;
import com.bakpun.mistborn.elementos.SkinFreeTypeLoader;
import com.bakpun.mistborn.enums.Fuente;
import com.bakpun.mistborn.enums.InfoPersonaje;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Render;

public final class PantallaSeleccion implements Screen{
	private int seleccion = 0,cantMaxPersonajes = 4;
	private InputMultiplexer im;
	private Entradas entradas;
	private Stage stage;
	private Table tabla,botones,imagenPj,infoPj;
	private Skin skin;
	private Image imgPj[] = new Image[cantMaxPersonajes];
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
	// 	- Poner la cabeza Ham en el boton, y cambiar tamb la Imagen. HECHO
	//  - Cambiar el tamano de las letras y el estilo.	HECHO
	
	//  - Hacer los disenos de esta pantalla (fondo,fondoPj,Window de la informacionPj).
	//  - Reflection para la creacion de la instancia Personaje.
	

	public PantallaSeleccion() {
		skin = SkinFreeTypeLoader.cargar();		//Cargo el codigo que hay que copiar para usar FreeTypeFont en Scene2DUI.
		stage = new Stage(new FillViewport(Config.ANCHO,Config.ALTO));
		tabla = new Table();
		botones = new Table();
		imagenPj = new Table();
		infoPj = new Table();
		entradas = new Entradas();
		im = new InputMultiplexer();
		txtInfo = new Label("",Fuente.PIXELINFO.getStyle(skin));
		nombrePj = new Label("",Fuente.PIXELTEXTO.getStyle(skin));
		avisoSeleccion = new Label(Render.bundle.get("seleccion.avisoenter"),Fuente.PIXELTEXTO.getStyle(skin));
		informacion = new Window(Render.bundle.get("seleccion.txtinfo"),skin);
		for (int i = 0; i < imgPj.length; i++) {
			imgPj[i] = new Image(new Texture(InfoPersonaje.values()[i].getRutaTextura()));
		}
	}

	public void show() {
		im.addProcessor(entradas);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		
		//Decimos si las tablas ocupan (true) o no (false) toda la pantalla.
		tabla.setFillParent(true);		
		botones.setFillParent(false);
		imagenPj.setFillParent(false);
		infoPj.setFillParent(false);
		
		//Aca se establece 3 columnas con un cierto ancho y alto.
		tabla.columnDefaults(0).width(Config.ANCHO / 3f).height(Config.ALTO);
		tabla.columnDefaults(1).width(Config.ANCHO / 3f);
		tabla.columnDefaults(2).width(Config.ANCHO / 3f).height(Config.ALTO);
		
		//Creacion de tablas.
		crearTablaInformacion();
		crearTablaBotones();
		
		//Anadirlas a la contenedora.
		tabla.add(imagenPj);
		tabla.add(botones).bottom().padBottom(30);
		tabla.add(infoPj);
		
		configAvisoSeleccion();
		
		stage.addActor(tabla);	//Anadir la contenedora al stage.
	}

	public void render(float delta) {
		Render.limpiarPantalla((float) 101/255,(float) 90/255,(float) 105/255);
		
		calcularTeclas(delta);		//Metodo para el uso del teclado (entradas).
		marcarOpcionSeleccionada();	//Aplicar efectos para la opcion seleccionada.
		
		if(entradas.isEnter()) {
			cambiarPantallaFadeOut();
		}
		stage.act(delta);
		stage.draw();
		
	}
	
	private void cambiarPantallaFadeOut() {
		stage.addAction(Actions.sequence(Actions.fadeOut(2),Actions.run(new Runnable() {   
			  @Override
	            public void run() {					//Se hace el fadeOut y cuando termine se cambia la pantalla con el .run
					Audio.cancionMenu.stop();
	                Render.app.setScreen(new PantallaPvP());
	            }})));
	}

	private void configAvisoSeleccion() {
		avisoSeleccion.setPosition(Config.ANCHO/2, Config.ALTO/1.2f,Align.center);		//Le asigno la posicion.
		avisoSeleccion.addAction(Actions.forever(Actions.sequence(						
			    Actions.fadeIn(0.5f),Actions.fadeOut(0.5f),				//Aca se hace la secuencia del fade, aplicandole un delay para que funcione.
			    Actions.delay(0.2f))));
		
		stage.addActor(avisoSeleccion);			//Lo anado al stage por separado de la contenedora.
	}
	
	private void crearTablaInformacion() {
		txtInfo.setWrap(true);
		txtInfo.setAlignment(Align.top);
		infoPj.add(informacion).size(Config.ANCHO/4,Config.ALTO/2);
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
		
		//Limpio la informacion anterior.
		imagenPj.clear();
		informacion.clear();
		
		txtInfo.setText(pjSeleccionado.getInfo());		//Agarro la informacion del pjSeleccionado,retorna la key del locale.
		nombrePj.setText(pjSeleccionado.getNombre());   //Aca tambien.
		
		//Anado la otra info.
		imagenPj.add(imgPj[indice]).size(Config.ANCHO/4, Config.ALTO/3).row();
		imagenPj.add(nombrePj).padTop(30);				//Aca creo la tabla imagenPj porque no se cambia luego la imagen, como pasa con el texto.
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
