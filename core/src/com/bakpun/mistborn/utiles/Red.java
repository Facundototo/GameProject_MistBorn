package com.bakpun.mistborn.utiles;

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
	
	public static boolean isOponenteListo() {
		return hc.isOponenteListo();
	}
	
	public static EstadoRed getEstado() {
		return hc.getEstado();
	}
}
