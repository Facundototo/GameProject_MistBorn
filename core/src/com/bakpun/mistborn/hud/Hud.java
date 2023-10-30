package com.bakpun.mistborn.hud;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.elementos.SkinFreeTypeLoader;
import com.bakpun.mistborn.enums.Fuente;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.EventoCrearBarra;
import com.bakpun.mistborn.eventos.EventoReducirPoder;
import com.bakpun.mistborn.eventos.EventoReducirVida;
import com.bakpun.mistborn.eventos.EventoRestarMonedas;
import com.bakpun.mistborn.eventos.EventoSetDuracionPeltre;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;

public final class Hud implements EventoCrearBarra,EventoReducirVida,EventoRestarMonedas,EventoSetDuracionPeltre,EventoReducirPoder{

	private Skin skin;
	private Stage stage;
	private Table tabla;
	private Image marcoVida;
	private Image[] marcosPoder = new Image[3];		//Lo creamos con el max de poderes que puede tener un pj pero lo limita el for de shapesPoder.
	private Label cantMonedas,tiempoPeltre;
	private ShapeRenderer shapeVida;
	private ArrayList<ShapeRenderer> shapesPoder;	//Este arraylist porque no se sabe cuantos poderes se van a crear.
	private float[] energiaPoderes;
	private float vida,escalado = 1.5f;
	private int monedas = 10;
	
	public Hud() {
		Listeners.agregarListener(this);
		
		skin = SkinFreeTypeLoader.cargar();
		stage = new Stage();
		tabla = new Table();
		shapeVida = new ShapeRenderer();
		shapesPoder = new ArrayList<ShapeRenderer>();
		
		stage.setViewport(new FillViewport(Config.ANCHO,Config.ALTO,stage.getCamera()));
		
		shapeVida.setColor(Color.RED);
				
		tabla.setFillParent(true);		//Con esto le digo que la tabla ocupe toda la pantalla.
		marcoVida = new Image(new Texture(Recursos.MARCO_VIDA));
		
		tabla.top().left().pad(30);		//Le pongo un padding de 30 px.
		tabla.add(marcoVida).size(marcoVida.getWidth()*escalado, marcoVida.getHeight()*escalado).row();
		this.vida = 245;
		stage.addActor(tabla);
	}
	
	public void draw(float delta) {
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
		shapeVida.rect(50*escalado, marcoVida.getY(), vida, marcoVida.getHeight());
		shapeVida.end();
	}
	
	public void drawPoderes() {
		for (int i = 0; i < shapesPoder.size(); i++) {
			shapesPoder.get(i).setProjectionMatrix(stage.getCamera().combined);
			shapesPoder.get(i).begin(ShapeType.Filled);
			shapesPoder.get(i).rect(marcosPoder[i].getX()+10*escalado, marcosPoder[i].getY(), energiaPoderes[i], marcosPoder[i].getHeight());
			shapesPoder.get(i).end();
		}
		if(tiempoPeltre != null) {
			tiempoPeltre.setVisible((tiempoPeltre.getText().contains("0")?false:true));	//Si es cero desaparece el contador.
		}
		
	}
	
	
	@Override
	public void crearBarra(String ruta,Color color,TipoPoder tipo) {
		shapesPoder.add(new ShapeRenderer());
		shapesPoder.get(shapesPoder.size()-1).setColor(color);
		
		final int  _index = shapesPoder.size()-1;
		
		marcosPoder[_index] = new Image(new Texture(ruta));
		tabla.add(marcosPoder[_index]).size(marcosPoder[_index].getWidth()*escalado, marcosPoder[_index].getHeight()*escalado).padTop(10).left();
		
		if(tipo == TipoPoder.ACERO) {		//Para que se agregue al lado de la barra de acero el contador de monedas que tiene el pj.
			cantMonedas = new Label(String.valueOf(monedas),Fuente.PIXELTEXTO.getStyle(skin));
			tabla.add(cantMonedas).right();
		}else if(tipo == TipoPoder.PELTRE) {
			tiempoPeltre = new Label(String.valueOf(0),Fuente.PIXELTEXTO.getStyle(skin));
			//Aca se hace la secuencia del fade, aplicandole un delay para que funcione.
			tiempoPeltre.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(0.3f),Actions.fadeOut(0.3f),Actions.delay(0.2f))));
			tabla.add(tiempoPeltre).right();
		}
		tabla.row();
		
		energiaPoderes = new float[shapesPoder.size()];	
		for (int i = 0; i < energiaPoderes.length; i++) {
			energiaPoderes[i] = 240f;
		}
	}
	@Override
	public void reducirVida(float dano) {
		vida -= (dano/100)*245;
	}

	@Override
	public void restarMonedas() {
		cantMonedas.setText(String.valueOf(--monedas));
	}

	@Override
	public void setDuracion(int segundo) {
		this.tiempoPeltre.setText(String.valueOf(segundo));
	}

	@Override
	public void reducirPoder(TipoPersonaje tipoPj, TipoPoder tipoPoder,float energia) {
		if(tipoPj == TipoPersonaje.NACIDO_BRUMA) {
			energiaPoderes[tipoPoder.getNroSeleccion()] -= (energia/100)*210;
		}else {
			energiaPoderes[0] -= (energia/100)*210;
		}
	}
	
	
}
