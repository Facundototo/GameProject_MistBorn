package com.bakpun.mistborn.redes;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HiloCliente extends Thread{
	
	private DatagramSocket socket;
	private boolean fin = false;
	private InetAddress ipServer;
	private int puerto = 7654;
	private boolean oponenteListo = false;
	public EstadoRed estado = EstadoRed.DESCONECTADO;
	
	public HiloCliente() {
		try {
			socket = new DatagramSocket();
			ipServer = InetAddress.getByName("255.255.255.255");	//Al principio se hace broadcast.
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
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
		String msg = new String(dp.getData()).trim();
		System.out.println(msg);
		switch(msg) {
		case "OK":	//OK es cuando el server responde a conexion, con este ok nos guardamos la ip del server para no estar haciendo broadcast siempre.
			estado = EstadoRed.CONECTADO;
			this.ipServer = dp.getAddress();
			break;
		case "OponenteListo":	//Si el oponente esta listo, por ende vos tambien, se pasa a la PantallaSeleccion.
			this.oponenteListo = true;
			break;
		case "desconexion":		//Responde a la llamada de desconectar, para que le avise al otro cliente de que me desconecte.
			estado = EstadoRed.DESCONECTADO;	//Con el desconectado se fuerza al otro cliente que se vaya a la PantallaMenu.
			this.oponenteListo = false;			//Reseteamos este booleano porque sino salta de la PantallaMenu a la PantallaSeleccion.
			break;
		}
	}
	
	public boolean isOponenteListo() {
		return this.oponenteListo;
	}
	public EstadoRed getEstado() {
		return this.estado;
	}
	
}
