package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;
import kane.physics.shapes.Plane;

/**
 * This is a generator for contacts between a plane and a box
 */
public class PlaneBoxContactGenerator implements ContactGenerator{

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		Plane planeA = (Plane) shapePair.getShapeA();
		Box boxB = (Box) shapePair.getShapeB();
		
		Vec2f[] points = new Vec2f[4];
		points[0] = new Vec2f(boxB.getMin());
		points[1] = new Vec2f(boxB.getMax());
		points[2] = new Vec2f(points[0].getX(), points[1].getY());
		points[3] = new Vec2f(points[1].getX(), points[0].getY());
		Vec2f pointOnPlane = planeA.getPoint();
		
		//get nearest Point of Box
		Vec2f point = new Vec2f();
		float projDistance = 0;
		for (int i = 0; i < 4; i++) {
			Vec2f pointi = points[i];
			Vec2f distanceToPlanei = new Vec2f(pointOnPlane).sub(pointi);
			float projDistancei = distanceToPlanei.dot(planeA.getNormal());
			if (projDistancei > projDistance || i == 0) {
				projDistance = projDistancei;
				point = pointi;
			}
		}
		Vec2f closestPointOnPlane = new Vec2f(point).addMult(planeA.getNormal(), projDistance);
		
		Contact newContact = new Contact(planeA.getNormal(), -projDistance, closestPointOnPlane, new Vec2f(), new Vec2f());
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}
		
	}

}
