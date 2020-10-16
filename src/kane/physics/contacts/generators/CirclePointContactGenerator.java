package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Circle;
import kane.physics.shapes.Point;

/**
 * This is a generator for contacts between a circle and a point
 */
public class CirclePointContactGenerator implements ContactGenerator {
//This is generates the Contacts between to Circles.

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		// Contact generation Circle-Circle
		Circle circleA = (Circle) shapePair.getShapeA();
		Point pointB = (Point) shapePair.getShapeB();
		Vec2f distanceBetween = new Vec2f(pointB.getAbsPos()).sub(circleA.getAbsPos());
		Vec2f normal = new Vec2f(distanceBetween).normalize();
		float projDistance = distanceBetween.dot(normal);
		float radius = circleA.getRad();
		float d = radius - projDistance;
		Vec2f closestPointOnA = new Vec2f(circleA.getAbsPos()).addMult(normal, circleA.getRad());

		Contact newContact = new Contact(normal, -d, closestPointOnA);
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}
	}
}
