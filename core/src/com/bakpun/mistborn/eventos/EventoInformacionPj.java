package com.bakpun.mistborn.eventos;

import com.bakpun.mistborn.enums.Movimiento;
import com.bakpun.mistborn.enums.TipoCliente;

public interface EventoInformacionPj {
	void actualizarPos(TipoCliente tipoCliente, float x, float y);
	void actualizarAnima(TipoCliente tipoCliente, int frameIndex,Movimiento mov,boolean saltando);
}
