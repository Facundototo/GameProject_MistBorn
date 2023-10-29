package com.bakpun.mistborn.poderes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Colision;
import com.bakpun.mistborn.personajes.Personaje;

public interface Disparable {

	void crearDisparo(World mundo,Personaje pj,OrthographicCamera cam,Colision c);
	
}
