package com.bakpun.mistborn.elementos;



import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.utiles.Recursos;

public class Moneda {	

	private final int _ancho = 6,_alto = 4;
	private Body body;
	private Fisica f;
	private Imagen moneda;

	public Moneda() {
		moneda = new Imagen(Recursos.MONEDA);
		moneda.setEscalaBox2D(16);
		f = new Fisica();
	}
	
	public void crear(Vector2 posInicial,World mundo) {
		
		f.setBody(BodyType.DynamicBody,posInicial);
		f.createPolygon(_ancho/Box2dConfig.PPM, _alto/Box2dConfig.PPM);	
		f.setFixture(f.getPolygon(), 5, 1, 0);
		body = mundo.createBody(f.getBody());	
		body.createFixture(f.getFixture());
		body.setBullet(true);		//Identificamos al body como bullet(bala),esto porque Box2D hace chequeos mas rigurosos con los bodies que tienen mucha velocidad.
		body.setUserData(UserData.MONEDA);
	}
	
	public void draw() {
		moneda.setPosicion(body.getPosition().x, body.getPosition().y);
		moneda.draw();
	}
	
	public Body getBody() {
		return this.body;
	}
	
	
}
