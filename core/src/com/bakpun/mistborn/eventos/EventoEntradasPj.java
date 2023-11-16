package com.bakpun.mistborn.eventos;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.bakpun.mistborn.enums.Accion;
import com.bakpun.mistborn.enums.Movimiento;

public interface EventoEntradasPj extends EventListener{
	void mover(Movimiento movimiento);
	void ejecutar(Accion accion);
}
