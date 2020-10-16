package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;

/**
 * This is a generator for contacts between a circle and a box
 */
public class CircleBoxContactGenerator implements ContactGenerator{

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		Circle circleA = (Circle) shapePair.getShapeA();
		Box boxB = (Box) shapePair.getShapeB();
		
		Vec2f boxBMin = new Vec2f(boxB.getMin());
		Vec2f boxBMax = new Vec2f(boxB.getMax());

		Vec2f relPos = new Vec2f(boxB.getAbsPos()).sub(circleA.getAbsPos());
		float overlapX = Math.abs(relPos.getX()) - boxB.getRad().getX();
		float overlapY = Math.abs(relPos.getY()) - boxB.getRad().getX();
		float smallestOverlap = overlapX > overlapY ? overlapX : overlapY;

		Vec2f normal = new Vec2f();
		Vec2f closestPoint = new Vec2f();
		float d;
		if (smallestOverlap < 0) {
			// Penetration
			closestPoint.set(boxB.getAbsPos());
			if (overlapX > overlapY) {
				normal.set(Scalar.sign(-relPos.getX()), 0);
			} else {
				normal.set(0, Scalar.sign(-relPos.getY()));
			}
			d = smallestOverlap - circleA.getRad();
			closestPoint.set(circleA.getAbsPos()).addMult(normal, -smallestOverlap);
		} else {
			// Separation
			closestPoint.set(circleA.getAbsPos());
			closestPoint.setX(Scalar.clamp(closestPoint.getX(), boxBMin.getX(), boxBMax.getX()));
			closestPoint.setY(Scalar.clamp(closestPoint.getY(), boxBMin.getY(), boxBMax.getY()));

			Vec2f distanceToClosest = new Vec2f(circleA.getAbsPos()).sub(closestPoint);
			normal.set(distanceToClosest).normalize();

			d = distanceToClosest.dot(normal) - circleA.getRad();

		}
		
		Contact newContact = new Contact(new Vec2f(normal).mult(-1), d, closestPoint.addMult(normal, d));
		if(acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}
	}

}
