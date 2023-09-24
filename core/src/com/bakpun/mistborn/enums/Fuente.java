package com.bakpun.mistborn.enums;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public enum Fuente {

	//Fuentes de texto que se usaran en el juego.
	PIXELINFO("fontpixel-info-white"),PIXELTEXTO("pixelfont-white");
	
	private String nombreFuente;
	
	Fuente(String nombreFuente) {
		this.nombreFuente = nombreFuente;
	}
	
	//Retorno el LabelStyle, es decir cargo una fuente determinada.
	public LabelStyle getStyle(Skin skin) {
		return new Label.LabelStyle(skin.getFont(this.nombreFuente),Color.WHITE);
	}
	
}
