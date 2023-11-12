package com.bakpun.mistborn.utiles;

import com.bakpun.mistborn.pantallas.PantallaMenu;
import com.bakpun.mistborn.redes.EstadoRed;
import com.bakpun.mistborn.redes.HiloCliente;

public class Red {
	
	private static HiloCliente hc = new HiloCliente();	//Creo la instancia al inicio del juego.
	
	public static void iniciar() {	//La usamos en MistBorn (en la main).
		hc.start();					//Porque entendemos de que si abrio el juego es si o si para jugarlo online.
	}
	
	public static void conectar() {	//Envia mensaje al server en busca de conectarse.
		hc.enviarMensaje("conexion");
	}
	
	public static void desconectar() { //Envia mensaje al server en busca de desconectarse.
		hc.enviarMensaje("desconectar");
	}
	
	public static void listoSeleccion(boolean listo) {
		hc.enviarMensaje(((listo)?"listo#":"nolisto#") + hc.getMiId());
	}
	
	public static void enviarSeleccion(int seleccion) {
		hc.enviarMensaje("seleccion#" + hc.getMiId() + "#" + String.valueOf(seleccion));
	}
	
	public static void chequearEstado() {
		if(hc.getEstado() == EstadoRed.DESCONECTADO) {	
			Render.app.setScreen(new PantallaMenu());
		}
	}
	
	public static int getSeleccionOponente() {
		return hc.getSeleccionOponente();
	}
	
	public static boolean isOponenteEncontrado() {
		return hc.isOponenteEncontrado();
	}
	
	public static boolean isEmpiezaPartida() {
		return hc.isEmpiezaPartida();
	}
	
	public static EstadoRed getEstado() {
		return hc.getEstado();
	}
	
	public static int getId() {
		return Integer.valueOf(hc.getMiId());
	}
}
