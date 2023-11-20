package com.bakpun.mistborn.eventos;

import java.util.ArrayList;
import java.util.EventListener;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.Accion;
import com.bakpun.mistborn.enums.Movimiento;
import com.bakpun.mistborn.enums.TipoCliente;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;

public class Listeners {

	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void agregarListener(EventListener listener) { 	//Anade las clases que tengan eventos.
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		
	}
	
	public static void crearBarraHUD(String ruta,Color color,TipoPoder tipo) {		//Este evento va a llamar al metodo de Hud para crear los marcos del poder que se cree.
		for (EventListener listener : listeners) {
			if((listener instanceof EventoCrearBarra)) {
				System.out.println("se esta creando barra" + listener.getClass().getName());
				((EventoCrearBarra)listener).crearBarra(ruta,color,tipo);
			}
		}
	}
	
	public static void reducirVidaPj(float dano, TipoCliente cliente) {		//Evento que se utiliza en Hud y Personaje.
		for (EventListener listener : listeners) {
			if(listener instanceof EventoReducirVida) {
				((EventoReducirVida)listener).reducirVida(dano, cliente);
			}
		}
	}
	
	public static void reducirPoderPj(TipoPersonaje tipoPj,TipoPoder tipoPoder,float energia) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoGestionPoderes)
				((EventoGestionPoderes)listener).reducirPoder(tipoPj, tipoPoder, energia);
		}
	}
	
	public static void aumentarPoderPj(TipoPersonaje tipoPj,TipoPoder tipoPoder,float energia) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoGestionPoderes)
				((EventoGestionPoderes)listener).aumentarPoder(tipoPj, tipoPoder, energia);
		}
	}
	
	public static void activarPeltre(boolean activo) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoUtilizarPoderes)
				((EventoUtilizarPoderes)listener).activarPeltre(activo);
		}
	}
	
	public static void seleccionPoder(TipoPoder poder) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoUtilizarPoderes)
				((EventoUtilizarPoderes)listener).seleccionPoder(poder);
		}
	}
	
	public static void posMouse(float x, float y) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoUtilizarPoderes)
				((EventoUtilizarPoderes)listener).posMouse(x,y);
		}
	}
	
	public static void restarMonedas() {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoGestionMonedas)
			((EventoGestionMonedas)listener).restarMonedas();
		}
	}

	public static void aumentarMonedas() {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoGestionMonedas)
			((EventoGestionMonedas)listener).aumentarMonedas();
		}
	}
	
	public static void setDuracion(int segundo) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoSetDuracionPeltre)
			((EventoSetDuracionPeltre)listener).setDuracion(segundo);
		}
	}
	
	public static void mover(Movimiento movimiento) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoEntradasPj)
			((EventoEntradasPj)listener).mover(movimiento);
		}
	}
	
	public static void ejecutar(Accion accion) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoEntradasPj)
			((EventoEntradasPj)listener).ejecutar(accion);
		}
	}
	
	public static void actualizarPosClientes(TipoCliente tipoCliente, float x, float y) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoInformacionPj)
			((EventoInformacionPj)listener).actualizarPos(tipoCliente,x,y);
		}
	}
	
	public static void actualizarAnimaClientes(TipoCliente tipoCliente, int frameIndex,Movimiento mov,boolean saltando) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoInformacionPj)
			((EventoInformacionPj)listener).actualizarAnima(tipoCliente,frameIndex,mov,saltando);
		}
	}
	
	public static void terminarPartida(String texto, TipoCliente ganador) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoTerminaPartida)
			((EventoTerminaPartida)listener).terminarPartida(texto,ganador);
		}
	}
	
	public static void empezarPartida() {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoEmpiezaPartida)
			((EventoEmpiezaPartida)listener).empezarPartida();
		}
	}
	
	public static void actualizarColisionPj(float x, float y,boolean colisionando) {
		for (EventListener listener : listeners) {
			if(listener instanceof EventoInformacionPj)
			((EventoInformacionPj)listener).actualizarColisionPj(x,y,colisionando);
		}
	}
}
