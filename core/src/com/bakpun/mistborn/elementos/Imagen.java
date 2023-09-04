package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bakpun.mistborn.utiles.Render;

public class Imagen {

	private Texture t;
	private Sprite s;
	private TextureRegion frameActual;
	
	public Imagen(String ruta) {
		t = new Texture(ruta);
		s = new Sprite(t);
	}
	
	public void draw() {	//Lo dejo para que dibuje otras imagenes que no necesiten animacion.
		s.draw(Render.batch);
	}
	
	public void draw(TextureRegion frameActual) {
		this.frameActual = frameActual;
		s.setRegion(frameActual);
		s.draw(Render.batch);
	}
	
	public void flip(boolean flip) {
		s.setScale((flip)?-1:1, 1);				//resto el tamano para quede invertida la textura.
		s.setRegion(this.frameActual);
		s.draw(Render.batch);	
	}
	
	public void ajustarTamano(float tamano) {	//Tamano.
		s.scale(tamano);
	}
	
	public void escalarImagen(float ppm) {			
		s.setSize(s.getWidth()/ppm, s.getHeight()/ppm);		//Escala el Sprite en base a los ppm (pixels per meter).
	}
	
	public void setTransparencia(float alpha) {	//Opacidad.
		s.setAlpha(alpha);
	}
	
	public void setPosicion(float x,float y) {	//Posicion x,y.
		s.setCenter(x,y);		//En vez de setPosition que te ponia las coor en el vertice inferior izquierdo, este te lo pone en el centro de la textura. 
		s.setOriginCenter();	//Lo tuve que adaptar por el flip.
	}
	public Texture getTexture() {
		return this.t;
	}
	
}
