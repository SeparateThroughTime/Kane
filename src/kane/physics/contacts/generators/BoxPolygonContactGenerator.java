package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.contacts.GeneratorFunctions;
import kane.physics.shapes.Box;
import kane.physics.shapes.Polygon;

public class BoxPolygonContactGenerator implements ContactGenerator {

    private int numPointsA;

    @Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		Box boxA = (Box) shapePair.shapeA;
		Polygon poliB = (Polygon) shapePair.shapeB;

		// Declarations
		Vec2f boxAAbsPos = boxA.getAbsPos();
		Vec2f poliBAbsPos = poliB.getAbsPos();
		Vec2f relPos = poliB.getAbsPos().sub(boxA.getAbsPos());

		final int numPointsA = 4;
		final int NUM_POINTS_B = poliB.getNumPoints();
		Vec2f[] boxAPoints = new Vec2f[numPointsA];
		boxAPoints[0] = new Vec2f(boxAAbsPos).add(boxA.rad);
		boxAPoints[1] = new Vec2f(boxAAbsPos).add(new Vec2f(boxA.rad.x, -boxA.rad.y));
		boxAPoints[2] = new Vec2f(boxAAbsPos).sub(boxA.rad);
		boxAPoints[3] = new Vec2f(boxAAbsPos).add(new Vec2f(-boxA.rad.x, boxA.rad.y));

		Vec2f[] poliBPoints = new Vec2f[NUM_POINTS_B];
		for (int i = 0; i < NUM_POINTS_B; i++) {
			poliBPoints[i] = new Vec2f(poliB.getPoint(i)).add(poliBAbsPos);
		}

		Vec2f[] boxADirs = new Vec2f[numPointsA];
		Vec2f[] boxADirsPerps = new Vec2f[numPointsA];
		for (int i = 0; i < numPointsA; i++) {
			int j = i < numPointsA - 1 ? i + 1 : 0;
			boxADirs[i] = new Vec2f(boxAPoints[j]).sub(boxAPoints[i]).normalize();
			boxADirsPerps[i] = new Vec2f(boxADirs[i]).perpRight();
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
		for (int i = 0; i < numPointsA + NUM_POINTS_B; i++) {
			Vec2f perp;
			if (i < numPointsA) {
				perp = boxADirsPerps[i];
			} else {
				perp = poliBDirsPerps[i - numPointsA];
			}

			float[] minMaxA = GeneratorFunctions.projectPolygon(perp, boxAPoints);
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
		Vec2f secondPointA = new Vec2f();
		Vec2f nearestPointA = new Vec2f();
		float smallestD = Float.POSITIVE_INFINITY;
		boolean lerpA = false;
		boolean lerpB = false;
		for (int i = 0; i < numPointsA; i++) {
			Vec2f point = boxAPoints[i];

			float d = new Vec2f(normal).mult(-1).dot(point);
			if (d < smallestD) {
				smallestD = d;
				nearestPointA = point;
				lerpA = false;
			} else if (d == smallestD) {
				lerpA = true;
				secondPointA = point;
			}
		}
		// Get contact point B
		Vec2f secondPointB = new Vec2f();
		Vec2f nearestPointB = new Vec2f();
		smallestD = Float.POSITIVE_INFINITY;
		for (int i = 0; i < NUM_POINTS_B; i++) {
			Vec2f point = poliBPoints[i];
			float d = point.dot(normal);
			if (d < smallestD) {
				smallestD = d;
				nearestPointB = point;
				lerpB = false;
			} else if (d == smallestD) {
				lerpB = true;
				secondPointB = point;
			}
		}
		
		// Lerp either A or B contact point
        Vec2f anotherPoint;
        Vec2f contactPoint;
				Vec2f perp = new Vec2f(normal).perpLeft();

				if (lerpA && lerpB) {
					// In this case, two edges are colliding.
					//Find the two outer points, two get the two inner points
					Vec2f[] points = new Vec2f[4];
					points[0] = nearestPointA;
					points[1] = secondPointA;
					points[2] = nearestPointB;
					points[3] = secondPointB;
					float nearestD;
					float nearestIndex = 0;
					float mostFarD;
					float mostFarIndex = 0;
					nearestD = mostFarD = perp.dot(points[0]);
					for (int i = 1; i < points.length; i++) {
						Vec2f point = points[i];
						float d = perp.dot(point);
						if (d < nearestD) {
							nearestD = d;
							nearestIndex = i;
						}
						else if (d > mostFarD) {
							mostFarD = d;
							mostFarIndex = i;
						}
					}
					//Get the two inner points
					Vec2f[] innerPoints = new Vec2f[2];
					int indexToFill = 0;
					for (int i = 0; i < points.length; i++) {
						if (i != nearestIndex && i != mostFarIndex) {
							innerPoints[indexToFill] = points[i];
							indexToFill++;
						}
					}
					//Find the mid of the inner points
					Vec2f distance = new Vec2f(innerPoints[0]).sub(innerPoints[1]);
					float projDistance = distance.dot(perp);
					contactPoint = new Vec2f(innerPoints[0]).addMult(perp, projDistance * -0.5f);
					
				} else if (lerpA){
					anotherPoint = new Vec2f(nearestPointB).addMult(normal, -nearestIntervalD);
					contactPoint = anotherPoint;
				} else {
					contactPoint = nearestPointA;
				}

		Contact newContact = new Contact(normal, nearestIntervalD, contactPoint);
		if (acceptor.accept(newContact)) {
			shapePair.contact = newContact;
		}
	}

}
