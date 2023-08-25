package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.elementos.Texto;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

//No se la forma de anadir el sonido cuando se selecciona opcion con el mouse.No me anda tampoco el mouse bien con las colisiones cuando cambio la resolucion.

public class PantallaMenu implements Screen {
	private final String textos[] = {"Jugar","Opciones","Salir"},cadenasOpciones[] = {"Pantalla Completa","Ventana","Subir Volumen","Bajar Volumen"};
	private final float VELOCIDAD_CAMARA = 1.2f;
	private Texto[] opciones = new Texto[3],txtOpc = new Texto[4];
	private Texto numeroVolumen;
	private Imagen fondo;
	private ShapeRenderer figuraBarra,figuraOpcion,boxFs,boxWin/*,colision*/;
	private OrthographicCamera cam,camEstatica;
	private FillViewport vwEstatica,vwMov;
	private Sound sfxOpcion;
	private Entradas entradas;
	private float tiempoMapa = 150f, contMapa = 0f, opacidad = 1f,tiempo,delta;
	private int seleccion = 1,selecOpciones = 1;
	private boolean reiniciarCam = false, terminoPrimeraParte = false,estaSobreOpcion = false, mostrarMenuOpcion = false;
	
	public void show() {
		sfxOpcion = Gdx.audio.newSound(Gdx.files.internal(Recursos.SONIDO_OPCION_MENU));
		entradas = new Entradas();
		fondo = new Imagen(Recursos.FONDO_MENU);
		figuraBarra = new ShapeRenderer();
		figuraOpcion = new ShapeRenderer();
		boxFs = new ShapeRenderer();
		boxWin = new ShapeRenderer();
		//colision = new ShapeRenderer();
		cam = new OrthographicCamera();	//Camara para el fondo que se mueve.
		camEstatica = new OrthographicCamera();	//Camara para las opciones del menu (estaticas).
		vwEstatica = new FillViewport(Config.ANCHO,Config.ALTO,camEstatica);
		vwMov = new FillViewport(Config.ANCHO,Config.ALTO, cam);
		Gdx.input.setInputProcessor(entradas);
		cargarTextos();
	}

	public void render(float delta) {
		this.delta = delta;
		Render.limpiarPantalla((float) 212 / 255, (float) 183 / 255, (float) 117 / 255);	//limpiarPantalla() ahora le tenes que pasar rgb.Lo divido por 255 porque es del 0 al 1.
		
		renderizarFondoMovimiento();
		renderizarMenu();
		
		if(!mostrarMenuOpcion) {
			seleccion = chequearEntradas(delta,seleccion,1,3);		//Si toco Opciones se bloquean los controles para Jugar y Salir.
			colorearOpcion(opciones,seleccion);
			accionesMenu();
			seleccion = calcularColisionMouse(opciones,seleccion);
		}
		if(entradas.isEscape()) {
			mostrarMenuOpcion = false;		//Si toca Escape sale de la pestana de Opciones
		}	
	}
	
	private void mostrarOpciones() {		//Metodo que encapsula toda la pestana de Opciones
		dibujarFigura(figuraOpcion,ShapeType.Filled,Config.ANCHO/2-200, Config.ALTO/2-200, 400, 420,0,0,0,0.8f);
		Render.batch.begin();
		for (int i = 0; i < txtOpc.length; i++) {
			txtOpc[i].draw();	
		}
		numeroVolumen.draw();
		Render.batch.end();
		selecOpciones = chequearEntradas(delta,selecOpciones,1,4);
		colorearOpcion(txtOpc,selecOpciones);
		accionesMenuOpciones();
		selecOpciones = calcularColisionMouse(txtOpc,selecOpciones);
	}

	private void accionesMenuOpciones() {	//Funciones del apartado Opciones.
		if(entradas.isEnter() || entradas.isMouseClick()) { 
			if((selecOpciones==1 && entradas.isEnter()) || (selecOpciones == 1 && (entradas.isMouseClick() && estaSobreOpcion))) {
				if(!Config.isFullScreen()) {
					sfxOpcion.play();
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				}
			}else if((selecOpciones == 2 && entradas.isEnter()) || (selecOpciones == 2 && (entradas.isMouseClick() && estaSobreOpcion))){
				if(!Config.isWindowed()) {
					sfxOpcion.play();
					Gdx.graphics.setWindowedMode(Config.ANCHO, Config.ALTO);	
				}
			}else if((selecOpciones == 3 && entradas.isEnter()) || (selecOpciones == 3 && (entradas.isMouseClick() && estaSobreOpcion))) {
				if(Render.volumen < 1) {
					Render.subirVolumen(0.01f);	
					numeroVolumen.setTexto(String.valueOf((int)(Render.volumen/0.01f)));
				}
			}else if((selecOpciones == 4 && entradas.isEnter()) || (selecOpciones == 4 && (entradas.isMouseClick() && estaSobreOpcion))) {
				if(Render.volumen > 0.01f) {
					Render.bajarVolumen(0.01f);	
					numeroVolumen.setTexto(String.valueOf((int)(Render.volumen/0.01f)));
				}
			}
		}
		
		//Relleno los cuadrados de Pantalla Completo y/o Ventana.
		dibujarFigura(boxFs,(Config.isFullScreen())?ShapeType.Filled:ShapeType.Line, Config.ANCHO/2 + 110, txtOpc[0].getY()-20, 20, 20, (float) 212 / 255, (float) 183 / 255, (float) 117 / 255, 1);
		dibujarFigura(boxWin,(Config.isWindowed())?ShapeType.Filled:ShapeType.Line, Config.ANCHO/2 + 110, txtOpc[1].getY()-20, 20, 20, (float) 212 / 255, (float) 183 / 255, (float) 117 / 255, 1);
	}

