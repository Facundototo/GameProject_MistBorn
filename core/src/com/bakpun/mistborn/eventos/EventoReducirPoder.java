package com.bakpun.mistborn.eventos;

import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;

public interface EventoReducirPoder {
	void reducirPoder(TipoPersonaje tipoPj,TipoPoder tipoPoder,float energia);
}
