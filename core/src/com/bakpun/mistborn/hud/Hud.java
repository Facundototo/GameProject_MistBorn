package com.bakpun.mistborn.hud;

import java.util.ArrayList;

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
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.poderes.EventoCrearBarra;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;

public class Hud implements EventoCrearBarra{

	private Stage stage;
	private Table tabla;
	private Image marcoVida;
	private Image[] marcosPoder = new Image[3];		//Lo creamos con el max de poderes que puede tener un pj pero lo limita el for de shapesPoder.
	private float vida,escalado = 1.5f;
	private ShapeRenderer shapeVida;
	private ArrayList<ShapeRenderer> shapesPoder;	//Este arraylist porque no se sabe cuantos poderes se van a crear.
	
	public Hud() {
		Listeners.agregarListener(this);
		
		stage = new Stage();
		tabla = new Table();
		shapeVida = new ShapeRenderer();
		shapesPoder = new ArrayList<ShapeRenderer>();
		
		shapeVida.setColor(Color.RED);
		
		stage.setViewport(new FillViewport(Config.ANCHO,Config.ALTO,stage.getCamera()));
		
		tabla.setFillParent(true);		//Con esto le digo que la tabla ocupe toda la pantalla.
		marcoVida = new Image(new Texture(Recursos.MARCO_VIDA));
		
		tabla.top().left().pad(30);		//Le pongo un padding de 30 px.
		tabla.add(marcoVida).size(marcoVida.getWidth()*escalado, marcoVida.getHeight()*escalado).row();
		this.vida = (marcoVida.getWidth()*escalado)-27;
		
		stage.addActor(tabla);
	}
	
	public void draw(float delta) {
		// Este if es para probar si anda la vida.
		if(Gdx.input.isKeyPressed(Keys.S) && vida > 19*escalado) {
			vida -= 1f;
		}else if(Gdx.input.isKeyPressed(Keys.W) && vida < 182*escalado) {
			vida += 1f;	
		}
		
		drawVida();
		drawPoderes();
		
		stage.act(delta);	//Dibujo el HUD.
		stage.draw();
	}
	
	public Stage getStage() {
		return this.stage;
	}

	public void drawVida() {
		shapeVida.setProjectionMatrix(stage.getCamera().combined);
		shapeVida.begin(ShapeType.Filled);
		shapeVida.rect(marcoVida.getX()+12*escalado, marcoVida.getY(), vida, marcoVida.getHeight());
		shapeVida.end();
	}
	
	public void drawPoderes() {
		for (int i = 0; i < shapesPoder.size(); i++) {
			shapesPoder.get(i).setProjectionMatrix(stage.getCamera().combined);
			shapesPoder.get(i).begin(ShapeType.Filled);
			shapesPoder.get(i).rect(marcosPoder[i].getX()+12*escalado, marcosPoder[i].getY(), vida, marcosPoder[i].getHeight());
			shapesPoder.get(i).end();
		}
	}
	
	
	@Override
	public void crearBarra(String ruta,Color color) {
		shapesPoder.add(new ShapeRenderer());
		shapesPoder.get(shapesPoder.size()-1).setColor(color);
		
		final int  _index = shapesPoder.size()-1;
		
		marcosPoder[_index] = new Image(new Texture(ruta));
		tabla.add(marcosPoder[_index]).size(marcosPoder[_index].getWidth()*escalado, marcosPoder[_index].getHeight()*escalado).padTop(10).left().row();
	}
	
	
}
