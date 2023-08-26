package com.bakpun.mistborn.io;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.bakpun.mistborn.utiles.Config;

public class Entradas implements InputProcessor{

	private boolean abajo,arriba,irDer,irIzq,saltar,mouseClick,enter,escape;
	private int mouseX=0,mouseY=0;
	
	public boolean keyDown(int keycode) {
		
		if(keycode == Keys.D || keycode == Keys.RIGHT) {
			irDer = true;
		}
		if(keycode == Keys.A || keycode == Keys.LEFT) {
			irIzq = true;
		}
		if(keycode == Keys.SPACE) {
			saltar = true;
		}
		if(keycode == Keys.DOWN) {
			abajo = true;
		}
		if(keycode == Keys.UP) {
			arriba = true;
		}
		if(keycode == Keys.ENTER) {
			enter = true;
		}
		if(keycode == Keys.ESCAPE) {
			escape = true;
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		if(keycode == Keys.D || keycode == Keys.RIGHT) {
			irDer = false;
		}
		if(keycode == Keys.A || keycode == Keys.LEFT) {
			irIzq = false;
		}
		if(keycode == Keys.SPACE) {
			saltar = false;
		}
		if(keycode == Keys.DOWN) {
			abajo = false;
		}
		if(keycode == Keys.UP) {
			arriba = false;
		}
		if(keycode == Keys.ENTER) {
			enter = false;
		}
		if(keycode == Keys.ESCAPE) {
			escape = false;
		}
		return false;
	}
	

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseClick = true;
		return false;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseClick = false;
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouseX = screenX;
		mouseY = Config.ALTO - screenY;
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isIrDer() {
		return this.irDer;
	}
	public boolean isIrIzq() {
		return this.irIzq;
	}
	public boolean isEspacio() {
		return this.saltar;
	}
	public boolean isAbajo() {
		return this.abajo;
	}
	public boolean isArriba() {
		return this.arriba;
	}
	public boolean isEnter() {
		return this.enter;
	}
	public boolean isEscape() {
		return this.escape;
	}
	public boolean isMouseClick() {
		return this.mouseClick;
	}
	
	public int getMouseX() {
		return this.mouseX;
	}
	public int getMouseY() {
		return this.mouseY;
	}

}
