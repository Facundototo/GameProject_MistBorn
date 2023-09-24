package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.bakpun.mistborn.elementos.Audio;
import com.bakpun.mistborn.elementos.Texto;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public final class VentanaOpciones {
	private Texto[] txtOpc = new Texto[4];
	private String[] cadenasOpciones = {"Pantalla Completa","Ventana","Subir Volumen","Bajar Volumen"};;
	private ShapeRenderer figuraOpcion,boxFs,boxWin;
	private Texto numeroVolumen;
	private Entradas entradas;
	private OrthographicCamera cam;
	private boolean estaSobreOpcion;
	private int selecOpciones = 1;
	private float tiempo;
	
	//Deberia borrarla y poner todo de nuevo en menu, porque me da problemas ponerla en pantallaPvP().
	
	public VentanaOpciones(OrthographicCamera cam,Entradas entradas) {
		this.cam = cam;
		this.entradas = entradas;
		Gdx.input.setInputProcessor(this.entradas);
		figuraOpcion = new ShapeRenderer();
		boxFs = new ShapeRenderer();
		boxWin = new ShapeRenderer();
		for (int i = 0; i < txtOpc.length; i++) {			//Cargo los textos.
			Texto[] t = txtOpc;
			txtOpc[i] = cargarTexto(t[i],35,Color.WHITE,cadenasOpciones[i],(Config.ANCHO/2),((i==0)?Config.ALTO/1.6f:(i==1)?t[i-1].getY()-60:(i==2)?t[i-1].getY()-60:t[i-1].getY()-60),true);
		}
		numeroVolumen = cargarTexto(numeroVolumen,35,Color.ORANGE,"100",(Config.ANCHO/2),txtOpc[3].getY()-60,true);
	}
	
	private Texto cargarTexto(Texto texto,int tamano,Color color,String cadena,float x,float y,boolean centrar) {	
		texto = new Texto(Recursos.FUENTE_MENU,tamano,color);		
		texto.setTexto(cadena);
		texto.setPosicion((!centrar)?x:x-(texto.getAncho()/2),y);	
		return texto;	
	}
	
	public void mostrarOpciones(float delta) {		//Unico metodo que muestra la ventana.
		dibujarFigura(figuraOpcion,ShapeType.Filled,Config.ANCHO/2-200, Config.ALTO/2-200, 400, 420,0,0,0,0.8f);
		Render.batch.begin();
		for (int i = 0; i < txtOpc.length; i++) {
			txtOpc[i].draw();	
		}
		numeroVolumen.draw();
		Render.batch.end();
		selecOpciones = calcularColisionMouse(txtOpc,selecOpciones);
		selecOpciones = chequearEntradas(delta,selecOpciones,1,4);
		colorearOpcion(txtOpc,selecOpciones);
		accionesMenuOpciones();
	}
	
	private void dibujarFigura(ShapeRenderer figura,ShapeType tipo,float x,float y,float width,float height,float r,float g,float b,float a) {	
		Gdx.gl.glEnable(GL30.GL_BLEND);		
	    Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		figura.setProjectionMatrix(cam.combined);	
		figura.begin(tipo);
		figura.rect(x, y, width, height);																
		figura.setColor(r,g,b,a);												
		figura.end();
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
	
	private void accionesMenuOpciones() {	//Funciones de la ventana opciones.
		if(entradas.isEnter() || entradas.isMouseClick()) { 
			if((selecOpciones==1 && entradas.isEnter()) || (selecOpciones == 1 && (entradas.isMouseClick() && estaSobreOpcion))) {
				if(!Config.isFullScreen()) {
					Audio.sonidoMenu.play(Audio.volumen);
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				}
			}else if((selecOpciones == 2 && entradas.isEnter()) || (selecOpciones == 2 && (entradas.isMouseClick() && estaSobreOpcion))){
				if(!Config.isWindowed()) {
					Audio.sonidoMenu.play(Audio.volumen);
					Gdx.graphics.setWindowedMode(Config.ANCHO, Config.ALTO);	
				}
			}else if((selecOpciones == 3 && entradas.isEnter()) || (selecOpciones == 3 && (entradas.isMouseClick() && estaSobreOpcion))) {
				if(Audio.volumen < 1) {
					Audio.setVolumen(0.01f);
					numeroVolumen.setTexto(String.valueOf((int)(Audio.volumen/0.01f)));
				}
			}else if((selecOpciones == 4 && entradas.isEnter()) || (selecOpciones == 4 && (entradas.isMouseClick() && estaSobreOpcion))) {
				if(Audio.volumen > 0.01f) {
					Audio.setVolumen(-0.01f);
					numeroVolumen.setTexto(String.valueOf((int)(Audio.volumen/0.01f)));
				}
			}
		}
		
		//Relleno los cuadrados de Pantalla Completo y/o Ventana.
		dibujarFigura(boxFs,(Config.isFullScreen())?ShapeType.Filled:ShapeType.Line, Config.ANCHO/2 + 110, txtOpc[0].getY()-20, 20, 20, (float) 212 / 255, (float) 183 / 255, (float) 117 / 255, 1);
		dibujarFigura(boxWin,(Config.isWindowed())?ShapeType.Filled:ShapeType.Line, Config.ANCHO/2 + 110, txtOpc[1].getY()-20, 20, 20, (float) 212 / 255, (float) 183 / 255, (float) 117 / 255, 1);
	}
	private int chequearEntradas(float delta,int seleccion,int minOpc,int maxOpc) {	
		tiempo += delta;
		if(entradas.isAbajo()) {
			if(tiempo >= 0.2f) {
				Audio.sonidoMenu.play(Audio.volumen);
				tiempo = 0;
				seleccion++;	
				if(seleccion > maxOpc) {
					seleccion = minOpc;
				}
			}
			return seleccion;
		}
		else if(entradas.isArriba()) {
			if(tiempo >= 0.2f) {
				Audio.sonidoMenu.play(Audio.volumen);
				tiempo = 0;
				seleccion--;	
				if(seleccion < minOpc) {
					seleccion = maxOpc;
				}
			}
		}
		return seleccion;
	}
	
	private void colorearOpcion(Texto[] textos,int seleccion) {	
		for (int i = 0; i < textos.length; i++) {		
			if (i == (seleccion-1)) {					
				textos[i].setColor(Color.RED);
			} else {
				textos[i].setColor(Color.WHITE);
			}
		}
	}
	
	
}
