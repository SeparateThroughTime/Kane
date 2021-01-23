package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;

/**
 * This is a generator for contacts between two boxes
 */
public class BoxBoxContactGenerator implements ContactGenerator {

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {

		Box boxA = (Box) shapePair.getShapeA();
		Box boxB = (Box) shapePair.getShapeB();

		// Get Minkowski Box
		Vec2f bothRadius = new Vec2f(boxA.getRad()).add(boxB.getRad());

		Vec2f min = new Vec2f(boxA.getMin());
		Vec2f max = new Vec2f(boxA.getMax());

		// Get closest Point on boxA
		Vec2f closestPointA = new Vec2f(boxB.getAbsPos());
		closestPointA.setX(Scalar.clamp(closestPointA.getX(), min.getX(), max.getX()));
		closestPointA.setY(Scalar.clamp(closestPointA.getY(), min.getY(), max.getY()));

		// getNormal with difference of Pos of boxes
		Vec2f relPos = new Vec2f(boxB.getAbsPos()).sub(boxA.getAbsPos());
		float overlapX = Math.abs(relPos.getX()) - bothRadius.getX();
		float overlapY = Math.abs(relPos.getY()) - bothRadius.getY();
		float overlap = 0f;
		Vec2f normal = new Vec2f();
		if (overlapX > overlapY) {
			overlap = overlapX;
			normal.set(Scalar.sign(relPos.getX()), 0).normalize();
		} else {
			overlap = overlapY;
			normal.set(0, Scalar.sign(relPos.getY())).normalize();
		}

		Vec2f perp = new Vec2f(normal).perpRight();

		Vec2f posTmp = new Vec2f(boxB.getAbsPos()).addMult(boxB.getRad(), new Vec2f(normal).negate());
		Vec2f closestPointB = Scalar.findIntersection(closestPointA, normal, posTmp, perp);

		Contact newContact = new Contact(normal, overlap, closestPointB);
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}

	}

}
