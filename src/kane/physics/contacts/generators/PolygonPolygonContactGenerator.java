package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.contacts.GeneratorFunctions;
import kane.physics.shapes.Polygon;

public class PolygonPolygonContactGenerator implements ContactGenerator{

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		Polygon poliA = (Polygon) shapePair.getShapeA();
		Polygon poliB = (Polygon) shapePair.getShapeB();

		// Declarations
		Vec2f poliAAbsPos = poliA.getAbsPos();
		Vec2f poliBAbsPos = poliB.getAbsPos();
		Vec2f relPos = poliB.getAbsPos().sub(poliA.getAbsPos());

		final int NUM_POINTS_A = poliA.getNumPoints();
		final int NUM_POINTS_B = poliB.getNumPoints();
		Vec2f[] poliAPoints = new Vec2f[NUM_POINTS_A];
		for (int i = 0; i < NUM_POINTS_A; i++) {
			poliAPoints[i] = new Vec2f(poliA.getPoint(i)).add(poliAAbsPos);
		}

		Vec2f[] poliBPoints = new Vec2f[NUM_POINTS_B];
		for (int i = 0; i < NUM_POINTS_B; i++) {
			poliBPoints[i] = new Vec2f(poliB.getPoint(i)).add(poliBAbsPos);
		}

		Vec2f[] poliADirs = new Vec2f[NUM_POINTS_A];
		Vec2f[] poliADirsPerps = new Vec2f[NUM_POINTS_A];
		for (int i = 0; i < NUM_POINTS_A; i++) {
			int j = i < NUM_POINTS_A - 1 ? i + 1 : 0;
			poliADirs[i] = new Vec2f(poliAPoints[j]).sub(poliAPoints[i]).normalize();
			poliADirsPerps[i] = new Vec2f(poliADirs[i]).perpRight();
		}

		Vec2f[] poliBDirs = new Vec2f[NUM_POINTS_B];
		Vec2f[] poliBDirsPerps = new Vec2f[NUM_POINTS_B];
		for (int i = 0; i < NUM_POINTS_B; i++) {
			int j = i < NUM_POINTS_B - 1 ? i + 1 : 0;
			poliBDirs[i] = new Vec2f(poliBPoints[j]).sub(poliBPoints[i]).normalize();
			poliBDirsPerps[i] = new Vec2f(poliBDirs[i]).perpRight();
		}

		float nearestIntervalD = Float.NEGATIVE_INFINITY;
		Vec2f normal = new Vec2f();

		// Check if there is a separation and get normal
		for (int i = 0; i < NUM_POINTS_A + NUM_POINTS_B; i++) {
			Vec2f perp;
			if (i < NUM_POINTS_A) {
				perp = poliADirsPerps[i];
			} else {
				perp = poliBDirsPerps[i - NUM_POINTS_A];
			}

			float[] minMaxA = GeneratorFunctions.projectPolygon(perp, poliAPoints);
			float[] minMaxB = GeneratorFunctions.projectPolygon(perp, poliBPoints);
			float d = GeneratorFunctions.intervalDist(minMaxA, minMaxB);

			if (d > nearestIntervalD) {
				nearestIntervalD = d;
				normal = perp;
			}

			if (d > 0) {
				break;
			}
		}

		// invert normal if its upside down
		if (relPos.dot(normal) < 0) {
			normal.mult(-1f);
		}

		// Get contact point A
		Vec2f thirdPoint = new Vec2f();
		Vec2f nearestPointA = new Vec2f();
		Vec2f supportA = new Vec2f();
		float smallestD = Float.POSITIVE_INFINITY;
		float greatestD = Float.NEGATIVE_INFINITY;
		boolean lerpA = false;
		for (int i = 0; i < NUM_POINTS_A; i++) {
			Vec2f point = poliAPoints[i];

			float d = new Vec2f(normal).mult(-1).dot(point);
			if(d > greatestD) {
				greatestD = d;
				supportA = point;
			}
			if (d < smallestD) {
				smallestD = d;
				nearestPointA = point;
			} else if (d == smallestD) {
				thirdPoint = point;
				lerpA = true;
			}
		}
		// Get contact point B
		Vec2f nearestPointB = new Vec2f();
		Vec2f supportB = new Vec2f();
		smallestD = Float.POSITIVE_INFINITY;
		greatestD = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < NUM_POINTS_B; i++) {
			Vec2f point = poliBPoints[i];
			float d = point.dot(normal);
			if(d > greatestD) {
				greatestD = d;
				supportB = point;
			}
			if (d < smallestD) {
				smallestD = d;
				nearestPointB = point;
			} else if (d == smallestD) {
				thirdPoint = point;
			}
		}
		// Lerp either A or B contact point
		Vec2f anotherPoint = new Vec2f();
		Vec2f contactPoint = new Vec2f();
		if (lerpA) {
			anotherPoint = new Vec2f(nearestPointB).addMult(normal, -nearestIntervalD);
			contactPoint = anotherPoint;
		} else {
			anotherPoint = new Vec2f(nearestPointA).addMult(normal, nearestIntervalD);
			contactPoint = nearestPointA;
		}

		Contact newContact = new Contact(normal, nearestIntervalD, contactPoint, supportA, supportB);
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}
		
	}

}
