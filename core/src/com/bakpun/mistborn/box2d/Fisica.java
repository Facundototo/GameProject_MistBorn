package com.bakpun.mistborn.box2d;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//Clase para la creacion de Body, hay que tener en cuenta que hay que llamar en orden a las funciones.

public class Fisica {

	private BodyDef bd;
	private FixtureDef fd;
	private PolygonShape polygon;
	private ChainShape chain;
	private CircleShape circle;
	
	
	public Fisica() {
		bd = new BodyDef();
		fd = new FixtureDef();				
		polygon = new PolygonShape();
		circle = new CircleShape();
	}
	
	public void setBody(BodyType type,Vector2 position) {		//Asigna el tipo de body y la posicion.
		bd.type = type;	
		bd.position.set(position);
	}
	
	public void setBody(BodyType type,Vector2 position,int angulo) {		//Sobrecarga para ponerle un angulo.
		bd.type = type;	
		bd.angle = angulo * MathUtils.degreesToRadians;		//Transforma los grados a radianes.
		bd.position.set(position);
	}
	
	public void setFixture(Shape shape,float density,float friction, float restitution) {	//Asigna las caracteristicas del body.
		fd.shape = shape;
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
	}
	
	public void createPolygon(float width, float height) {	//Crea un cuadrado/rectangulo.
		polygon.setAsBox(width, height);
	}
	
	public void createCircle(float radius) {	//Crea un circulo.
		circle.setRadius(radius);
	}
	public void createChain(Vector2 verticeA,Vector2 verticeB) {	//Crea una linea.
		chain = new ChainShape();
		chain.createChain(new Vector2[] {verticeA,verticeB});
	}
	
	public void createBody(World world) {	//Crea el body con todo lo anterior.
		world.createBody(bd).createFixture(fd);
	}
	
	public Shape getPolygon() {		
		return this.polygon;
	}
	public Shape getCircle() {
		return this.circle;
	}
	public Shape getChain() {
		return this.chain;
	}
	public BodyDef getBody() {		//Estos 2 getters para cuando se quiera crear un Body. (pj).
		return this.bd;
	}
	public FixtureDef getFixture() {
		return this.fd;
	}

	public void dispose() {
		this.circle.dispose();
		this.chain.dispose();
		this.polygon.dispose();	
	}
 
	
	
}
