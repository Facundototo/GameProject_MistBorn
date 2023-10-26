package com.bakpun.mistborn.eventos;

import java.util.EventListener;

import com.badlogic.gdx.graphics.Color;
import com.bakpun.mistborn.enums.TipoPoder;

public interface EventoCrearBarra extends EventListener{
	void crearBarra(String ruta,Color color,TipoPoder tipo);
}