	private void renderizarMenu() {
		camEstatica.update();
		Render.batch.setProjectionMatrix(camEstatica.combined);	//segundo bloque para hacer la camara estatica.
		dibujarFigura(figuraBarra,ShapeType.Filled,100, 200, (opciones[1].getAncho()+100), Config.ANCHO - 50,0,0,0,(!mostrarMenuOpcion)?0.7f:0.5f);	//Dibuja un rectangulo que vendria a ser donde estan las opciones.
		Render.batch.begin();
		for (int i = 0; i < opciones.length; i++) {
			opciones[i].draw();		//Dibuja las opciones.
		}
		Render.batch.end();	
		if(mostrarMenuOpcion) {
			mostrarOpciones();	
		}
		
	}
	
	private void cargarTextos() {
		for (int i = 0; i < opciones.length; i++) {
			opciones[i] = cargarTexto(opciones[i],50,Color.WHITE,textos[i],150,((i==0)?400:(i==1)?340:280),false);
		}
		for (int i = 0; i < txtOpc.length; i++) {
			Texto[] t = txtOpc;
			txtOpc[i] = cargarTexto(t[i],35,Color.WHITE,cadenasOpciones[i],(Config.ANCHO/2),((i==0)?Config.ALTO/1.6f:(i==1)?t[i-1].getY()-60:(i==2)?t[i-1].getY()-60:t[i-1].getY()-60),true);
		}
		numeroVolumen = cargarTexto(numeroVolumen,35,Color.ORANGE,"100",(Config.ANCHO/2),txtOpc[3].getY()-60,true);
	}

	private void renderizarFondoMovimiento() {
		calcularMovCamara();																
		if (reiniciarCam) {		// Esta va a reinciar la camara cuando haya superado el tiempo de muestra. Va a volver al punto de inicio.
			reiniciarCam = reinciarCamara();
		}
		cam.update();
		Render.batch.setProjectionMatrix(cam.combined);  //primer bloque para hacer la camara que se mueve.
		Render.batch.begin();
		fondo.draw();
		Render.batch.end();
	}

	
	private void accionesMenu() {	//Funciones del menu.
		if(entradas.isEnter() || entradas.isMouseClick()) { 
			if((seleccion==1 && entradas.isEnter()) || (seleccion == 1 && (entradas.isMouseClick() && estaSobreOpcion))) {
				sfxOpcion.play();
				Render.cancionMenu.pause();		//Pauso la cancion del menu.
				Render.app.setScreen(new PantallaPvP());
			}else if((seleccion == 2 && entradas.isEnter()) || (seleccion == 2 && (entradas.isMouseClick() && estaSobreOpcion))){
				mostrarMenuOpcion = true;
				sfxOpcion.play();
			}else if((seleccion == 3 && entradas.isEnter()) || (seleccion == 3 && (entradas.isMouseClick() && estaSobreOpcion))) {
				Gdx.app.exit();
			}
		}

	}

	private int calcularColisionMouse(Texto[] textos,int seleccion) {	//Metodo reutilizable para la colision del mouse con las palabras.
		/*colision.setProjectionMatrix(camEstatica.combined);
		colision.begin(ShapeType.Line);
		colision.setColor(Color.RED);
		for (int i = 0; i < textos.length; i++) {
			colision.rect(textos[i].getX(), textos[i].getY()-textos[i].getAlto(), textos[i].getAncho(), textos[i].getAlto());
		}
		colision.end();*/
		//Lo dejo comentado, porque son los rectangulos de las opciones (colisiones).
		
		int contSobreOpcion = 0;
		
		//Dentro de este for hay un if que es largo, sirve para calcular cuando el mouse esta sobre el rectangulo (osea la opcion).
		// Se podria usar me parece el metodo overlaps con los rectangulos de lo comentado arriba, pero la verdad que no indaque.
		for (int i = 0; i < textos.length; i++) {
			if((entradas.getMouseX() >= textos[i].getX() && entradas.getMouseX() <= (textos[i].getX()+ textos[i].getAncho())) && 
					(entradas.getMouseY() >= (textos[i].getY() - textos[i].getAlto()) && entradas.getMouseY() <= textos[i].getY())) {
				seleccion = (i+1);
				contSobreOpcion++;
			}
			if(contSobreOpcion > 0) {
				estaSobreOpcion = true;
			}else {
				estaSobreOpcion = false;
			}
		}
		return seleccion;
	}

