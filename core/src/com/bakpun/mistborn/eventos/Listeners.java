package com.bakpun.mistborn.eventos;

import java.util.ArrayList;

import java.util.EventListener;

import com.badlogic.gdx.graphics.Color;

public class Listeners {

	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void agregarListener(EventListener listener) { 	//Anade las clases que tengan eventos.
		listeners.add(listener);
	}
	
	public static void crearBarraHUD(String ruta,Color color) {		//Este evento va a llamar al metodo de Hud para crear los marcos del poder que se cree.
		for (EventListener listener : listeners) {
			if(listener instanceof EventoCrearBarra) {		//Esta condicion porque antes me generaba un error cuando anadimos el pj al Arraylist de listeners.
				((EventoCrearBarra)listener).crearBarra(ruta,color);
			}
		}
	}
	
	public static void reducirVidaPj(float dano) {		//Evento que se utiliza en Hud y Personaje.
		for (EventListener listener : listeners) {
			((EventoReducirVida)listener).reducirVida(dano);
		}
	}
}
