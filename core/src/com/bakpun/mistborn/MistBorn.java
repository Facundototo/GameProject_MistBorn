package com.bakpun.mistborn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bakpun.mistborn.elementos.Audio;
import com.bakpun.mistborn.pantallas.PantallaCarga;
import com.bakpun.mistborn.redes.EstadoRed;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Red;
import com.bakpun.mistborn.utiles.Render;

public class MistBorn extends Game {

	
	public void create() {
		Render.audio = new Audio();
		Render.app = this;			//Asigno a app esta clase para hacer el metodo setScreen() en otras pantallas.
		Render.batch = new SpriteBatch();	//SpriteBatch unico.
		
		Pixmap cursor = new Pixmap(Gdx.files.internal(Recursos.CURSOR_MOUSE));	//Cargamos un cursor.
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, cursor.getWidth()/2, cursor.getHeight()/2));
		//Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		Gdx.graphics.setWindowedMode(Config.ANCHO/3,Config.ALTO/2);
		this.setScreen(new PantallaCarga());
		Red.iniciar();	//start() el HiloCliente.
	}

	public void render () {
		super.render();
	}
	
	public void dispose () { // No va a mandar desconectar si nunca se conecto al server (pantalla menu).
		if(Red.getEstado() == EstadoRed.CONECTADO) {	//Si esta en la pantalla espera o seleccion entonces esta conectado.
			Red.desconectar();	//Este desconectar() para el cliente que toque ALT F4. Hace el mismo procedimiento de ESC.
		}
		Render.batch.dispose();		//SpriteBatch.
		Render.audio.dispose();
		super.dispose();		
	}
}
