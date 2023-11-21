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
import com.bakpun.mistborn.elementos.SkinFreeTypeLoader;
import com.bakpun.mistborn.enums.Fuente;
import com.bakpun.mistborn.enums.InfoPersonaje;
import com.bakpun.mistborn.enums.TipoAudio;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Red;
import com.bakpun.mistborn.utiles.Render;

public final class PantallaSeleccion implements Screen{
	
	private int seleccion = 0,cantMaxPersonajes = 4;
	private boolean derecha,izquierda,opcionElegida = false;
	private float tiempo = 0;
	
	private InputMultiplexer im;
	private Entradas entradas;
	private Stage stage;
	private Table tabla,botones,imagenPj,infoPj;
	private Skin skin;
	private Image[] imgPj = new Image[cantMaxPersonajes];
	private Image fondo;
	private ImageButton botonesPj[] = new ImageButton[cantMaxPersonajes];
	private Window informacion;
	private Label txtInfo,nombrePj,avisoSeleccion;
	private InfoPersonaje pjSeleccionado;

	
	public PantallaSeleccion() {
		Render.audio.cancionSeleccion.play();
		Render.audio.cancionSeleccion.setLooping(true);
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
		avisoSeleccion = new Label(Render.bundle.get("seleccion.aviso"),Fuente.PIXELTEXTO.getStyle(skin));
		informacion = new Window(Render.bundle.get("seleccion.txtinfo"),skin);
		fondo = new Image(new Texture(Recursos.FONDO_SELECCION));
		fondo.setSize(Config.ANCHO, Config.ALTO);
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
		
		stage.addActor(fondo);
		stage.addActor(tabla);	//Anadir la contenedora al stage.
		configAvisoSeleccion();
	}

	public void render(float delta) {
		Render.limpiarPantalla((float) 24.3f/255,(float) 24.3f/255,(float) 24.3f/255);
		
		marcarOpcionSeleccionada();	//Aplicar efectos para la opcion seleccionada.
		
		if(!opcionElegida) {	//Se bloquean las teclas.
			avisoSeleccion.setText(Render.bundle.get("seleccion.aviso"));
			calcularTeclas(delta);		//Metodo para el uso del teclado (entradas).
			if(entradas.isEnter()) {
				opcionElegida = true;
				Render.audio.seleccionElegida.play(Render.audio.getVolumen(TipoAudio.SONIDO));
				avisoSeleccion.setText(Render.bundle.get("seleccion.avisoenter"));
				Red.listoSeleccion(true);
			}
		}else {
			if(Red.isEmpiezaPartida()) {
				avisoSeleccion.setVisible(false);
				cambiarPantallaFadeOut();
			}
			if(entradas.isBotonDer()) {
				opcionElegida = false;
				Red.listoSeleccion(false);
			}
		}
		
		/*Este es para el cliente que toca ESC,este llama a desconectar() lo que hace que se limpie el server 
		y ponga en el if de abajo que el estado es DESCONECTADO para el segundo cliente, es una especie de polimorfismo*/
		if(entradas.isEscape()) {	
			Render.audio.cancionSeleccion.stop();
			Render.app.setScreen(new PantallaMenu());
			Red.desconectar();
		}
		
		//Para forzar al cliente que no se desconecto mediante el ESC o ALT F4, a que se vaya. Y tambien por si el server se cierra inesperadamente.
		Red.chequearEstado();
		
		stage.act(delta);
		stage.draw();
		
	}
	
	private void cambiarPantallaFadeOut() {  	
		final int _seleccionElegida = seleccion;		
		stage.addAction(Actions.sequence(Actions.fadeOut(1.5f),
			Actions.run(new Runnable() {   
			  @Override
	            public void run() {	//Se hace el fadeOut y cuando termine se cambia la pantalla con el .run
					if(Red.getId() == 0) {
						Render.app.setScreen(new PantallaPvP(InfoPersonaje.values()[_seleccionElegida].getNombre(), InfoPersonaje.values()[Red.getSeleccionOponente()].getNombre(),2));
					}else {
						Render.app.setScreen(new PantallaPvP(InfoPersonaje.values()[Red.getSeleccionOponente()].getNombre(), InfoPersonaje.values()[_seleccionElegida].getNombre(),1));
					}
					Render.audio.cancionSeleccion.stop();
					
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
		informacion.getTitleLabel().setAlignment(Align.center);
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
					 if(!opcionElegida) {
			             seleccion = opc;
			             mostrarInformacion(seleccion);
			             Render.audio.sonidoSeleccion.play(Render.audio.getVolumen(TipoAudio.SONIDO));
			             Red.enviarSeleccion(seleccion);
					 }
				 } 
			});
			botones.add(botonesPj[i]).pad(10);
		}
		
	}
	
	private void marcarOpcionSeleccionada() {
		
		if(Red.getSeleccionOponente() != seleccion) {
			botonesPj[Red.getSeleccionOponente()].setColor(Color.RED); 	//Se colorea la seleccion mia y del oponente.
			botonesPj[seleccion].setColor(Color.BLUE);
		}else {		//Si los 2 son iguales se pone violeta.
			botonesPj[seleccion].setColor(Color.PURPLE);
		}
		
		mostrarInformacion(seleccion);
		
		for (int i = 0; i < botonesPj.length; i++) {
			if (i != seleccion && i != Red.getSeleccionOponente()) {
				botonesPj[i].setColor(Color.WHITE);
			}
		}
		if(opcionElegida) {
			botonesPj[seleccion].setColor(Color.GOLD);
		}
	}


	private void calcularTeclas(float delta) {
		
		tiempo += delta;
		
		derecha = (entradas.isIrDerD() || entradas.isIrDerRight());
		izquierda = (entradas.isIrIzqA() || entradas.isIrIzqLeft());
		
		if(derecha) {
			if(tiempo >= 0.2f) {	
				Render.audio.sonidoSeleccion.play(Render.audio.getVolumen(TipoAudio.SONIDO));
				seleccion = (seleccion == cantMaxPersonajes-1)?0:seleccion+1;
				tiempo = 0;
				Red.enviarSeleccion(seleccion);
			}
		}else if(izquierda) {
			if(tiempo >= 0.2f) {
				Render.audio.sonidoSeleccion.play(Render.audio.getVolumen(TipoAudio.SONIDO));
				seleccion = (seleccion == 0)?cantMaxPersonajes-1:seleccion-1;
				tiempo = 0;
				Red.enviarSeleccion(seleccion);
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
		Render.audio.sonidoSeleccion.dispose();
	}

	
	
	
	
}
