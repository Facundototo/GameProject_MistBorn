package com.bakpun.mistborn.enums;

public enum Accion {
	
	GOLPE("golpe"),DISPARANDO("disparando"),TOCA_R("toca_r"),NADA("nada");
	
	private String accion;
	
	Accion(String accion){
		this.accion = accion;
	}

	public String getAccion() {
		return accion;
	}
}
