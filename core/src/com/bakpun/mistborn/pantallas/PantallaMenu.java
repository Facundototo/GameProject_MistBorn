package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.elementos.SkinFreeTypeLoader;
import com.bakpun.mistborn.enums.Fuente;
import com.bakpun.mistborn.enums.TipoAudio;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public class PantallaMenu implements Screen{

	private Stage stage;
	private Skin skin;
	private Table tablaCont, tablaBotones,tablaOpciones;
	private Label botones[] = new Label[3];
	private Image fondo,barraNegra,fondoNegro;
	private SelectBox<String> modoPantalla;
	private Label textoMusica,textoSonido,numeroVolMusica,numeroVolSonido;
	private Slider volumenMusica,volumenSonido;
	private Entradas entradas;
	private InputMultiplexer im;
	private int seleccion = 0;
	private float tiempo = 0;  
	private boolean flagOpciones = false;
	
	//Falta cambiar el skin con skinComposer
	
	public PantallaMenu(){
		Render.audio.cancionMenu.play();
		Render.audio.cancionMenu.setLooping(true);
		
		fondo = new Image(new Texture(Recursos.FONDO_MENU));
		barraNegra = new Image(new Texture(Recursos.BARRA_NEGRA));
		stage = new Stage(new FillViewport(Config.ANCHO, Config.ANCHO));
		skin = SkinFreeTypeLoader.cargar();
		entradas = new Entradas();
		im = new InputMultiplexer();
		tablaBotones = new Table();
		tablaOpciones = new Table();
		tablaCont = new Table();
		botones[0] = new Label(Render.bundle.get("menu.jugar"), Fuente.PIXELMENU.getStyle(skin));
		botones[1] = new Label(Render.bundle.get("menu.opciones"), Fuente.PIXELMENU.getStyle(skin));
		botones[2] = new Label(Render.bundle.get("menu.salir"), Fuente.PIXELMENU.getStyle(skin));
		//Ventana Opciones
		fondoNegro = new Image(new Texture(Recursos.BARRA_NEGRA));
		modoPantalla = new SelectBox<String>(skin);
		textoMusica = new Label(Render.bundle.get("menuopciones.musica"),Fuente.PIXELINFO.getStyle(skin));
		textoSonido = new Label(Render.bundle.get("menuopciones.sonido"),Fuente.PIXELINFO.getStyle(skin));
		volumenMusica = new Slider(0,100,1,false,skin);
		volumenSonido = new Slider(0,100,1,false,skin);
		numeroVolMusica = new Label("",Fuente.PIXELINFO.getStyle(skin));
		numeroVolSonido = new Label("",Fuente.PIXELINFO.getStyle(skin));
	}
	
	@Override
	public void show() {
		im.addProcessor(entradas);
		im.addProcessor(stage);		//Usamos entradas porque stage por lo que sabemos no maneja input por teclado.
		Gdx.input.setInputProcessor(im);
		
		tablaBotones.setFillParent(false);
		tablaCont.setFillParent(true);			
		tablaOpciones.setFillParent(false);	
		
		
		for (int i = 0; i < botones.length; i++) {
			final int opc = i;
			tablaBotones.add(botones[i]).left().pad(10).row();
			botones[i].addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(!flagOpciones) {		//If para que no puedas seleccionar si esta en la ventanaOpciones
						Render.audio.sonidoSeleccion.play(Render.audio.getVolumen(TipoAudio.SONIDO));
						seleccion = opc;
						colorearSeleccion();
						tocarOpcion(true);
					}
				}
			});
		}
		
		addListeners();
		addTablaOpciones();
		
		modoPantalla.setItems(new String[] {Render.bundle.get("menuopciones.ventana"),Render.bundle.get("menuopciones.fullscreen") });
		modoPantalla.setWidth(100);
		
		fondo.addAction(Actions.forever(Actions.parallel(Actions.sequence(		//Esta accion es la que maneja todo el movimiento del fondo.
				Actions.moveTo(-1600, -1500, 40),
				Actions.fadeOut(1),
				Actions.moveTo(0, 0),
				Actions.fadeIn(1)
				))));
		
		barraNegra.setSize(Config.ANCHO/7, Config.ALTO);
		barraNegra.setPosition(Config.ANCHO/9.5f,Config.ALTO/1.5f);		//La barra negra es la de Opciones.
		barraNegra.getColor().a = 0.7f;
		
		fondoNegro.setSize(Config.ANCHO/6, Config.ALTO/3);
		fondoNegro.setPosition(Config.ANCHO/4.1f,Config.ALTO/1.7f);		
		fondoNegro.getColor().a = 0.7f;
		fondoNegro.setVisible(false);
		
		tablaCont.add(tablaBotones).expand().left().padLeft(Config.ANCHO/8);					//Anadimos las tablas a la contenedora.
	    tablaCont.add(tablaOpciones).expand().left().padRight(Config.ANCHO/1.8f).padTop(Config.ALTO/4);
	    
		stage.addActor(fondo);
		stage.addActor(barraNegra);		//Anadimos los actores al stage
		stage.addActor(fondoNegro);
		stage.addActor(tablaCont);
	
		stage.getRoot().getColor().a = 0f;		//El stage inicia con 0 alpha. Y despues hace un fadeIn.
		stage.addAction(Actions.fadeIn(2.5f));
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla((float) 212 / 255, (float) 183 / 255, (float) 117 / 255);
        
        tiempo += delta;
        
        if(!flagOpciones) {
        	asignarEntradas();
        	colorearSeleccion();
        	tocarOpcion(false);
        }
		if(entradas.isEscape()) {flagOpciones = false;}
		
		mostrarOpciones((flagOpciones)?true:false);
		
		stage.act();
		stage.draw();
		
	}

	private void addListeners() {
		volumenMusica.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				numeroVolMusica.setText(String.valueOf(volumenMusica.getValue()));	//Este evento detecta cuando el slider esta siendo cambiado de valor.
				Render.audio.setVolumen(volumenMusica.getValue(),TipoAudio.MUSICA);					//Entonces lo que hacemos es cambiar el valor del volumen.
			}
		});
		
		volumenSonido.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				numeroVolSonido.setText(String.valueOf(volumenSonido.getValue()));	
				Render.audio.setVolumen(volumenSonido.getValue(),TipoAudio.SONIDO);					
			}
		});
		
		volumenMusica.setValue(Render.audio.getVolumen(TipoAudio.MUSICA)*100);	//Ponemos el valor que tenia antes por si el usuario crea de nuevo la pantallaMenu().
		volumenSonido.setValue(Render.audio.getVolumen(TipoAudio.SONIDO)*100);
	
		modoPantalla.addListener(new ChangeListener() {		
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {			//Si hubo algun cambio en el SelectBox este evento lo detecta.
				if(modoPantalla.getSelected().equals(Render.bundle.get("menuopciones.fullscreen"))) {		//Si el string seleccionado es fullscreen.
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				}else {
					Gdx.graphics.setWindowedMode(Config.ANCHO/3, Config.ALTO/2);
				}
			}
		});
		
	}

	private void addTablaOpciones() {
		tablaOpciones.add(modoPantalla).center().padBottom(20).row();	//Anadimos los widgets de las Opciones.
		tablaOpciones.add(textoMusica).row();
		tablaOpciones.add(volumenMusica).center().row();
		tablaOpciones.add(numeroVolMusica).center().row();
		tablaOpciones.add(textoSonido).row();
		tablaOpciones.add(volumenSonido).center().row();
		tablaOpciones.add(numeroVolSonido).center();
		tablaOpciones.setVisible(false);		//Le ponemos que al principio sea invisible.
	}
	
	private void tocarOpcion(boolean click) {		//Booleano que es true en el evento clicked().
		if(entradas.isEnter() || click) {
			if(seleccion == 0) {Render.app.setScreen(new PantallaEspera()); Render.audio.cancionMenu.stop();}
			else if(seleccion == 1) {flagOpciones = true;}
			else {Gdx.app.exit();}
		}
	}

	private void colorearSeleccion() {
		botones[seleccion].setColor(Color.RED);
		for (int i = 0; i < botones.length; i++) {
			if(i != seleccion) {
				botones[i].setColor(Color.WHITE);
			}
		}		
	}

	private void asignarEntradas() {
		if(entradas.isAbajo()) {
	        if(tiempo >= 0.2f) {
	        	Render.audio.sonidoSeleccion.play(Render.audio.getVolumen(TipoAudio.SONIDO));
	        	if(seleccion == 2) {
	        		seleccion = 0;
	        	}else {
	        		seleccion++;
	        	}
	        	tiempo = 0;
	        }	
		}else if(entradas.isArriba()) {
			if(tiempo >= 0.2f) {
				Render.audio.sonidoSeleccion.play(Render.audio.getVolumen(TipoAudio.SONIDO));
				if(seleccion == 0) {
					seleccion = 2;
	    		}else {
	    			seleccion--;
	    		}
				tiempo = 0;
	    	}	
	     }
	}
	
	private void mostrarOpciones(boolean flag) {
		fondoNegro.setVisible(flag);
		tablaOpciones.setVisible(flag);
	}

	@Override
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

	@Override
	public void dispose() {
		stage.dispose();
	}
	
}
