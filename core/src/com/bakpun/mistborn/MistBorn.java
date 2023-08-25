package com.bakpun.mistborn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bakpun.mistborn.pantallas.PantallaCarga;
import com.bakpun.mistborn.utiles.Render;

public class MistBorn extends Game {
	
	public void create() {
		Render.cancionMenu.play();	//Reproduzco la cancion aca porque quiero que se escuche en la 2 pantallas diferentes.
		Render.app = this;			//Asigno a app esta clase para hacer el metodo setScreen() en otras pantallas.
		Render.batch = new SpriteBatch();	//SpriteBatch unico.
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		this.setScreen(new PantallaCarga());
	}

	public void render () {
		super.render();
	}
	
	public void dispose () {
		Render.batch.dispose();		//SpriteBatch.
		super.dispose();		
	}
}
