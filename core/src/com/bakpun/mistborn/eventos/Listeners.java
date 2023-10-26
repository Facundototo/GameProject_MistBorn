package com.bakpun.mistborn.eventos;

import java.util.ArrayList;
import java.util.EventListener;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.TipoPoder;

public class Listeners {

	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void agregarListener(EventListener listener) { 	//Anade las clases que tengan eventos.
		listeners.add(listener);
	}
	
	public static void crearBarraHUD(String ruta,Color color,TipoPoder tipo) {		//Este evento va a llamar al metodo de Hud para crear los marcos del poder que se cree.
		for (EventListener listener : listeners) {
				((EventoCrearBarra)listener).crearBarra(ruta,color,tipo);
			}
	}
	
	public static void reducirVidaPj(float dano) {		//Evento que se utiliza en Hud y Personaje.
		for (EventListener listener : listeners) {
			((EventoReducirVida)listener).reducirVida(dano);
		}
	}
	
	public static void restarMonedas() {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoRestarMonedas)
			((EventoRestarMonedas)listener).restarMonedas();
		}
	}
}
