package com.bakpun.mistborn.eventos;

import com.bakpun.mistborn.enums.TipoPoder;

public interface EventoUtilizarPoderes {
	void activarPeltre(boolean activo);
	void seleccionPoder(TipoPoder poder);
	void posMouse(float x, float y);
}
