package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animacion {
	private Animation<TextureRegion> animacion;
	private float tiempo = 0f, duracionTotal;
	
	
	public void create(String ruta,int division_ancho,int division_alto,float duracion) {
		
		TextureRegion[][] temp;		//Es una matriz porque la funcion split() te los da de esa manera.
		TextureRegion[] frames;		
		
		Texture texturaFrames = new Texture(ruta);
		
		temp = TextureRegion.split(texturaFrames , texturaFrames.getWidth()/division_ancho, texturaFrames.getHeight()/division_alto);
		frames = new TextureRegion[division_ancho*division_alto];
		
		int index = 0;
		
		for (int i = 0; i < division_alto; i++) {
			for (int j = 0; j < division_ancho; j++) {
				frames[index++] = temp[i][j];		//Meto los frames separados en un vector 1D.
			}		
		}
		
		animacion = new Animation<>(duracion, frames); 	//Creo la animacion.
		duracionTotal = frames.length * duracion;
	}

	public void update(float delta) {
		tiempo += delta;
		if (tiempo >= duracionTotal) {		//Si el tiempo es mayor a la duracion total que le asigne , el tiempo se restara por este.
			tiempo -= duracionTotal;
		}
	}
	
	public TextureRegion getCurrentFrame() {
		return animacion.getKeyFrame(tiempo);	//En base a x tiempo te devuelve que frame tendria que ir ahora. 
	}

}
