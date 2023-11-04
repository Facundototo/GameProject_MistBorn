package com.bakpun.mistborn.eventos;

import java.util.EventListener;

import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;

public interface EventoGestionPoderes extends EventListener{	//Este evento no lo ponemos en Poder porque ya el metodo abstracto quemar() funciona asi.
	void reducirPoder(TipoPersonaje tipoPj,TipoPoder tipoPoder,float energia);
	void aumentarPoder(TipoPersonaje tipoPj,TipoPoder tipoPoder, float energia);
}		
