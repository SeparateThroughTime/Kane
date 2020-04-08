package kane.physics.contacts;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.ShapeType;

public class ContactSolver implements ContactAcceptor {
//
	protected int velocityIterations;
	protected int positionIterations;
	protected final float deltaTime;

	public ContactSolver(float deltaTime, int velocityIterations, int positionIterations) {
		this.velocityIterations = velocityIterations;
		this.positionIterations = positionIterations;
		this.deltaTime = deltaTime;
	}

	public void solveVelocity(ShapePair[] shapePairs, int numShapePairs) {
		// Generates new Velocity for Objects
		for (int i = 0; i < velocityIterations; i++) {
			for (int j = 0; j < numShapePairs; j++) {
				ShapePair shapePair = shapePairs[j];
				Shape shapeA = shapePair.getShapeA();
				Shape shapeB = shapePair.getShapeB();
				Body bodyA = shapeA.getBody();
				Body bodyB = shapeB.getBody();
				Vec2f velA = bodyA.getVel();
				Vec2f velB = bodyB.getVel();
				Vec2f velAB = new Vec2f(velB).sub(velA);
				float impulseRatioA = bodyA.getImpulseRatio();
				float impulseRatioB = bodyB.getImpulseRatio();
				float impulseRatioAB = 1 / (impulseRatioA + impulseRatioB);
				if (shapeA.getCoolision() && shapeB.getCoolision() && shapePair.isCollideable()) {
					// Normal Impulse
					Contact contact = shapePair.getContact();
					Vec2f normal = contact.getNormal();
					float projVelAB = velAB.dot(normal);
					float velToRemove = projVelAB + contact.getDistance() / deltaTime;
					float impulse = Math.min(velToRemove * impulseRatioAB, 0f);

					// I really don't unterstand this part... explained in:
					// https://www.youtube.com/watch?v=jusFm0oSNF0&list=PLYG-GfK4ITZ5X2dciKXT_COJrzQAI4oxL&index=29
					// UPDATE: Seems to be pretty useless.
					// TODO: If no bugs appear: delete this. Also delete impulse in contacts
//						contact.setImpulse(impulse);
//						float newImpulse = Math.min(impulse + contact.getImpulse(), 0f);
//						impulse = newImpulse - contact.getImpulse();
//						contact.setImpulse(newImpulse);

					velA.addMult(normal, impulse * impulseRatioA);
					velB.addMult(normal, -impulse * impulseRatioB);

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
					float frictionImpulse = Scalar.clamp(velAB.dot(perp) * impulseRatioAB, maxFriction, -maxFriction);
					contact.setFrictionImpulse(frictionImpulse);

					// TODO Same as above
//						float newFrictionImpulse = Scalar.clamp(frictionImpulse + contact.getFrictionImpulse(), maxFriction, -maxFriction);
//						frictionImpulse = newFrictionImpulse - contact.getFrictionImpulse();
//						contact.setFrictionImpulse(newFrictionImpulse);

					velA.addMult(perp, frictionImpulse * impulseRatioA);
					velB.addMult(perp, -frictionImpulse * impulseRatioB);

					// Rotation Impulse
					Vec2f distanceAToContact = new Vec2f(bodyA.getPos()).add(contact.getPoint());
					float angle = distanceAToContact.angleTo(normal) / Scalar.PI;
				}
			}
		}

	}

	public void solvePosition(ShapePair[] shapePairs, int numShapePairs) {
		// This method doesnt need correction of the position.

	}

	@Override
	public boolean accept(Contact contact) {
		return true;
	}

}
