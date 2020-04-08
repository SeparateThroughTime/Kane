package kane.physics;

import kane.math.Vec2f;
import kane.physics.contacts.Contact;

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

	public void flipShapes() {
		Shape tmp = shapeA;
		shapeA = shapeB;
		shapeB = tmp;
	}

	public Shape getShapeA() {
		return shapeA;
	}

	public Shape getShapeB() {
		return shapeB;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public void setPenetration(boolean b) {
		penetraion = b;
	}

	public boolean isPenetration() {
		return penetraion;
	}

	public boolean isCollideable() {
		return collideable;
	}

	public void setCollideable(boolean collideable) {
		this.collideable = collideable;
	}
}
