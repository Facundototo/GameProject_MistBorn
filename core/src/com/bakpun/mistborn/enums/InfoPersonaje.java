package com.bakpun.mistborn.enums;

import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public enum InfoPersonaje {
	
	//Contiene la informacion de los personajes en la PantallaSeleccion.
	
	VIN("seleccion.vin","Vin",Recursos.PERSONAJE_VIN,Recursos.CABEZA_VIN),
	HAM("seleccion.ham","Ham",Recursos.PERSONAJE_VIN,Recursos.CABEZA_VIN),
	LESTIBOURNES("seleccion.lestibournes","Lestibournes",Recursos.PERSONAJE_VIN,Recursos.CABEZA_VIN),
	DOCKSON("seleccion.dockson","Dockson",Recursos.PERSONAJE_VIN,Recursos.CABEZA_VIN);
	
	private String infoPj,nombreClasePj,rutaTexturaPj,rutaIconoPj;
	
	InfoPersonaje(String indice,String clasePj,String rutaTexturaPj,String rutaIconoPj) {
		this.infoPj = indice;
		this.nombreClasePj = clasePj;
		this.rutaTexturaPj = rutaTexturaPj;
		this.rutaIconoPj = rutaIconoPj;
	}
	
	public String getInfo() {
		return Render.bundle.get(infoPj);
	}
	public String getNombre() {
		return this.nombreClasePj;
	}
	public String getRutaTextura() {
		return this.rutaTexturaPj;
	}
	public String getRutaIcono() {
		return this.rutaIconoPj;
	}
	
	
}
