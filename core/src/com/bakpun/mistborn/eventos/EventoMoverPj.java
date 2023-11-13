package com.bakpun.mistborn.eventos;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.bakpun.mistborn.enums.Movimiento;

public interface EventoMoverPj extends EventListener{
	void mover(Movimiento movimiento);
}
