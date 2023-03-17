package kane.physics;

import kane.physics.contacts.Contact;

/**
 * A shapePair is a pair of shapes with a potential collision.
 */
public class ShapePair {

	public Shape shapeA;
	public Shape shapeB;
	public Contact contact;
	public boolean penetration;
	public boolean collideable;

	public ShapePair(Shape shapeA, Shape shapeB) {
		this.shapeA = shapeA;
		this.shapeB = shapeB;
		contact = null;
		collideable = true;
		penetration = false;
	}

	/**
	 * flip the Shapes so shapeA is shapeB and shapeB is shapeA.
	 */
	public void flipShapes() {
		Shape tmp = shapeA;
		shapeA = shapeB;
		shapeB = tmp;
	}

	@Override
	public String toString() {
		return "Body A: " + shapeA.body + ", Shape A: " + shapeA + ", Body B: " + shapeB.body + ", Shape B: " + shapeB;
	}
}
