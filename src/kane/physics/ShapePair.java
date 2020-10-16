package kane.physics;

import kane.physics.contacts.Contact;

/**
 * A shapePair is a pair of shapes with a potential collision.
 */
public class ShapePair {

	private Shape shapeA;
	private Shape shapeB;
	private Contact contact;
	private boolean penetraion;
	private boolean collideable;

	public ShapePair(Shape shapeA, Shape shapeB) {
		this.shapeA = shapeA;
		this.shapeB = shapeB;
		contact = null;
		setCollideable(true);
	}

	/**
	 * flip the Shapes so shapeA is shapeB and shapeB is shapeA.
	 */
	public void flipShapes() {
		Shape tmp = shapeA;
		shapeA = shapeB;
		shapeB = tmp;
	}

	/**
	 * Get first Shape.
	 * @return
	 */
	public Shape getShapeA() {
		return shapeA;
	}

	/**
	 * Get second Shape.
	 * @return
	 */
	public Shape getShapeB() {
		return shapeB;
	}

	/**
	 * Get contact.
	 * @return
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Set contact.
	 * @param contact
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * set penetration.
	 * @param b
	 */
	public void setPenetration(boolean b) {
		penetraion = b;
	}

	/**
	 * Check if pairs are penetrating.
	 * @return
	 */
	public boolean isPenetration() {
		return penetraion;
	}

	/**
	 * Check if pair is collideable.
	 * @return
	 */
	public boolean isCollideable() {
		return collideable;
	}

	/**
	 * set collideable.
	 * @param collideable
	 */
	public void setCollideable(boolean collideable) {
		this.collideable = collideable;
	}

	@Override
	public String toString() {
		return "Body A: " + shapeA.body + ", Shape A: " + shapeA + ", Body B: " + shapeB.body + ", Shape B: " + shapeB;
	}
}
