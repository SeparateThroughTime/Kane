package kane.physics;

import kane.math.Vec2f;
import kane.physics.contacts.Contact;

public class ShapePair {

	private Shape shapeA;
	private Shape shapeB;
	private final Contact[] contacts;
	private int numContacts;
	private final int MAX_CONTACTS = 2;

	public ShapePair(Shape shapeA, Shape shapeB) {
		this.shapeA = shapeA;
		this.shapeB = shapeB;
		contacts = new Contact[MAX_CONTACTS];
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

	public Contact[] getContacts() {
		return contacts;
	}

	public int getNumContacts() {
		return numContacts;
	}

	public boolean addContact(Contact contact) {
		if (numContacts < MAX_CONTACTS) {
			contacts[numContacts++] = contact;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void zeroContacts() {
		numContacts = 0;
	}

	public int getMAX_CONTACTS() {
		return MAX_CONTACTS;
	}
}
