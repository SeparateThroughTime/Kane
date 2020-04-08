package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.contacts.GeneratorFunctions;
import kane.physics.shapes.Box;
import kane.physics.shapes.LineSegment;

public class LineSegmentBoxContactGenerator implements ContactGenerator{

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		LineSegment lineA = (LineSegment) shapePair.getShapeA();
		Box boxB = (Box) shapePair.getShapeB();
		
		//Declarations
		Vec2f lineAAbsPos = lineA.getAbsPos();
		Vec2f boxBAbsPos = boxB.getAbsPos();
		Vec2f relPos = boxB.getAbsPos().sub(lineA.getCenter());

		final int NUM_POINTS_A = 2;
		final int NUM_POINTS_B = 4;
		Vec2f[] lineAPoints = new Vec2f[NUM_POINTS_A];
		lineAPoints[0] = new Vec2f(lineAAbsPos).add(lineA.getRelPosA());
		lineAPoints[1] = new Vec2f(lineAAbsPos).add(lineA.getRelPosB());

		Vec2f[] boxBPoints = new Vec2f[NUM_POINTS_B];
		boxBPoints[0] = new Vec2f(boxBAbsPos).add(boxB.getRad());
		boxBPoints[1] = new Vec2f(-boxB.getRad().getX(), boxB.getRad().getY()).add(boxBAbsPos);
		boxBPoints[2] = new Vec2f(boxBAbsPos).sub(boxB.getRad());
		boxBPoints[3] = new Vec2f(boxB.getRad().getX(), -boxB.getRad().getY()).add(boxBAbsPos);

		Vec2f[] lineADirs = new Vec2f[NUM_POINTS_A];
		Vec2f[] lineADirsPerps = new Vec2f[NUM_POINTS_A];
		for (int i = 0; i < NUM_POINTS_A; i++) {
			int j = i < NUM_POINTS_A - 1 ? i + 1 : 0;
			lineADirs[i] = new Vec2f(lineAPoints[j]).sub(lineAPoints[i]).normalize();
			lineADirsPerps[i] = new Vec2f(lineADirs[i]).perpRight();
//			drawPoint(lineAPoints[i], 2, 0x00ffff);
//			drawNormal(lineAPoints[i], lineADirs[i]);
		}

		Vec2f[] boxBDirs = new Vec2f[NUM_POINTS_B];
		Vec2f[] boxBDirsPerps = new Vec2f[NUM_POINTS_B];
		for (int i = 0; i < NUM_POINTS_B; i++) {
			int j = i < NUM_POINTS_B - 1 ? i + 1 : 0;
			boxBDirs[i] = new Vec2f(boxBPoints[j]).sub(boxBPoints[i]).normalize();
			boxBDirsPerps[i] = new Vec2f(boxBDirs[i]).perpRight();
//			drawPoint(boxBPoints[i], 2, 0x00ffff);
//			drawNormal(boxBPoints[i], boxBDirs[i]);
		}
		
		float nearestIntervalD = Float.NEGATIVE_INFINITY;
		Vec2f normal = new Vec2f();
	
		//Check if there is a separation and get normal
		for(int i = 0; i < NUM_POINTS_A + NUM_POINTS_B; i++) {
			Vec2f perp;
			if(i < NUM_POINTS_A) {
				perp = lineADirsPerps[i];
			}
			else {
				perp = boxBDirsPerps[i - NUM_POINTS_A];
			}
			
			float[] minMaxA = GeneratorFunctions.projectPolygon(perp, lineAPoints);
			float[] minMaxB = GeneratorFunctions.projectPolygon(perp, boxBPoints);
			float d = GeneratorFunctions.intervalDist(minMaxA, minMaxB);
			
			if (d > nearestIntervalD) {
				nearestIntervalD = d;
				normal = perp;
			}
			
			if (d > 0) {
				break;
			}
		}
		
		//invert normal when point of Shape B is used
		if(relPos.dot(normal) < 0) {
			normal.mult(-1f);
		}
		
		// Get contact point A
		Vec2f thirdPoint = new Vec2f();
		Vec2f nearestPointA = new Vec2f();
		float smallestD = Float.POSITIVE_INFINITY;
		boolean lerpA = false;
		for (int i = 0; i < NUM_POINTS_A; i++) {
			Vec2f point = lineAPoints[i];
			
			float d = new Vec2f(normal).mult(-1).dot(point);
			if (d < smallestD) {
				smallestD = d;
				nearestPointA = point;
			}
			else if(d == smallestD) {
				thirdPoint = point;
				lerpA = true;
			}
		}
		// Get contact point B
		Vec2f nearestPointB = new Vec2f();
		smallestD = Float.POSITIVE_INFINITY;
		for (int i = 0; i < NUM_POINTS_B; i++) {
			Vec2f point = boxBPoints[i];
			float d = point.dot(normal);
			if (d < smallestD) {
				smallestD = d;
				nearestPointB = point;
			}
			else if(d == smallestD) {
				thirdPoint = point;
			}
		}
		// Lerp either A or B contact point
		Vec2f anotherPoint = new Vec2f();
		Vec2f contactPoint = new Vec2f();
		if(lerpA) {
			anotherPoint = new Vec2f(nearestPointB).addMult(normal, -nearestIntervalD);
			contactPoint = anotherPoint;
		}
		else {
			anotherPoint = new Vec2f(nearestPointA).addMult(normal, nearestIntervalD);	
			contactPoint = nearestPointA;
		}
		
		Contact newContact = new Contact(normal, nearestIntervalD, contactPoint, new Vec2f(), new Vec2f());
		if (acceptor.accept(newContact)) {
			shapePair.setContact(newContact);
		}
		
	}
	
}
