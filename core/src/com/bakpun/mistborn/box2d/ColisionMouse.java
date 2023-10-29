package com.bakpun.mistborn.box2d;

import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.enums.UserData;
import com.bakpun.mistborn.utiles.Recursos;


public class ColisionMouse{

	private World mundo;
	private Vector2 colision,normal;
	private RayCastCallback callback;
	private Imagen cursor;
	private boolean colisionando = false;
	
	
	public ColisionMouse(World mundo,OrthographicCamera cam) {
		this.mundo = mundo;
		normal = new Vector2();
		colision = new Vector2();
		cursor = new Imagen(Recursos.CABEZA_HAM);
		cursor.setEscalaBox2D(32);
	}
	
	public void dibujar(Vector2 posPj,Vector2 posMouse) {
		
		//Esta linea de codigo porque si no esta colisionando con ningun body el evento no lo detecta, entonces se settea siempre false hasta que el evento se active.
		colisionando = false;		
		
		callback = new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(fixture.getBody().getUserData() == UserData.METAL) {
					colision.set(point);
					ColisionMouse.this.normal.set(normal).add(point);
					colisionando = true;
					cursor.setPosicion(colision.x, colision.y);
					cursor.draw();
				}else {
					colisionando = false;
				}
				return -1;
			}
		};
		
		mundo.rayCast(callback, posPj, posMouse);
		
	}
	
	public boolean isColision() {
		if(colisionando) {
			return true;
		}else {
			return false;
		}
	}
	
	public Vector2 getPuntoColision() {
		return this.colision;
	}
}
