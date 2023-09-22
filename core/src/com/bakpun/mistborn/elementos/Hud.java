package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;

public class Hud {

	private Stage stage;
	private Table tabla;
	private Image marcoVida;
	private float vida,escalado = 1.5f;
	private ShapeRenderer shape;
	
	//El Stage tiene su propia cam y viewport.
	
	//Terminar el hud.
	
	public Hud() {
		stage = new Stage();
		tabla = new Table();
		shape = new ShapeRenderer();
		
		stage.setViewport(new FillViewport(Config.ANCHO,Config.ALTO,stage.getCamera()));
		
		tabla.setFillParent(true);		//Con esto le digo que la tabla ocupe toda la pantalla.
		marcoVida = new Image(new Texture(Recursos.MARCO_VIDA));
		
		tabla.top().left().pad(30);		//Le pongo un padding de 30 px.
		tabla.add(marcoVida).size(marcoVida.getWidth()*escalado, marcoVida.getHeight()*escalado);
		this.vida = 182*escalado;
		
		//tabla.debug();
		stage.addActor(tabla);
	}
	
	public void draw(float delta) {
		// Este if es para probar si anda la vida.
		if(Gdx.input.isKeyPressed(Keys.S) && vida > 19*escalado) {
			vida -= 1f;
		}else if(Gdx.input.isKeyPressed(Keys.W) && vida < 182*escalado) {
			vida += 1f;	
		}
		//Rectangulo de la vida.
		shape.setProjectionMatrix(stage.getCamera().combined);
		shape.begin(ShapeType.Filled);
		shape.rect(marcoVida.getX()+12*escalado, marcoVida.getY(), vida, marcoVida.getHeight());
		shape.setColor(Color.RED);
		shape.end();
		
		
		stage.act(delta);	//Dibujo el HUD.
		stage.draw();
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	
}
