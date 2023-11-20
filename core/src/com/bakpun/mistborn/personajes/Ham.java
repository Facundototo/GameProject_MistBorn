package com.bakpun.mistborn.personajes;

import com.bakpun.mistborn.enums.Spawn;
import com.bakpun.mistborn.enums.TipoCliente;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Peltre;
import com.bakpun.mistborn.utiles.Recursos;

public final class Ham extends Personaje{

	public Ham(Entradas entradas,Spawn spawn,TipoCliente tipoCliente) {
		super(Recursos.PERSONAJE_HAM,Recursos.SALTOS_HAM,Recursos.ANIMACIONES_ESTADOS_HAM, entradas,spawn,tipoCliente,TipoPersonaje.VIOLENTO);
	}

	@Override
	protected void crearPoderes() {
		super.poderes[0] = new Peltre(this);
	}

}
