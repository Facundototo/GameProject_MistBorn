package com.bakpun.mistborn.io;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bakpun.mistborn.enums.UserData;

//Clase para detectar las colisiones y que hacer en base a eso.

public class Colision implements ContactListener{

	private boolean puedeSaltar;
	
	public void beginContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody(); 	//Me da los bodies que chocaron(contact).
		Body bodyB = contact.getFixtureB().getBody();
		
		if(bodyA.getUserData() == UserData.PJ && bodyB.getUserData() == UserData.SALTO_P) {		//Con esos bodies, me fijo si son los que quiero que choquen.
			puedeSaltar = true;		
		}else if(bodyB.getUserData() == UserData.PJ && bodyA.getUserData() == UserData.SALTO_P) {//En este caso es cuando puede volver a saltar el pj.
			puedeSaltar = true; 
		}
	}
	//Si el personaje esta tocando el suelo o una plataforma, entonces puedeSaltar = true, sino = false.
	public void endContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody(); 
		Body bodyB = contact.getFixtureB().getBody();
		
		if(bodyA.getUserData() == UserData.PJ && bodyB.getUserData() == UserData.SALTO_P) {
			puedeSaltar = false;
		}else if(bodyB.getUserData() == UserData.PJ && bodyA.getUserData() == UserData.SALTO_P) {
			puedeSaltar = false;
		}
	}
	public boolean isPuedeSaltar() {
		return this.puedeSaltar;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
}
