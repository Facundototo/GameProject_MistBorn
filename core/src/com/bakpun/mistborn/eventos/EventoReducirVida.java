package com.bakpun.mistborn.eventos;

import java.util.EventListener;

public interface EventoReducirVida extends EventListener{
	void reducirVida(float dano);
}
