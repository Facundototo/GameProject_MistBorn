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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.bakpun.mistborn.elementos.SkinFreeTypeLoader;
import com.bakpun.mistborn.enums.Fuente;
import com.bakpun.mistborn.enums.TipoCliente;
import com.bakpun.mistborn.enums.TipoPersonaje;
import com.bakpun.mistborn.enums.TipoPoder;
import com.bakpun.mistborn.eventos.EventoCrearBarra;
import com.bakpun.mistborn.eventos.EventoGestionMonedas;
import com.bakpun.mistborn.eventos.EventoGestionPoderes;
import com.bakpun.mistborn.eventos.EventoReducirVida;
import com.bakpun.mistborn.eventos.EventoSetDuracionPeltre;
import com.bakpun.mistborn.eventos.EventoTerminaPartida;
import com.bakpun.mistborn.eventos.Listeners;
import com.bakpun.mistborn.utiles.Config;
import com.bakpun.mistborn.utiles.Recursos;
import com.bakpun.mistborn.utiles.Render;

public final class Hud implements EventoTerminaPartida,EventoCrearBarra,EventoReducirVida,EventoGestionMonedas,EventoSetDuracionPeltre,EventoGestionPoderes{

	private Skin skin;
	private Stage stage;
	private Table tabla;
	private Image marcoVida;
	private ArrayList<Image> marcosPoder;	//Lo creamos con el max de poderes que puede tener un pj pero lo limita el for de shapesPoder.
	private Label cantMonedas,tiempoPeltre,txtCentro,avisoSalir;
	private ShapeRenderer shapeVida;
	private ArrayList<ShapeRenderer> shapesPoder;	//Este arraylist porque no se sabe cuantos poderes se van a crear.
	private float[] energiaPoderes;
	private float vida,escalado = 1.5f,tiempoComienzo = 6f;
	private int monedas = 10;
	private boolean flagComenzo = false;
	
	public Hud() {
		Listeners.agregarListener(this);
		
		skin = SkinFreeTypeLoader.cargar();
		stage = new Stage();
		tabla = new Table();
		txtCentro = new Label("",Fuente.PIXELPELEA.getStyle(skin));
		avisoSalir = new Label(Render.bundle.get("hud.salir"),Fuente.PIXELPELEA.getStyle(skin));
		shapeVida = new ShapeRenderer();
		shapesPoder = new ArrayList<ShapeRenderer>();
		marcosPoder = new ArrayList<Image>();
		
		stage.setViewport(new FillViewport(Config.ANCHO,Config.ALTO,stage.getCamera()));
		
		shapeVida.setColor(Color.RED);
				
		tabla.setFillParent(true);		//Con esto le digo que la tabla ocupe toda la pantalla.
		
		marcoVida = new Image(new Texture(Recursos.MARCO_VIDA));
		
		tabla.top().left().pad(30);		//Le pongo un padding de 30 px.
		tabla.add(marcoVida).size(marcoVida.getWidth()*escalado, marcoVida.getHeight()*escalado).row();
		
		this.vida = 245;
		
		txtCentro.setPosition(Config.ANCHO/2, Config.ALTO/2,Align.center);
		
		stage.addActor(txtCentro);
		stage.addActor(tabla);
		
	}
	
	public void draw(float delta) {
		if(!flagComenzo) {empezarPelea(delta);}
		drawVida();
		drawPoderes();
		
		stage.act(delta);	//Dibujo el HUD.
		stage.draw();
	}
	
	private void empezarPelea(float delta) {
		this.tiempoComienzo -= delta;
		if((int)tiempoComienzo >= 1) {txtCentro.setText((int)tiempoComienzo);}
		else if(tiempoComienzo >= -0.2f) {txtCentro.setText(Render.bundle.get("hud.empezar"));}
		else {
			this.flagComenzo = true;
			txtCentro.setVisible(false);
			Listeners.empezarPartida();
		}
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
			shapesPoder.get(i).rect(marcosPoder.get(i).getX()+10*escalado, marcosPoder.get(i).getY(), energiaPoderes[i], marcosPoder.get(i).getHeight());
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
	
		int  _index = shapesPoder.size()-1;
		
		
		marcosPoder.add(new Image(new Texture(ruta)));
		tabla.add(marcosPoder.get(_index)).size(marcosPoder.get(_index).getWidth()*escalado, marcosPoder.get(_index).getHeight()*escalado).padTop(10).left();
		
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
	public void reducirVida(float dano,TipoCliente cliente) {
		if(cliente == TipoCliente.USUARIO) {
			vida -= (dano/100)*245;
		}
	}

	@Override
	public void restarMonedas() {
		cantMonedas.setText(String.valueOf(--monedas));
	}
	@Override
	public void aumentarMonedas() {
		cantMonedas.setText(String.valueOf(++monedas));
	}

	@Override
	public void setDuracion(int segundo) {
		this.tiempoPeltre.setText(String.valueOf(segundo));
	}

	@Override
	public void reducirPoder(TipoPersonaje tipoPj, TipoPoder tipoPoder,float energia) {
		//Este if ternario porque si es nacido de la bruma el array energiaPoderes tiene mas indices y varian.
		energiaPoderes[(tipoPj == TipoPersonaje.NACIDO_BRUMA)?tipoPoder.getNroSeleccion():0] -= (energia/100)*210;
	}
	
	
	@Override
	public void aumentarPoder(TipoPersonaje tipoPj,TipoPoder tipoPoder, float energia) {
		energiaPoderes[(tipoPj == TipoPersonaje.NACIDO_BRUMA)?tipoPoder.getNroSeleccion():0] += (energia/100)*210;
	}

	@Override
	public void terminarPartida(String texto,TipoCliente ganador) {
		if(texto.equals("Perdiste")) {
			this.vida = 0;
		}
		txtCentro.setText(texto);
		txtCentro.setVisible(true);
		
		avisoSalir.setPosition(Config.ANCHO/2, Config.ALTO/2.5f,Align.center);
		
		stage.addActor(avisoSalir);
		
	}
	
	
}
