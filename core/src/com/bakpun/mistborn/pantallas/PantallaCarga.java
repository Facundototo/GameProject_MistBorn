package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public class PantallaCarga implements Screen{

	private Stage stage;
	private Image logo;
	private Entradas entradas;
	
	
	public PantallaCarga() {
		stage = new Stage();
		logo = new Image(new Texture(Recursos.LOGO_MISTBORN));
		entradas = new Entradas();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(entradas);
		stage.addActor(logo);
		
		((OrthographicCamera)stage.getViewport().getCamera()).zoom = 3f;
		logo.getColor().a = 0;		//Canal alpha del logo empieza en 0.
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla(0, 0, 0);
		((OrthographicCamera)stage.getViewport().getCamera()).zoom -= 0.003f;	//Se aumenta el zoom.
		
		if(((OrthographicCamera)stage.getViewport().getCamera()).zoom < 3f) {	//Hace una especie de fadeIn.
			logo.addAction(Actions.alpha(1, 1));
		}
		//Si llega a determinado zoom o toca click o espacion salta a la otra pantalla.
		if(((OrthographicCamera)stage.getViewport().getCamera()).zoom <= 1.5f || entradas.isEspacio() || entradas.isBotonIzq()) {
			logo.addAction(Actions.sequence(
					Actions.fadeOut(0.7f),
					Actions.run(new Runnable() {	
						@Override
						public void run() {
							Render.app.setScreen(new PantallaMenu());
						}
					})));
		}
		stage.draw();
		stage.act();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		// TODO Auto-generated method stub
		
	}
	
}
