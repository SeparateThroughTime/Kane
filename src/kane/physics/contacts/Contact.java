package kane.physics.contacts;

import kane.math.Vec2f;

/**
 * A contact saves all important information of the relation between to bodies.
 */
public class Contact {
	
	private final Vec2f normal;
	private final float distance;
	private final Vec2f point;
	private final Vec2f supportA;
	private final Vec2f supportB;
	
	private float impulse;
	
	/**
	 * @param normal -direction of contact
	 * @param distance -distance between bodies
	 * @param point -position of contact
	 */
	public Contact(Vec2f normal, float distance, Vec2f point) {
		this.normal = new Vec2f(normal);
		this.distance = distance;
		this.point = new Vec2f(point);
		this.supportA = new Vec2f();
		this.supportB = new Vec2f();
		setImpulse(0f);
	}
	
	/**
	 * Get normal
	 * @return -normal
	 */
	public Vec2f getNormal() {
		return normal;
	}
	
	/**
	 * Get distance
	 * @return -distance
	 */
	public float getDistance() {
		return distance;
	}
	
	/**
	 * Get point
	 * @return -point
	 */
	public Vec2f getPoint() {
		return point;
	}

	/**
	 * Get Support point A
	 * @return -supportA
	 */
	public Vec2f getSupportA() {
		return supportA;
	}

	/**
	 * Get Support point B
	 * @return -supportB
	 */
	public Vec2f getSupportB() {
		return supportB;
	}

	public float getImpulse() {
		return impulse;
	}

	public void setImpulse(float impulse) {
		this.impulse = impulse;
	}
	
}
