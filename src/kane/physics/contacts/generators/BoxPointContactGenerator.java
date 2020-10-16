package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

/**
 * This is a generator for contacts between a box and a point
 */
public class BoxPointContactGenerator implements ContactGenerator {

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		Box boxA = (Box) shapePair.getShapeA();
		Point pointB = (Point) shapePair.getShapeB();
		Vec2f radius = new Vec2f(boxA.getRad());
		Vec2f min = new Vec2f(boxA.getMin());
		Vec2f max = new Vec2f(boxA.getMax());

		// Get closest Point on boxA
		Vec2f closestPointA = new Vec2f(pointB.getAbsPos());
		closestPointA.setX(Scalar.clamp(closestPointA.getX(), min.getX(), max.getX()));
		closestPointA.setY(Scalar.clamp(closestPointA.getY(), min.getY(), max.getY()));

		// get Normal with difference of Pos of boxes
		Vec2f relPos = new Vec2f(pointB.getAbsPos()).sub(boxA.getAbsPos());
		float overlapX = Math.abs(relPos.getX()) - radius.getX();
		float overlapY = Math.abs(relPos.getY()) - radius.getY();
		float overlap = 0f;
		Vec2f normal = new Vec2f();
		if (overlapX > overlapY) {
			overlap = overlapX;
			normal.set(Scalar.sign(relPos.getX()), 0).normalize();
		} else {
			overlap = overlapY;
			normal.set(0, Scalar.sign(relPos.getY())).normalize();
		}

		Vec2f closestPointB = new Vec2f(pointB.getAbsPos());

		Contact newContact = new Contact(normal, overlap, closestPointB, new Vec2f(), new Vec2f());
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}
	}

}
