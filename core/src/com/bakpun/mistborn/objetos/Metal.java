package com.bakpun.mistborn.objetos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.utiles.Recursos;

public class Metal {
	
	private Fisica f;
	private Body body;
	private Imagen spr;
	
	public Metal(World mundo,Vector2 pos,int angulo) {
		spr = new Imagen(Recursos.METAL);
		
		spr.setTamano(10/Box2dConfig.PPM,200/Box2dConfig.PPM);
		spr.setPosicion(pos.x, pos.y);
		spr.setAngulo(angulo);
		spr.setColor(Color.GRAY);
		
		//Creo el body.
		f = new Fisica();									
		f.setBody(BodyType.StaticBody, pos,angulo);
		f.createPolygon(5/Box2dConfig.PPM,100/Box2dConfig.PPM);
		f.setFixture(f.getPolygon(), 4, 0.5f, 0);
		
		body = mundo.createBody(f.getBody());
		body.createFixture(f.getFixture());
		body.setUserData(UserData.METAL);
		
	}
	
	public void draw() {
		spr.draw();
	}
	
	public void dispose() {
		spr.getTexture().dispose();
		f.dispose();
	}
	
	
	
}
