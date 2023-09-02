package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bakpun.mistborn.utiles.Recursos;

public class Hud {

	private Stage stage;
	private Table tabla;
	private Skin skin;
	Image marcoVida,vida,iconoCorazon;
	
	//El Stage tiene su propia cam y viewport.
	
	//Terminar el hud.
	
	public Hud() {
		stage = new Stage();
		tabla = new Table();
		skin = new Skin(Gdx.files.internal(Recursos.SKIN));
		
		tabla.setFillParent(true);
		stage.addActor(tabla);
		tabla.setDebug(true);
		
		marcoVida = new Image(new Texture(Recursos.MARCO_VIDA));
		marcoVida.setSize(200, 30);
		vida = new Image(new Texture(Recursos.VIDA));
		vida.setSize(200, 30);
		iconoCorazon = new Image(new Texture(Recursos.ICONO_CORAZON));
		iconoCorazon.setSize(40, 40);
		
		
		tabla.add(marcoVida);
		tabla.add(vida);
		tabla.add(iconoCorazon);
		
		stage.addActor(marcoVida);
		stage.addActor(vida);
		stage.addActor(iconoCorazon);
	}
	
	public void draw(float delta) {
		if(Gdx.input.isKeyPressed(Keys.S)) {
			vida.setWidth(vida.getWidth()-1);
		}
		stage.act(delta);
		stage.draw();
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	
}
