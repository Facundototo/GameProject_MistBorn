package com.bakpun.mistborn.redes;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.EventListener;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.bakpun.mistborn.enums.Movimiento;
import com.bakpun.mistborn.eventos.EventoMoverPj;
import com.bakpun.mistborn.eventos.Listeners;


public final class HiloCliente extends Thread implements EventoMoverPj, EventListener{
	
	private DatagramSocket socket;
	private InetAddress ipServer;
	private int puerto = 7654, id = -1,seleccionOponente = 0;
	private boolean oponenteEncontrado = false, empiezaPartida = false,fin = false;
	public EstadoRed estado = EstadoRed.DESCONECTADO;
	
	public HiloCliente() {
		try {
			socket = new DatagramSocket();
			ipServer = InetAddress.getByName("255.255.255.255");	//Al principio se hace broadcast.
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		Listeners.agregarListener(this);
	}
	
	public void enviarMensaje(String msg) {
		byte[] data = msg.getBytes(); 
		DatagramPacket dp = new DatagramPacket(data,data.length,ipServer,puerto);
			try {
				socket.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
	
	@Override
	public void run() {
		do {
			byte[] datos = new byte[1024];
			DatagramPacket dp = new DatagramPacket(datos, datos.length);
			try {
				socket.receive(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			procesarMensaje(dp);
		}while(!fin);
	}


	private void procesarMensaje(DatagramPacket dp) {
		String msg[] = new String(dp.getData()).trim().split("#");
		System.out.println(msg[0]);
		
		switch(msg[0]) {
		case "OK":	//OK es cuando el server responde a conexion, con este ok nos guardamos la ip del server para no estar haciendo broadcast siempre.
			this.estado = EstadoRed.CONECTADO;
			this.ipServer = dp.getAddress();
			this.id = Integer.valueOf(msg[1]);
			break;
		case "OponenteEncontrado":	//Si el oponente esta listo, por ende vos tambien, se pasa a la PantallaSeleccion.
			this.oponenteEncontrado = true;
			break;
		case "EmpiezaPartida": 
			this.empiezaPartida = true;
			break;
		case "desconexion":		//Responde a la llamada de desconectar, para que le avise al otro cliente de que me desconecte.
			estado = EstadoRed.DESCONECTADO;	//Con el desconectado se fuerza al otro cliente que se vaya a la PantallaMenu.
			this.oponenteEncontrado = false;			//Reseteamos este booleano porque sino salta de la PantallaMenu a la PantallaSeleccion.
			break;
		case "seleccionOponente":	//Es la seleccion del oponente mientras se elige al pj.
			this.seleccionOponente = Integer.valueOf(msg[1]);
			break;
		}
	}
	
	public boolean isOponenteEncontrado() {
		return this.oponenteEncontrado;
	}
	public boolean isEmpiezaPartida() {
		return this.empiezaPartida;
	}
	public int getSeleccionOponente() {
		return this.seleccionOponente;
	}
	
	public EstadoRed getEstado() {
		return this.estado;
	}
	public String getMiId() {
		return String.valueOf(this.id);
	}

	@Override
	public void mover(Movimiento movimiento) {
		enviarMensaje("movimiento#" + movimiento.getMovimiento() + "#" + this.id);
	}

	@Override
	public boolean handle(Event event) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
