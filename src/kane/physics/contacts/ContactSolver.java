package kane.physics.contacts;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapePair;

import static kane.physics.Physics.PHYSICS;

import kane.genericGame.Game;

/**
 * The ContactSolver solves the contacts. It alters velocity and position of the
 * bodies.
 */
public class ContactSolver implements ContactAcceptor {
	public static ContactSolver CONTACT_SOLVER;
	
	protected int velocityIterations;
	protected int positionIterations;

	private ContactSolver(int velocityIterations, int positionIterations) {
		this.velocityIterations = velocityIterations;
		this.positionIterations = positionIterations;
	}
	
	public static void initializeContactSolver(int velocityIterations, int positionIterations) {
		if (CONTACT_SOLVER == null) {
			CONTACT_SOLVER = new ContactSolver(velocityIterations, positionIterations);
		}
	}

	/**
	 * Alter velocity
	 * 
	 * @param shapePairs
	 * @param numShapePairs
	 */
	public void solveVelocity() {
		// Generates new Velocity for Objects
		final float coefficientOfRestitution = 0f;
		for (int i = 0; i < velocityIterations; i++) {
			for (int j = 0; j < PHYSICS.numShapePairs; j++) {
				ShapePair shapePair = PHYSICS.shapePairs[j];
				Shape shapeA = shapePair.shapeA;
				Shape shapeB = shapePair.shapeB;
				Body bodyA = shapeA.body;
				Body bodyB = shapeB.body;
				Vec2f velA = bodyA.vel;
				Vec2f velB = bodyB.vel;
				float impulseRateA = bodyA.invMass;
				float impulseRateB = bodyB.invMass;
				float massAB = 1 / (impulseRateA + impulseRateB);

				if (shapeA.collision && shapeB.collision && shapePair.collideable) {

					// New Impulses
					// TODO: Not working
					if (false) {
						Contact contact = shapePair.contact;
						float distance = shapePair.contact.distance;
						if (distance < 0) {
							Vec2f normal = contact.normal;
							Vec2f collisionPoint = contact.point;
							Vec2f centerOfMassA = new Vec2f(bodyA.centerOfMass).add(bodyA.pos);
							Vec2f distCoMAtoCollisionP = new Vec2f(collisionPoint).sub(centerOfMassA).perpLeft();
							Vec2f centerOfMassB = new Vec2f(bodyB.centerOfMass).add(bodyB.pos);
							Vec2f distCoMBtoCollisionP = new Vec2f(collisionPoint).sub(centerOfMassB).perpLeft();
							Vec2f orbitSpeedA = new Vec2f(distCoMAtoCollisionP).mult(bodyA.angleVel);
							Vec2f orbitSpeedB = new Vec2f(distCoMBtoCollisionP).mult(bodyB.angleVel);
							Vec2f velPointA = new Vec2f(velA).add(orbitSpeedA);
							Vec2f velPointB = new Vec2f(velB).add(orbitSpeedB);
							Vec2f velAB = new Vec2f(velPointA).sub(velPointB);

							float nominator = -(1 + coefficientOfRestitution) * velAB.dot(normal);
							float impulseRateAB = impulseRateA + impulseRateB;
							float distComAtoCollisionPProj = Math.abs(distCoMAtoCollisionP.dot(normal));
							float rotationPartA = (distComAtoCollisionPProj * distComAtoCollisionPProj)
									/ bodyA.momentOfInertia;
							float distComBtoCollisionPProj = Math.abs(distCoMBtoCollisionP.dot(normal));
							float rotationPartB = (distComBtoCollisionPProj * distComBtoCollisionPProj)
									/ bodyB.momentOfInertia;
							float collisionScaleFactor = nominator / (impulseRateAB + rotationPartA + rotationPartB);

							velA.addMult(normal, collisionScaleFactor * impulseRateA);
							velB.addMult(normal, -collisionScaleFactor * impulseRateB);

							float angleVelModificationA = (distComAtoCollisionPProj * collisionScaleFactor)
									/ bodyA.momentOfInertia;
							bodyA.angleVel = bodyA.angleVel + angleVelModificationA;

							float angleVelModificationB = (distComBtoCollisionPProj * collisionScaleFactor)
									/ bodyB.momentOfInertia;
							bodyB.angleVel = bodyB.angleVel + angleVelModificationB;
						}
					} else {
//					Old Impulses
						// Normal Impulse
						Vec2f velAB = new Vec2f(velB).sub(velA);
						Contact contact = shapePair.contact;
						Vec2f normal = contact.normal;
						float projVelAB = velAB.dot(normal);
						float velToRemove = projVelAB + contact.distance / Game.DELTATIME;
						float impulse = Math.min(velToRemove * massAB, 0f);

						velA.addMult(normal, impulse * impulseRateA);
						velB.addMult(normal, -impulse * impulseRateB);

//						float frictionA = shapePair.getShapeA().getMaterial().getFriction();
//						float frictionB = shapePair.getShapeB().getMaterial().getFriction();
//						float relFriction = (float) Math.sqrt(frictionA * frictionB) * 2;
//						float maxFriction = impulse == 0 ? 0 : relFriction * 22000;
////						float maxFriction = impulse * relFriction;
//						float frictionImpulse = velAB.dot(perp) > 0 ? maxFriction : -maxFriction;
//						frictionImpulse = velAB.dot(perp) == 0 ? 0 : frictionImpulse;
////						float frictionImpulse = Scalar.clamp(velAB.dot(perp)*4000, maxFriction, -maxFriction);
//						contact.setFrictionImpulse(frictionImpulse);
//						System.out.println(velAB.dot(perp));

//								velA.addMult(perp, frictionImpulse * impulseRateA);
//						velB.addMult(perp, -frictionImpulse * impulseRateB);
					}
				}
			}
		}

	}

