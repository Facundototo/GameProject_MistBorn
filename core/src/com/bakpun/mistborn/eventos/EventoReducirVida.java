package com.bakpun.mistborn.eventos;

import java.util.EventListener;

import com.bakpun.mistborn.enums.TipoCliente;

public interface EventoReducirVida extends EventListener{
	void reducirVida(float dano, TipoCliente cliente);
}
