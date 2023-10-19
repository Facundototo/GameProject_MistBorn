package com.bakpun.mistborn.box2d;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bakpun.mistborn.enums.UserData;

//Clase para detectar las colisiones y que hacer en base a eso.

public class Colision implements ContactListener{

	private ArrayList<Body> pjs = new ArrayList<Body>();
	private ArrayList<Body> monedasInactivas = new ArrayList<Body>();	
	
	public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();     //Me da los bodies que chocaron(contact).
        Body bodyB = contact.getFixtureB().getBody();

        if(bodyA.getUserData() == UserData.PJ && bodyB.getUserData() == UserData.SALTO_P) {        //Con esos bodies, me fijo si son los que quiero que choquen.
        	pjs.add(bodyA);
        }else if(bodyB.getUserData() == UserData.PJ && bodyA.getUserData() == UserData.SALTO_P) {//En este caso es cuando puede volver a saltar el pj.
        	pjs.add(bodyB);  
        }
        
        if(bodyA.getUserData() == UserData.MONEDA && bodyB.getUserData() == UserData.SALTO_P) {
        	monedasInactivas.add(bodyA);
        }else if(bodyB.getUserData() == UserData.MONEDA && bodyA.getUserData() == UserData.SALTO_P) {
        	monedasInactivas.add(bodyB);
        }
        
    }
	
	public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody(); 
        Body bodyB = contact.getFixtureB().getBody();

        if(bodyA.getUserData() == UserData.PJ && bodyB.getUserData() == UserData.SALTO_P) {
            pjs.remove(bodyA);
        }else if(bodyB.getUserData() == UserData.PJ && bodyA.getUserData() == UserData.SALTO_P) {
            pjs.remove(bodyB);
        }
        
        if(bodyA.getUserData() == UserData.MONEDA && bodyB.getUserData() == UserData.SALTO_P) {
        	monedasInactivas.remove(bodyA);
        }else if(bodyB.getUserData() == UserData.MONEDA && bodyA.getUserData() == UserData.SALTO_P) {
        	monedasInactivas.remove(bodyB);
        }
    }
	//Si el personaje esta tocando el suelo o una plataforma, entonces isPuedeSaltar retorna true, sino, false.
	public boolean isPuedeSaltar(Body pj) {		//Retorna si en este momento, la lista tiene al pj, es decir que esta en el piso(true).
		return this.pjs.contains(pj);
	}
	
	public boolean isMonedaInactiva(Body moneda) {	//Retorna si en este momento,la lista tiene a una moneda inactiva.Osea que esta inmovil.
		return this.monedasInactivas.contains(moneda);
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
