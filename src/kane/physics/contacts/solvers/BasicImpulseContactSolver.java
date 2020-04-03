package kane.physics.contacts.solvers;

import kane.math.Vec2f;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactSolver;

public class BasicImpulseContactSolver extends ContactSolver {
//Simplest Contact Solver. Has Restitution but is unprecise if many objects collide
//(Angry Birds Phenomenon)

	public BasicImpulseContactSolver(float deltaTime, int velocityIterations, int positionIterations) {
		super(deltaTime, velocityIterations, positionIterations);
	}

	@Override
	public void solveVelocity(ShapePair[] shapePairs, int numShapePairs) {
		// Generates new Velocity for Objects
		float restitution = 0.6f;
		for (int i = 0; i < velocityIterations; i++) {
			for (int j = 0; j < numShapePairs; j++) {
				ShapePair shapePair = shapePairs[j];
				for (int k = 0; k < shapePair.getNumContacts(); k++) {
					Contact contact = shapePair.getContacts()[k];
					{
						Vec2f normal = contact.getNormal();
						Shape shapeA = shapePair.getShapeA();
						Shape shapeB = shapePair.getShapeB();
						Vec2f velA = shapeA.getBody().getVel();
						Vec2f velB = shapeB.getBody().getVel();
						Vec2f velAB = new Vec2f(velB).sub(velA);
						float projVelAB = velAB.dot(normal);
						boolean movingTowards = projVelAB < 0;
						if (movingTowards) {
							float e = 1f + restitution;
							float impulseRatioA = shapeA.getBody().getImpulseRatio();
							float impulseRatioB = shapeB.getBody().getImpulseRatio();
							float impulse = Math.min(projVelAB * e / (impulseRatioA + impulseRatioB), 0);
							velA.addMult(normal, impulse * impulseRatioA);
							velB.addMult(normal, -impulse * impulseRatioB);
						}
					}
				}
			}
		}
	}

	@Override
	public void solvePosition(ShapePair[] shapePairs, int numShapePairs) {
		// Generates new Position for Objects
		final float minDistance = 0.01f;
		final float maxCorrection = 0.5f;
		for (ShapePair shapePair : shapePairs) {
			if (shapePair != null) {
				Shape shapeA = shapePair.getShapeA();
				Shape shapeB = shapePair.getShapeB();
				float impulseRatioA = shapeA.getBody().getImpulseRatio();
				float impulseRatioB = shapeB.getBody().getImpulseRatio();
				float impulseRatio = 1.0f / (impulseRatioA + impulseRatioB);
				for (Contact contact : shapePair.getContacts()) {
					if (contact != null) {
						Vec2f normal = contact.getNormal();
						float correction = Math
								.min((contact.getDistance() + minDistance) * maxCorrection * impulseRatio, 0f);
						shapeA.getAbsPos().addMult(normal, correction * impulseRatioA);
						shapeB.getAbsPos().addMult(normal, -correction * impulseRatioB);
					}
				}
			}
		}

	}

	@Override
	public boolean accept(Contact contact) {
		// TODO Auto-generated method stub
		return contact.getDistance() < 0;
	}

}
