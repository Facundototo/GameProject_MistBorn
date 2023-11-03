package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.bakpun.mistborn.enums.TipoAudio;
import com.bakpun.mistborn.utiles.Recursos;

public class Audio {
	//Clase para organizar todos los sonidos del videojuego.
	
	public Sound sonidoMenu,sonidoSeleccion,seleccionElegida,pjCorriendo;
	public Music cancionMenu;
	private float volumenMusica = 0.5f,volumenSonido = 0.5f;	//Volumenes iniciales
	
	public Audio() {
		seleccionElegida = Gdx.audio.newSound(Gdx.files.internal(Recursos.SELECCION_ELEGIDA));
		sonidoSeleccion = Gdx.audio.newSound(Gdx.files.internal(Recursos.SONIDO_SELECCION));
		cancionMenu = Gdx.audio.newMusic(Gdx.files.internal(Recursos.CANCION_MENU));
		sonidoMenu = Gdx.audio.newSound(Gdx.files.internal(Recursos.SONIDO_OPCION_MENU));
		pjCorriendo = Gdx.audio.newSound(Gdx.files.internal(Recursos.SONIDO_PJ_CORRIENDO));
	}
	
	public void setVolumen(float volumen,TipoAudio tipo) {
		if(tipo == TipoAudio.MUSICA) {
			this.volumenMusica = (volumen/100);		//Lo divido por cien porque el volumen va del 0 al 1.
		}else {
			this.volumenSonido = (volumen/100);
		}
		cancionMenu.setVolume(this.volumenMusica);
	}
	
	public void dispose() {
		sonidoMenu.dispose();
		sonidoSeleccion.dispose();
		seleccionElegida.dispose();
		pjCorriendo.dispose();
		cancionMenu.dispose();
	}
	
	public float getVolumen(TipoAudio tipo) {
		return (tipo == TipoAudio.SONIDO)?this.volumenSonido:this.volumenMusica;	
	}
}
