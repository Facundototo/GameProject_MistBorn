package com.bakpun.mistborn.pantallas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bakpun.mistborn.elementos.Imagen;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public class PantallaCarga implements Screen {
	private Imagen logo;
	private float a = 0f,contEspera = 0f;
	private int espera = 3;
	private boolean terminoFadeIn = false,termina = false;
	private OrthographicCamera camZoom;		//Camara para hacer el zoom y para el viewport.
	private Viewport vw;
	private Entradas entradas;
	
	public void show() {
		logo = new Imagen(Recursos.LOGO_MISTBORN);
		logo.ajustarTamano(-0.5f);
		camZoom = new OrthographicCamera();
		camZoom.position.set(new Vector2(logo.getTexture().getWidth()/2,logo.getTexture().getHeight()/2), 0);
		vw = new FillViewport(Config.ANCHO,Config.ALTO,camZoom);
		entradas = new Entradas();
		Gdx.input.setInputProcessor(entradas);
	}
	public void render(float delta) {
		Render.limpiarPantalla(0,0,0);
		logo.setTransparencia(a);
		termina = procesarFadePantalla();
		camZoom.update();	
		Render.batch.setProjectionMatrix(vw.getCamera().combined);
		camZoom.zoom -= 0.0004;
		Render.batch.begin();
		logo.draw();
		Render.batch.end();
		if(termina || entradas.isEspacio()) {
			Render.app.setScreen(new PantallaMenu());
		}
	}
	private boolean procesarFadePantalla() {
		if (!terminoFadeIn) {
			a += 0.003f;
			if (a > 1) {
				a = 1;
				terminoFadeIn = true;
			}
		} else {
			if (contEspera > espera) {
				a -= 0.007f;
				if(a < 0) {
					a = 0;
					termina = true;
				}
			} else {
				contEspera += 0.01;
			}
		}
		return termina;
	}
	public void resize(int width, int height) {
		vw.update(width, height);	
	}
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	public void dispose() {
		logo.getTexture().dispose(); //Texture
		Render.batch.dispose();		//SpriteBatch.
		this.dispose();
	}

}
