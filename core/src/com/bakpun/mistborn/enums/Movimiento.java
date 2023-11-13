package com.bakpun.mistborn.enums;

public enum Movimiento {
	IZQUIERDA("izq"),DERECHA("der"),SALTO("salto"),QUIETO("quieto");
	
	private String movimiento;
	
	Movimiento(String movimiento){
		this.movimiento = movimiento;
	}

	public String getMovimiento() {
		return movimiento;
	}
	
	
	
}
