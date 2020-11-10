package kane.physics.contacts;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapePair;

/**
 * The ContactSolver solves the contacts. It alters velocity and position of the
 * bodies.
 */
public class ContactSolver implements ContactAcceptor {
	protected int velocityIterations;
	protected int positionIterations;
	protected final float deltaTime;

	public ContactSolver(float deltaTime, int velocityIterations, int positionIterations) {
		this.velocityIterations = velocityIterations;
		this.positionIterations = positionIterations;
		this.deltaTime = deltaTime;
	}

	/**
	 * Alter velocity
	 * 
	 * @param shapePairs
	 * @param numShapePairs
	 */
	public void solveVelocity(ShapePair[] shapePairs, int numShapePairs) {
		// Generates new Velocity for Objects
		final float coefficientOfRestitution = 0f;
		for (int i = 0; i < velocityIterations; i++) {
			for (int j = 0; j < numShapePairs; j++) {
				ShapePair shapePair = shapePairs[j];
				Shape shapeA = shapePair.getShapeA();
				Shape shapeB = shapePair.getShapeB();
				Body bodyA = shapeA.getBody();
				Body bodyB = shapeB.getBody();
				Vec2f velA = bodyA.getVel();
				Vec2f velB = bodyB.getVel();
				float impulseRateA = bodyA.getImpulseRate();
				float impulseRateB = bodyB.getImpulseRate();
				float massAB = 1 / (impulseRateA + impulseRateB);

				if (shapeA.getCollision() && shapeB.getCollision() && shapePair.isCollideable()) {

					// New Impulses
					// TODO: Not working
					if (true) {
						Contact contact = shapePair.getContact();
						float distance = shapePair.getContact().getDistance();
						if (distance < 0) {
							Vec2f normal = contact.getNormal();
							Vec2f collisionPoint = contact.getPoint();
							Vec2f centerOfMassA = new Vec2f(bodyA.getCenterOfMass()).add(bodyA.getPos());
							Vec2f distCoMAtoCollisionP = new Vec2f(collisionPoint).sub(centerOfMassA).perpLeft();
							Vec2f centerOfMassB = new Vec2f(bodyB.getCenterOfMass()).add(bodyB.getPos());
							Vec2f distCoMBtoCollisionP = new Vec2f(collisionPoint).sub(centerOfMassB).perpLeft();
							Vec2f orbitSpeedA = new Vec2f(distCoMAtoCollisionP).mult(bodyA.getAngleVel());
							Vec2f orbitSpeedB = new Vec2f(distCoMBtoCollisionP).mult(bodyB.getAngleVel());
							Vec2f velPointA = new Vec2f(velA).add(orbitSpeedA);
							Vec2f velPointB = new Vec2f(velB).add(orbitSpeedB);
							Vec2f velAB = new Vec2f(velPointA).sub(velPointB);

							float nominator = -(1 + coefficientOfRestitution) * velAB.dot(normal);
							float impulseRateAB = impulseRateA + impulseRateB;
							float distComAtoCollisionPProj = Math.abs(distCoMAtoCollisionP.dot(normal));
							float rotationPartA = (distComAtoCollisionPProj * distComAtoCollisionPProj)
									/ bodyA.getMomentOfInertia();
							float distComBtoCollisionPProj = Math.abs(distCoMBtoCollisionP.dot(normal));
							float rotationPartB = (distComBtoCollisionPProj * distComBtoCollisionPProj)
									/ bodyB.getMomentOfInertia();
							float collisionScaleFactor = nominator / (impulseRateAB + rotationPartA + rotationPartB);

							velA.addMult(normal, collisionScaleFactor * impulseRateA);
							velB.addMult(normal, -collisionScaleFactor * impulseRateB);

							float angleVelModificationA = (distComAtoCollisionPProj * collisionScaleFactor)
									/ bodyA.getMomentOfInertia();
							bodyA.setAngleVel(bodyA.getAngleVel() + angleVelModificationA);

							float angleVelModificationB = (distComBtoCollisionPProj * collisionScaleFactor)
									/ bodyB.getMomentOfInertia();
							bodyB.setAngleVel(bodyB.getAngleVel() + angleVelModificationB);
						}
					} else {
//					Old Impulses
						// Normal Impulse
						Vec2f velAB = new Vec2f(velB).sub(velA);
						Contact contact = shapePair.getContact();
						Vec2f normal = contact.getNormal();
						float projVelAB = velAB.dot(normal);
						float velToRemove = projVelAB + contact.getDistance() / deltaTime;
						float impulse = Math.min(velToRemove * massAB, 0f);

						// I don't unterstand this part... explained in:
						// https://www.youtube.com/watch?v=jusFm0oSNF0&list=PLYG-GfK4ITZ5X2dciKXT_COJrzQAI4oxL&index=29
						// UPDATE: Seems to be pretty useless.
						// TODO: If no bugs appear: delete this. Also delete impulse in contacts
//						contact.setImpulse(impulse);
//						float newImpulse = Math.min(impulse + contact.getImpulse(), 0f);
//						impulse = newImpulse - contact.getImpulse();
//						contact.setImpulse(newImpulse);

						velA.addMult(normal, impulse * impulseRateA);
						velB.addMult(normal, -impulse * impulseRateB);

						// Just Normal Impulse in direction of perp
						velAB = new Vec2f(velB).sub(velA);
						Vec2f perp = new Vec2f(normal).perpRight();
//						float perpProjVelAB = velAB.dot(perp);
//						float perpVelToRemove = perpProjVelAB + contact.getDistance() / deltaTime;
//						float perpImpulse = Math.min(perpVelToRemove * impulseRatioAB, 0f);
//						float perpNewImpulse = Math.min(perpImpulse + contact.getPerpImpulse(), 0f);
//						perpImpulse = perpNewImpulse - contact.getPerpImpulse();
//						contact.setPerpImpulse(perpNewImpulse);

						// Friction Impulse
						float frictionA = shapePair.getShapeA().getMaterial().getFriction();
						float frictionB = shapePair.getShapeB().getMaterial().getFriction();
						float relFriction = (float) Math.sqrt(frictionA * frictionB) * 2;
						float maxFriction = impulse == 0 ? 0 : -relFriction * 4000;
//						float maxFriction = impulse * relFriction;
						float frictionImpulse = Scalar.clamp(velAB.dot(perp) * massAB, maxFriction, -maxFriction);
						contact.setFrictionImpulse(frictionImpulse);

						// TODO Same as above
//						float newFrictionImpulse = Scalar.clamp(frictionImpulse + contact.getFrictionImpulse(), maxFriction, -maxFriction);
//						frictionImpulse = newFrictionImpulse - contact.getFrictionImpulse();
//						contact.setFrictionImpulse(newFrictionImpulse);

						velA.addMult(perp, frictionImpulse * impulseRateA);
						velB.addMult(perp, -frictionImpulse * impulseRateB);
					}
				}
			}
		}

	}

