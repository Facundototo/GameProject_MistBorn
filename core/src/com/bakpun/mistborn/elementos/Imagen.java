package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bakpun.mistborn.utiles.Render;

public class Imagen {

	private Texture t;
	private Sprite s;
	private float tamano;
	
	//Tengo que arreglar el flip de nuevo :( por la implementacion de box2d.
	
	public Imagen(String ruta) {
		t = new Texture(ruta);
		s = new Sprite(t);
	}
	
	public void draw() {	//Lo dejo para que dibuje otras imagenes que no necesiten animacion.
		s.draw(Render.batch);
	}
	
	public void draw(TextureRegion frameActual) {		//Medio que la sobrecargue mucho pero creo que no queda otra.
		s.setRegion(frameActual);
		s.draw(Render.batch);	
	}
	
	public void draw(TextureRegion frameActual,boolean flip) { //Dibuja el Sprite con la animacion y flipea si es true.
		s.setScale((flip)?-1:1, 1);				//resto el tamano para quede invertida la textura.
		s.setRegion(frameActual);
		s.draw(Render.batch);	
	}
	
	public void ajustarTamano(float tamano) {	//Tamano.
		s.scale(tamano);
	}
	
	public void escalarImagen(float ppm) {
		this.tamano = s.getWidth()/ppm;					//Escala el Sprite en base a los ppm (pixels per meter).
		s.setSize(s.getWidth()/ppm, s.getHeight()/ppm);
	}
	
	public void setTransparencia(float alpha) {	//Opacidad.
		s.setAlpha(alpha);
	}
	
	public void setPosicion(float x,float y) {	//Posicion x,y.
		s.setPosition(x, y);
	}
	public Texture getTexture() {
		return this.t;
	}
	
}
