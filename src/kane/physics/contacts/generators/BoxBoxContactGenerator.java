package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;

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

		Contact newContact = new Contact(normal, overlap, closestPointB, new Vec2f(), new Vec2f());
		if (acceptor.accept(newContact)) {
			shapePair.addContact(newContact);
		}
		
		
		
		//Old code

//		// Get Minkowski Box
//		Vec2f bothRadius = new Vec2f(boxA.getRad()).add(boxB.getRad());
//		Vec2f extendedMin = new Vec2f(boxA.getAbsPos().sub(bothRadius));
//		Vec2f extendedMax = new Vec2f(boxA.getAbsPos().add(bothRadius));
//		
//		//Get closest Point on Minkowski Box
//		Vec2f closestPoint = new Vec2f(boxB.getAbsPos());
//		closestPoint.setX(Scalar.clamp(closestPoint.getX(), extendedMin.getX(), extendedMax.getX()));
//		closestPoint.setY(Scalar.clamp(closestPoint.getY(), extendedMin.getY(), extendedMax.getY()));
//		
//		Vec2f relPos = new Vec2f(boxB.getAbsPos()).sub(boxA.getAbsPos());
//		float overlapX = Math.abs(relPos.getX()) - bothRadius.getX();
//		float overlapY = Math.abs(relPos.getY()) - bothRadius.getY();
//		float overlap = 0f;
//		Vec2f normal = new Vec2f();
//		Vec2f[] edgePoints = new Vec2f[2];
//		Vec2f[] realEdgePoints = new Vec2f[2];
//		if(overlapX > overlapY) {
//			overlap = overlapX;
//			normal.set(Scalar.sign(relPos.getX()), 0);
//		}
//		else {
//			overlap = overlapY;
//			normal.set(0, Scalar.sign(relPos.getY()));
//		}
//		
//		//Get Edges
//		Vec2f tangent = new Vec2f(normal).perpRight();
//		edgePoints[0] = new Vec2f(boxA.getAbsPos()).addMult(normal, bothRadius).addMult(tangent, bothRadius);
//		edgePoints[1] = new Vec2f(boxA.getAbsPos()).addMult(normal, bothRadius).addMult(tangent, new Vec2f(bothRadius).mult(-1));
//		realEdgePoints[0] = new Vec2f(boxA.getAbsPos()).addMult(normal, boxA.getRad()).addMult(tangent, boxA.getRad());
//		realEdgePoints[1] = new Vec2f(boxA.getAbsPos()).addMult(normal, boxA.getRad()).addMult(tangent, new Vec2f(boxA.getRad()).mult(-1));
//		
//		//Get Line Factor
//		Vec2f distanceToEdge = new Vec2f(boxB.getAbsPos()).sub(edgePoints[0]);
//		Vec2f edgeLineDistance = new Vec2f(edgePoints[1]).sub(edgePoints[0]);
//		// f determines if the Point of Box B is inside the area between the Edges (Just in that axis)
//		// if f is between 0 an 1 the Point is in the area.
//		float f = distanceToEdge.dot(edgeLineDistance) / (edgeLineDistance.lengthSquared());
//		
//		if (f < 0 || f > 1) {
//			//Edge
//			Vec2f distanceToClosest = new Vec2f(boxB.getAbsPos()).sub(closestPoint);
//			normal.set(distanceToClosest).normalize();
//			overlap = distanceToClosest.dot(normal);
//		}
//		else {
//			//Side
//		}
//		
//		//Get Final Contact Point
//		Vec2f realLineDistance = new Vec2f(realEdgePoints[1]).sub(realEdgePoints[0]);
//		Vec2f pointOnA = new Vec2f(realEdgePoints[0]).addMult(realLineDistance, Scalar.clamp(f, 0, 1));
//		
//		Contact newContact = new Contact(normal, overlap, pointOnA);
//		if (acceptor.accept(newContact)) {
//			shapePair.addContact(newContact);
//		}

	}

}
