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
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.personajes.Personaje;

public final class Disparo{
	
	private Colision c;
	private World mundo;
	private OrthographicCamera cam;
	private ShapeRenderer linea;
	private Personaje pj;
	private Moneda moneda;
	private ArrayList<Body> monedasInutiles = new ArrayList<Body>();
	private Vector2 direccionBala,posIniBala,movimientoBala,fuerzaContraria;
	private final float _amplitud = 1.5f,_velocidad = 30f;
	private boolean balaDisparada = false;
	
	//Faltaria destruir la moneda cuando lo toca.
	
	public Disparo(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		this.mundo = mundo;
		this.pj = pj;
		this.cam = cam;
		this.c = c;
		linea = new ShapeRenderer();
		direccionBala = new Vector2();
		posIniBala = new Vector2();
		movimientoBala = new Vector2();
		fuerzaContraria = new Vector2();
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
		if(!balaDisparada) {
			actualizarDireccion(pj.getInput().getMouseX()/Box2dConfig.PPM,pj.getInput().getMouseY()/Box2dConfig.PPM);
			//Agarra la pos del pj y la suma con la direccion(normalizada es igual a 1) por la amplitud(radio).
		    posIniBala.set(pj.getX() + _amplitud * direccionBala.x, pj.getY() + _amplitud * direccionBala.y);	
			
		    moneda.crear(posIniBala, mundo);
		    balaDisparada = true;
		}
	}
	
	public void calcularFuerzas(boolean disparando) {
		
		//Aclaracion: El juego se crashea me parece si saltamos y vamos disparando, no sabemos a que se debe.
		
		borrarMonedas();
		
		if(balaDisparada) {
			if(disparando) { 
				moneda.getBody().setLinearVelocity(movimientoBala);
				balaDisparada = true;
				if(c.isMonedaColisiona(moneda.getBody())) {
					actualizarDireccion(pj.getInput().getMouseX()/Box2dConfig.PPM,pj.getInput().getMouseY()/Box2dConfig.PPM);		//Actualizo la direccion opuesta que va a tomar el pj, porque puede ser diferente a la direccion inicial.
					//Cuando la moneda toca algo inamovible mientras dispara el pj, este se impulsa para la direccion contraria a la moneda.
					pj.aplicarFuerza(fuerzaContraria);		 
				}
				if(c.isPjMoneda(pj.getBody())) {
					Listeners.reducirVidaPj(1);		
				}
			}else {	
				Listeners.restarMonedas();
				monedasInutiles.add(moneda.getBody());
				moneda.getBody().applyForceToCenter(new Vector2(0,0), true);	//Para que la moneda caiga realisticamente.
				balaDisparada = false;
			}
		}
	}
	
	private void borrarMonedas() {		//Metodo que borra las monedas del mundo que estan inutilizadas.
		if(monedasInutiles.size()>0) {
			for (int i = 0; i < monedasInutiles.size(); i++) {
				if(c.isMonedaColisiona(monedasInutiles.get(i)) && !monedasInutiles.get(i).isAwake()) {
					mundo.destroyBody(monedasInutiles.get(i));
					monedasInutiles.remove(i);
				}
			}
		}
	}
	
	public void actualizarDireccion(float destinoX,float destinoY) {		//Metodo reutilizable que actualiza y que va a ayudar para la direccion de la moneda y del pj.
		//Calcula solo la direccion no la distancia. con el .nor()
		direccionBala.set(destinoX - pj.getX(), destinoY - pj.getY()); 
		// direccionBala se normaliza para asegurarse de que tenga una longitud de 1, lo que significa que indica solo la dirección sin importar la distancia.
		direccionBala.nor(); 
		//movimientoBala guarda el valor de direccionBala.
		movimientoBala.set(direccionBala.x*_velocidad,direccionBala.y*_velocidad);
		//fuerzaContraria es el movimiento contrario al que va la bala.
		fuerzaContraria.set(-movimientoBala.x, -movimientoBala.y);
	}
	
	public int getCantMonedas() {
		return this.pj.getCantMonedas();
	}
	public Vector2 getMovimientoBala() {
		return this.movimientoBala;
	}
}
