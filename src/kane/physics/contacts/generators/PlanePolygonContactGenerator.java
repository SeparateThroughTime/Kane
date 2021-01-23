package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Polygon;

/**
 * This is a generator for contacts between a plane and a polygon
 */
public class PlanePolygonContactGenerator implements ContactGenerator {

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		Plane planeA = (Plane) shapePair.getShapeA();
		Polygon poliB = (Polygon) shapePair.getShapeB();

		final int NUM_POINTS_B = poliB.getNumPoints();
		Vec2f[] points = new Vec2f[NUM_POINTS_B];
		for (int i = 0; i < NUM_POINTS_B; i++) {
			points[i] = new Vec2f(poliB.getPoint(i)).add(poliB.getAbsPos());
		}

		Vec2f pointOnPlane = planeA.getPoint();

		// get nearest Point of poli
		Vec2f point = new Vec2f();
		float projDistance = 0;
		Vec2f supportB = new Vec2f();
		float greatestD = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < NUM_POINTS_B; i++) {
			Vec2f pointi = points[i];
			Vec2f distanceToPlanei = new Vec2f(pointOnPlane).sub(pointi);
			float projDistancei = distanceToPlanei.dot(planeA.getNormal());
			if (Scalar.greaterEqual(projDistancei, projDistance) || i == 0) {
				if (Scalar.equals(projDistancei, projDistance) && i != 0) {
					// if two edges collide
					point = new Vec2f(point).add(pointi).mult(0.5f);
				} else {
					projDistance = projDistancei;
					point = pointi;
				}
			}
			
			float d = point.dot(planeA.getNormal());
			if (d > greatestD) {
				greatestD = d;
				supportB = point;
			}
		}
		
		
		
		Vec2f closestPointOnPlane = new Vec2f(point).addMult(planeA.getNormal(), projDistance);

		Contact newContact = new Contact(planeA.getNormal(), -projDistance, closestPointOnPlane);
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}

	}

}
