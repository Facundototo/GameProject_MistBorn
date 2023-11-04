package com.bakpun.mistborn.eventos;

import java.util.EventListener;

public interface EventoGestionMonedas extends EventListener{
	void restarMonedas();
	void aumentarMonedas();
}