	/**
	 * Alter position. With this method, no correction of position is needed
	 * 
	 * @param shapePairs
	 * @param numShapePairs
	 */
	public void solvePosition(ShapePair[] shapePairs, int numShapePairs) {
		// Theory was: Bodies collide. In the next frame they are still in collision, so
		// the energey conservation doesnt work.
		// So this was an attempt to replace the bodies. But still, the impulse is too
		// high.
		// Maybe this code will be useful in future...
//		for (int i = 0; i < positionIterations; i++) {
//			for (int j = 0; j < numShapePairs; j++) {
//				ShapePair shapePair = shapePairs[j];
//				Shape shapeA = shapePair.getShapeA();
//				Shape shapeB = shapePair.getShapeB();
//				Body bodyA = shapeA.getBody();
//				Body bodyB = shapeB.getBody();
//
//				if (shapeA.getCollision() && shapeB.getCollision() && shapePair.isCollideable()) {
//					Contact contact = shapePair.getContact();
//					float distance = contact.getDistance();
//					if (distance < 0) {
//						Vec2f normal = contact.getNormal();
//						float impulseRateA = bodyA.getImpulseRate();
//						float impulseRateB = bodyB.getImpulseRate();
//						float massAB = 1 / (impulseRateA + impulseRateB);
//						bodyA.getPos().addMult(normal, distance * impulseRateA * massAB);
//						bodyB.getPos().addMult(normal, -distance * impulseRateB * massAB);
//					}
//				}
//			}
//		}
	}

	@Override
	public boolean accept(Contact contact) {
		return true;
	}

}
