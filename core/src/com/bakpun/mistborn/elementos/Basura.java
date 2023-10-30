package com.bakpun.mistborn.elementos;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;


public class Basura {	
	/*Mas que nada esta clase esta hecha porque las monedas cuando las disparabas y cambiabas a otro poder 
	no se borraban porque no entraban en el codigo de borrarMonedas() de la clase Disparo.*/
	
	private static ArrayList<Moneda> monedasInutiles = new ArrayList<Moneda>();
	public static World mundo;
	public static Colision c;
	
	public static void agregarBasura(Moneda basura) {
		monedasInutiles.add(basura);
	}
	
	public static void borrarBasura() {
		if(monedasInutiles.size()>0) {
			for (int i = 0; i < monedasInutiles.size(); i++) {
				if(c.isMonedaColisiona(monedasInutiles.get(i).getBody()) && !monedasInutiles.get(i).getBody().isAwake()) {
					mundo.destroyBody(monedasInutiles.get(i).getBody());
					monedasInutiles.remove(i);
				}
			}
		}
	}
	
	
}
