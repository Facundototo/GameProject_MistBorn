package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.elementos.SkinFreeTypeLoader;
import com.bakpun.mistborn.enums.Fuente;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.redes.EstadoRed;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Red;
import com.bakpun.mistborn.utiles.Render;

public class PantallaEspera implements Screen {
	
	private Stage stage;
	private Skin skin;
	private Label esperando, escape;
	private Table tabla;
	private Entradas entradas;

	@Override
	public void show() {
		entradas = new Entradas();
		Gdx.input.setInputProcessor(entradas);
		stage = new Stage(new FillViewport(Config.ANCHO, Config.ALTO));
		skin = SkinFreeTypeLoader.cargar();
		tabla = new Table();
		tabla.setFillParent(true);
		
		esperando = new Label("", Fuente.PIXELMENU.getStyle(skin));
		esperando.addAction(Actions.forever(Actions.sequence(
				Actions.fadeIn(1f),Actions.fadeOut(1f),		
			    Actions.delay(0.4f)
				)));
		
		escape = new Label("Pulsa ESC para volver al Menu", Fuente.PIXELMENU.getStyle(skin));
		
		tabla.add(esperando).center().expandY().row();
		tabla.add(escape).bottom().pad(50);
		stage.addActor(tabla);
		
		Red.conectar();	//Cuando se crea esta pantalla se conecta al server.
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla(0,0,0);
	
		if(Red.getEstado() == EstadoRed.CONECTADO) {
			esperando.setText("Buscando Oponente...");
		}else {
			esperando.setText("Buscando Servidor...");
		}
		
		if(entradas.isEscape()) {		//Si toca ESCAPE se desconecta del server.
			Red.desconectar();
			Render.app.setScreen(new PantallaMenu());
		}
		
		if(Red.isOponenteEncontrado()){Render.app.setScreen(new PantallaSeleccion());}	//Si los 2 estan listos van a la PantallaSeleccion.
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}
	
	
	
}
