package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bakpun.mistborn.utiles.Render;

public class Imagen {

	private Texture t;
	private Sprite s;
	private float tamano;
	
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
		s.setScale((flip)?-tamano-1:tamano+1, tamano+1);		//resto el tamano para quede invertida la textura.
		s.setRegion(frameActual);
		s.draw(Render.batch);	
	}
	
	public void ajustarTamano(float tamano) {	//Tamano.
		this.tamano = tamano;
		s.scale(tamano);
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
