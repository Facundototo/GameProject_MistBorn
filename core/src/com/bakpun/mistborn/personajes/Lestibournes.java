package com.bakpun.mistborn.personajes;

import com.bakpun.mistborn.enums.Spawn;
import com.bakpun.mistborn.enums.TipoCliente;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.poderes.Hierro;
import com.bakpun.mistborn.utiles.Recursos;

public final class Lestibournes extends Personaje{

	public Lestibournes(Entradas entradas,Spawn spawn,TipoCliente tipoCliente) {
		super(Recursos.PERSONAJE_VIN,Recursos.SALTOS_VIN,Recursos.ANIMACIONES_ESTADOS_VIN, entradas,spawn,tipoCliente,TipoPersonaje.ATRAEDOR);
	}

	@Override
	protected void crearPoderes() {
		super.poderes[0] = new Hierro(this);
	}

}
