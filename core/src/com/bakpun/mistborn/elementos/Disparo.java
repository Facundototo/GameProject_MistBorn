package com.bakpun.mistborn.elementos;



import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.enums.TipoAudio;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.objetos.GestorMonedas;
import com.bakpun.mistborn.objetos.Moneda;
import com.bakpun.mistborn.personajes.Personaje;
import com.bakpun.mistborn.utiles.Render;

public final class Disparo{
	
	private Colision c;
	private World mundo;
	private Personaje pj;
	private Moneda moneda;
	private Vector2 direccionBala,posIniBala,movimientoBala,fuerzaContraria;
	private final float _amplitud = 1.5f,_velocidad = 30f;
	private boolean balaDisparada = false;
	private float energia;
	
	
	public Disparo(World mundo,Personaje pj,OrthographicCamera cam,Colision c) {
		this.mundo = mundo;
		this.pj = pj;
		this.c = c;
		direccionBala = new Vector2();
		posIniBala = new Vector2();
		movimientoBala = new Vector2();
		fuerzaContraria = new Vector2();
		
	}
	
	public void disparar(float energia) {
		//Le paso la energia por aca para que se chequee en el metodo calcularFuerzas(). Porque sino se puede quedar manteniendo el disparo de por vida.
		this.energia = energia;		
		if(!balaDisparada) {
			Render.audio.pjDisparo.play(Render.audio.getVolumen(TipoAudio.SONIDO));
			actualizarDireccion(pj.getInput().getMouseX()/Box2dConfig.PPM,pj.getInput().getMouseY()/Box2dConfig.PPM);
			//Agarra la pos del pj y la suma con la direccion(normalizada es igual a 1) por la amplitud(radio).
		    posIniBala.set(pj.getX() + _amplitud * direccionBala.x, pj.getY() + _amplitud * direccionBala.y);	
		    
		    Moneda moneda = new Moneda();
		    this.moneda = moneda;		//Ponemos la variable moneda para que se use en los otros metodos.
		    moneda.crear(posIniBala, mundo);
		    GestorMonedas.agregarMoneda(moneda);	//La agregamos a este GestorMonedas para que despues de dibuje en PantallaPvP.
		    
		    balaDisparada = true;
		}
	}
	
	public void calcularFuerzas(boolean disparando) {
		
		//Aclaracion: El juego se crashea nos parece, si saltamos y vamos disparando, no sabemos a que se debe.
		
		if(balaDisparada) {
			
			if(disparando && this.energia > 0) { 
				moneda.getBody().setLinearVelocity(movimientoBala);
				balaDisparada = true;
				if(c.isMonedaColisiona(moneda.getBody())) {//Actualizar la direccion aca tambien me genera que mientras estes disparando el personaje se pueda mover con el mouse.
					actualizarDireccion(pj.getInput().getMouseX()/Box2dConfig.PPM,pj.getInput().getMouseY()/Box2dConfig.PPM);		//Actualizo la direccion opuesta que va a tomar el pj, porque puede ser diferente a la direccion inicial.
					//Cuando la moneda toca algo inamovible mientras dispara el pj, este se impulsa para la direccion contraria a la moneda.
					pj.aplicarFuerza(fuerzaContraria);	
				}
				if(c.isPjMoneda(pj.getBody())) {
					//Listeners.reducirVidaPj(1);		
				}
			}else {	
				Listeners.restarMonedas();
				GestorMonedas.agregarBasura(moneda.getBody());		//La agregamos a la basura para despues borrarla.
				moneda.getBody().applyForceToCenter(new Vector2(0,0), true);	//Para que la moneda caiga realisticamente.
				balaDisparada = false;
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
	public Vector2 getFuerzaContraria() {
		return this.fuerzaContraria;
	}
}