	//Metodo reutilizable para crear figuras.
	private void dibujarFigura(ShapeRenderer figura,ShapeType tipo,float x,float y,float width,float height,float r,float g,float b,float a) {	
		Gdx.gl.glEnable(GL30.GL_BLEND);		//Esto para que funcione el canal alpha de figuraMenu.setColor();
	    Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		figura.setProjectionMatrix(camEstatica.combined);	 // Viewport, esto dentro del Render de la camEstatica.
		figura.begin(tipo);
		figura.rect(x, y, width, height);	//No me anda poner alto, tendria que hacer una clase config,															
		figura.setColor(r,g,b,a);												//que me de el ancho,alto.
		figura.end();
	}

	private void colorearOpcion(Texto[] textos,int seleccion) {	//Metodo reutilizable.
		for (int i = 0; i < textos.length; i++) {		//Este metodo hace que la opcion elegida se pinte de x color, diferenciandose de los demas.
			if (i == (seleccion-1)) {					
				textos[i].setColor(Color.RED);
			} else {
				textos[i].setColor(Color.WHITE);
			}
		}
	}

	private int chequearEntradas(float delta,int seleccion,int minOpc,int maxOpc) {	//Metodo reutilizable para la seleccion.
		tiempo += delta;
		if(entradas.isAbajo()) {
			if(tiempo >= 0.2f) {	//Hay un delay para elegir otra opcion.
				sfxOpcion.play();
				tiempo = 0;
				seleccion++;	//Si en este contador se supera el MAX_OPC, la seleccion va a ser igual a la primera opcion.
				if(seleccion > maxOpc) {
					seleccion = minOpc;
				}
			}
			return seleccion;
		}
		else if(entradas.isArriba()) {
			if(tiempo >= 0.2f) {
				sfxOpcion.play();
				tiempo = 0;
				seleccion--;	//Si en este contador es menor que el MIX_OPC, la seleccion va a ser igual a la ultima opcion.
				if(seleccion < minOpc) {
					seleccion = maxOpc;
				}
			}
		}
		return seleccion;
	}

	private Texto cargarTexto(Texto texto,int tamano,Color color,String cadena,float x,float y,boolean centrar) {	//Metodo reutilizable para cargar los textos.
		texto = new Texto(Recursos.FUENTE_MENU,tamano,color);		
		texto.setTexto(cadena);
		texto.setPosicion((!centrar)?x:x-(texto.getAncho()/2),y);	//Centrar para el menu/opciones.
		return texto;	//Retorno el texto, aunque no deberia porque es de referencia, pero me sale error.
	}

	private void calcularMovCamara() { //Este metodo va a hacer que la camara se mueva constantemente y va a calcular cuando hay que reiniciar la camara.
		moverCamara();
		if (tiempoMapa <= contMapa) {
			reiniciarCam = true;
			contMapa = 0f;
		}
		contMapa += 0.1f;
	}

	private boolean reinciarCamara() {		//Este metodo hace el fade para que no se vea el cambio de posicion de la camara.
		fondo.setTransparencia(opacidad);
		if (!terminoPrimeraParte) {
			if (opacidad < 0f) {
				terminoPrimeraParte = true;
				// cam.viewportWidth es lo que estaba buscando. Sino me andaba, tenia que
				// ponerlo por mi cuenta.
				cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
			} else {
				opacidad -= 0.007f;
			}									
		}
		if (terminoPrimeraParte) {
			opacidad += 0.007f;
		}
		if (terminoPrimeraParte && opacidad >= 1f) {
			terminoPrimeraParte = false;
			return false;
		}
		return true;
	}

	private void moverCamara() {
		cam.translate(VELOCIDAD_CAMARA, VELOCIDAD_CAMARA);	//Esto mueve la camara en base a una velocidad introducida x,y.
	}

	public void resize(int width, int height) {
		vwMov.update(width, height);
		vwEstatica.update(width, height);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
	    cam.update();
	    camEstatica.position.set(camEstatica.viewportWidth / 2, camEstatica.viewportHeight / 2, 0);
	    camEstatica.update();
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
		for (int i = 0; i < opciones.length; i++) {
			opciones[i].dispose();	//BitMapFont
		}
		for (int i = 0; i < cadenasOpciones.length; i++) {
			txtOpc[i].dispose();	//BitMapFont
		}
		numeroVolumen.dispose();	//BitMapFont
		boxWin.dispose();	//Shape
		boxFs.dispose();	//Shape
		figuraBarra.dispose();		//Shape
		figuraOpcion.dispose();		//Shape
		fondo.getTexture().dispose(); //Texture
		sfxOpcion.dispose();			//Sound
		Render.cancionMenu.dispose();	//Music
		Render.batch.dispose();		//SpriteBatch
		this.dispose();
	}
}
