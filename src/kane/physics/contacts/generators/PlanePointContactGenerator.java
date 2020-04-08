package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Circle;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Point;

public class PlanePointContactGenerator implements ContactGenerator {
	// This is generates the Contacts between a Plane and a Circle.

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		Plane planeA = (Plane) shapePair.getShapeA();
		Point pointB = (Point) shapePair.getShapeB();
		Vec2f pointOnPlane = planeA.getPoint();
		Vec2f distanceToPlane = new Vec2f(pointOnPlane).sub(pointB.getAbsPos());
		float d = distanceToPlane.dot(planeA.getNormal());
		Vec2f closestPointOnPlane = new Vec2f(pointB.getAbsPos()).addMult(planeA.getNormal(), d);
		
		Contact newContact = new Contact(planeA.getNormal(), -d, closestPointOnPlane, new Vec2f(), pointB.getAbsPos());
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}
	}
}
