package kane.physics.contacts;

import kane.math.Vec2f;
import kane.physics.Body;

public class Contact {
//A contact saves all important information of the relation between to bodies.
	
	private final Vec2f normal;
	private final float distance;
	private final Vec2f point;
	private float impulse;
	private float perpImpulse;
	private float frictionImpulse;
	private final Vec2f supportA;
	private final Vec2f suppertB;
	
	public Contact(Vec2f normal, float distance, Vec2f point, Vec2f supportA, Vec2f supportB) {
		this.normal = new Vec2f(normal);
		this.distance = distance;
		this.point = new Vec2f(point);
		this.supportA = supportA;
		this.suppertB = supportB;
	}
	
	//TODO choose this or the other for rotation solution
	public Contact(Vec2f normal, float distance, Vec2f point) {
		this.normal = new Vec2f(normal);
		this.distance = distance;
		this.point = new Vec2f(point);
		this.supportA = new Vec2f();
		this.suppertB = new Vec2f();
	}
	
	public Vec2f getNormal() {
		return normal;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public Vec2f getPoint() {
		return point;
	}

	public float getImpulse() {
		return impulse;
	}

	public float setImpulse(float impulse) {
		this.impulse = impulse;
		return impulse;
	}

	public float getFrictionImpulse() {
		return frictionImpulse;
	}

	public void setFrictionImpulse(float frictionImpulse) {
		this.frictionImpulse = frictionImpulse;
	}

	public Vec2f getSupportA() {
		return supportA;
	}

	public Vec2f getSuppertB() {
		return suppertB;
	}

	public float getPerpImpulse() {
		return perpImpulse;
	}

	public void setPerpImpulse(float perpImpulse) {
		this.perpImpulse = perpImpulse;
	}
	
}
