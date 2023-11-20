package com.bakpun.mistborn.objetos;

import com.badlogic.gdx.math.Vector2;
import com.bakpun.mistborn.elementos.Animacion;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.utiles.Recursos;

public class Plataforma {

	private Imagen spr;
	private Animacion anima;
	
	
	public Plataforma(boolean esChica,Vector2 posXY) {
		spr = new Imagen((esChica)?Recursos.PLATAFORMA_CHICA:Recursos.PLATAFORMA_GRANDE);	//Cargo el Sprite con la textura chica o grande,depende.
		spr.setEscalaBox2D(16);
		spr.setPosicion(posXY.x, posXY.y);
		//Creo animacion.
		anima = new Animacion();
		anima.create((esChica)?Recursos.ANIMACION_PLATAFORMA_CHICA:Recursos.ANIMACION_PLATAFORMA_GRANDE, 3, 1, 0.6f);
	}
	
	public void draw(float delta) {
		//Update animacion.
		anima.update(delta);
		//dibujo sprite con la animacion que sigue.
		spr.draw(anima.getCurrentFrame());
	}
	
	public void dispose() {
		spr.getTexture().dispose();
	}
	
	
}
