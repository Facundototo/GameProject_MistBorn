package com.bakpun.mistborn.eventos;

import java.util.ArrayList;
import java.util.EventListener;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.poderes.EventoCrearBarra;

public class Listeners {

	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void agregarListener(EventListener listener) { 	//Anade las clases que tengan eventos.
		listeners.add(listener);
	}
	
	public static void crearBarraHUD(String ruta,Color color) {		//Este evento va a llamar al metodo de Hud para crear los marcos del poder que se cree.
		for (EventListener listener : listeners) {
			((EventoCrearBarra)listener).crearBarra(ruta,color);
		}
	}
	
}
