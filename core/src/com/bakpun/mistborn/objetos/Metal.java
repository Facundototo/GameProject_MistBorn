package com.bakpun.mistborn.objetos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.bakpun.mistborn.elementos.Box2dConfig;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.utiles.Recursos;

public class Metal {
	
	private Imagen spr;
	
	public Metal(Vector2 pos,int angulo) {
		spr = new Imagen(Recursos.METAL);
		
		spr.setTamano(10/Box2dConfig.PPM,200/Box2dConfig.PPM);
		spr.setPosicion(pos.x, pos.y);
		spr.setAngulo(angulo);
		spr.setColor(Color.GRAY);
		
	}
	
	public void draw() {
		spr.draw();
	}
	
	public void dispose() {
		spr.getTexture().dispose();
	}
	
	
	
}
