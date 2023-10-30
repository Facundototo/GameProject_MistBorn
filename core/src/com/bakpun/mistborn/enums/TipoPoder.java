package com.bakpun.mistborn.enums;

public enum TipoPoder {
	ACERO(0),HIERRO(1),PELTRE(2);	//Indices de la seleccion cuando sos nacido de la bruma,esto ayuda al hud a restar la energia.
		
	private int nroSeleccion;
	
	TipoPoder(int nroSeleccion){
		this.nroSeleccion = nroSeleccion;
	}
	
	public int getNroSeleccion() {
		return this.nroSeleccion;
	}
}
