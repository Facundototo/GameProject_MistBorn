package com.bakpun.mistborn.elementos;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.enums.UserData;

public class Moneda {	

	private final int _ancho = 6,_alto = 4;
	private Body body;
	private Fisica f;
	
	public Moneda() {
		//Aplicar textura.
		f = new Fisica();
	}
	
	public void crear(Vector2 posInicial,World mundo) {

		f.setBody(BodyType.DynamicBody,posInicial);
		f.createPolygon(_ancho/Box2dConfig.PPM, _alto/Box2dConfig.PPM);	
		f.setFixture(f.getPolygon(), 5, 1, 0);
		body = mundo.createBody(f.getBody());	
		body.createFixture(f.getFixture());
		body.setBullet(true);		//Identifico al body como bullet(bala),esto porque Box2D hace chequeos mas rigurosos con los bodies que tienen mucha velocidad.
		body.setUserData(UserData.MONEDA);
	}
	
	public Body getBody() {
		return this.body;
	}
	
	
}