	public void solveFriction() {
		for (int i = 0; i < velocityIterations; i++) {
			for (int j = 0; j < PHYSICS.numShapePairs; j++) {
				ShapePair shapePair = PHYSICS.shapePairs[j];
				Shape shapeA = shapePair.shapeA;
				Shape shapeB = shapePair.shapeB;
				Body bodyA = shapeA.body;
				Body bodyB = shapeB.body;

				if (shapeA.collision && shapeB.collision && shapePair.collideable) {
					Vec2f velA = bodyA.vel;
					Vec2f velB = bodyB.vel;
					Vec2f accA = bodyA.acc;
					Vec2f accB = bodyB.acc;
					float impulseRateA = bodyA.invMass;
					float impulseRateB = bodyB.invMass;
					float massAB = 1 / (impulseRateA + impulseRateB);
					Vec2f velAB = new Vec2f(velB).sub(velA);
					Vec2f accAB = new Vec2f(accB).sub(accA);
					Contact contact = shapePair.contact;
					Vec2f normal = contact.normal;
					float distance = shapePair.contact.distance;
					velAB = new Vec2f(velB).sub(velA);
					Vec2f perp = new Vec2f(normal).perpRight();
					float projVelAB = velAB.dot(perp);
					if ((!Scalar.equals(velAB.dot(normal), 0f) || !Scalar.equals(accAB.dot(normal), 0f))
							&& distance <= 0) {

						// Friction Impulse
						float frictionA = shapePair.shapeA.material.getFriction();
						float frictionB = shapePair.shapeB.material.getFriction();
						float relFriction = frictionA * frictionB;

						float projVelToRemoveAB = projVelAB - projVelAB * relFriction;
						float frictionImpulse = projVelToRemoveAB * massAB;

						accA.addMult(perp, -(frictionImpulse * impulseRateA) / Game.DELTATIME);
						accB.addMult(perp, -(frictionImpulse * impulseRateB) / Game.DELTATIME);
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
	public void solvePosition() {
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
