package com.bakpun.mistborn.elementos;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.personajes.Personaje;

public final class Disparo {
	
	private Colision c;
	private World mundo;
	private OrthographicCamera cam;
	private ShapeRenderer linea;
	private Personaje pj;
	private Moneda moneda;
	private ArrayList<Body> monedasInutiles = new ArrayList<Body>();
	private Vector2 direccionBala,posIniBala,movimientoBala;
	private final float _amplitud = 1.5f,_velocidad = 1.3f;
	
	public Disparo(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		this.mundo = mundo;
		this.pj = pj;
		this.cam = cam;
		this.c = c;
		linea = new ShapeRenderer();
		direccionBala = new Vector2();
		posIniBala = new Vector2();
		movimientoBala = new Vector2();
		moneda = new Moneda();
	}
	
	public void drawLinea() {
		linea.setProjectionMatrix(cam.combined);		//Linea para debuggear el disparo.
		linea.begin(ShapeType.Line);
		linea.setColor(Color.CYAN);
		linea.line(pj.getX(), pj.getY(),pj.getInput().getMouseX()/Box2dConfig.PPM, pj.getInput().getMouseY()/Box2dConfig.PPM);
		linea.end();
	}
	
	public void disparar() {
		//Calcula solo la direccion no la distancia. con el .nor()
		direccionBala.set(pj.getInput().getMouseX()/Box2dConfig.PPM - pj.getX(), pj.getInput().getMouseY()/Box2dConfig.PPM - pj.getY()); 
		//movimientoBala guarda el valor de direccionBala, porque luego se normaliza y se pierde.
		movimientoBala.set(direccionBala.x*_velocidad,direccionBala.y*_velocidad);
		// direccionBala se normaliza para asegurarse de que tenga una longitud de 1, lo que significa que indica solo la dirección sin importar la distancia.
		direccionBala.nor(); 
		//Agarra la pos del pj y la suma con la direccion(normalizada es igual a 1) por la amplitud(radio).
	    posIniBala.set(pj.getX() + _amplitud * direccionBala.x, pj.getY() + _amplitud * direccionBala.y);	
		
	    moneda.crear(posIniBala, mundo);
	}
	
	public boolean calcularFuerzas(boolean disparando) {
		boolean balaEnAccion;
		
		//Aclaracion: Notamos que el pj que esta mas cerca del destino de la bala, esta va mas lenta, porque esta esperando a que llegue la bala del otro pj.
		//Esto con Redes no va a pasar obviamente.
		
		if(disparando) { 
			moneda.getBody().setLinearVelocity(movimientoBala);
			balaEnAccion = true;
		}else {	
			monedasInutiles.add(moneda.getBody());
			moneda.getBody().applyForceToCenter(new Vector2(0,0), true);
			balaEnAccion = false;
		}
		return balaEnAccion;
	}
	
	public void borrarMonedas() {		//Metodo que borra las monedas del mundo que estan inutilizadas.
		if(monedasInutiles.size()>0) {
			for (Body monedaInutil: monedasInutiles) {
				if(c.isMonedaInactiva(monedaInutil) && !monedaInutil.isAwake()) {
					mundo.destroyBody(monedaInutil);
				}
			}
		}
	}
	
	
	
	
	
	
}
