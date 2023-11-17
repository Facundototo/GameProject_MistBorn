package com.bakpun.mistborn.eventos;

import com.bakpun.mistborn.enums.TipoCliente;

public interface EventoTerminaPartida {
	void terminarPartida(String texto, TipoCliente ganador);
}
