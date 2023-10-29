package com.bakpun.mistborn.elementos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bakpun.mistborn.box2d.Box2dConfig;
import com.bakpun.mistborn.box2d.Fisica;
import com.bakpun.mistborn.enums.UserData;

public class ObjetoMetalico {
	
	private Fisica f;
	private Body plataforma;
	
	
	public ObjetoMetalico(World mundo,Vector2 pos,int angulo) {
		
		//Creo el body.
		f = new Fisica();									
		f.setBody(BodyType.KinematicBody, pos,angulo);
		f.createPolygon(5/Box2dConfig.PPM,100/Box2dConfig.PPM);
		f.setFixture(f.getPolygon(), 4, 0.5f, 0);
		plataforma = mundo.createBody(f.getBody());
		plataforma.createFixture(f.getFixture());
		plataforma.setUserData(UserData.METAL);
	}
	
	
	
}
