package com.bakpun.mistborn.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.elementos.SkinFreeTypeLoader;
import com.bakpun.mistborn.io.Entradas;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public class PantallaMenu implements Screen{

	private VentanaOpciones vo;
	private Stage stage;
	private Skin skin;
	private Stack stack;
	private Table tablaCont, tablaBotones;
	private TextButton botones[] = new TextButton[3];
	private Image fondo,barraNegra;
	private Entradas entradas;
	private InputMultiplexer im;
	private int seleccion = 0;
	private float tiempo = 0;  
	private boolean mostrarOpciones = false;
	
	
	public PantallaMenu(){
		fondo = new Image(new Texture(Recursos.FONDO_MENU));
		barraNegra = new Image(new Texture(Recursos.BARRA_NEGRA));
		stage = new Stage(new FillViewport(Config.ANCHO, Config.ANCHO));
		entradas = new Entradas();
		vo = new VentanaOpciones((OrthographicCamera) stage.getCamera(),entradas);
		im = new InputMultiplexer();
		skin = SkinFreeTypeLoader.cargar();
		stack = new Stack();
		tablaBotones = new Table();
		tablaBotones.setFillParent(false);
		tablaCont = new Table();
		tablaCont.setFillParent(true);
		botones[0] = new TextButton("Jugar", skin);
		botones[1] = new TextButton("Opciones", skin);
		botones[2] = new TextButton("Salir", skin);
	}
	
	@Override
	public void show() {
		im.addProcessor(entradas);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		
		
		tablaCont.add(tablaBotones).expand().left().padLeft(Config.ANCHO/8); 
		
		for (int i = 0; i < botones.length; i++) {
			final int opc = i;
			tablaBotones.add(botones[i]).size(150, 50).pad(20).row();
			botones[i].addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					seleccion = opc;
				}
			});
		}
		
		stack.add(barraNegra);
		stack.add(tablaCont);
		
		fondo.addAction(Actions.forever(Actions.parallel(Actions.sequence(
				Actions.moveTo(-1600, -1500, 10),
				Actions.fadeOut(1),
				Actions.moveTo(0, 0),
				Actions.fadeIn(1)
				))));
		
		barraNegra.setSize(Config.ANCHO/7, Config.ALTO);
		barraNegra.setPosition(Config.ANCHO/9.5f,Config.ALTO/1.5f);
		barraNegra.getColor().a = 0.7f;
		
		
		stage.addActor(fondo);
		stage.addActor(barraNegra);
		stage.addActor(tablaCont);
		
		stage.getRoot().getColor().a = 0f;
		stage.addAction(Actions.fadeIn(2.5f));
		
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla((float) 212 / 255, (float) 183 / 255, (float) 117 / 255);
        
        tiempo += delta;
        asignarEntradas();
        colorearSeleccion();
  
		if(entradas.isEnter()) {
			if(seleccion == 0) {Render.app.setScreen(new PantallaSeleccion());}
			else if(seleccion == 1) {mostrarOpciones = true;}
			else {Gdx.app.exit();}
		}
		
		if(mostrarOpciones) {
			//vo.mostrarOpciones(delta); hay que crear VentanaOpciones con Scene2D.
		}
		if(entradas.isEscape()) {
			mostrarOpciones = false;
		}
		
		stage.act();
		stage.draw();
	}

	private void colorearSeleccion() {
		botones[seleccion].setColor(Color.PURPLE);
		for (int i = 0; i < botones.length; i++) {
			if(i != seleccion) {
				botones[i].setColor(Color.BLACK);
			}
		}		
	}

	private void asignarEntradas() {
		if(entradas.isAbajo()) {
        	if(tiempo >= 0.2f) {
        		if(seleccion == 2) {
        			seleccion = 0;
        		}else {
        			seleccion++;
        		}
        		tiempo = 0;
        	}
        	
        }else if(entradas.isArriba()) {
        	if(tiempo >= 0.2f) {
        		if(seleccion == 0) {
        			seleccion = 2;
        		}else {
        			seleccion--;
        		}
        		tiempo = 0;
        	}
        	
        }
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
		
	}
	
}
