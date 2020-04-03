package kane.physics;

import kane.math.Vec2f;
import kane.math.Vec2i;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGeneratorFactory;
import kane.physics.contacts.ContactSolver;
import kane.physics.contacts.solvers.BasicImpulseContactSolver;
import kane.physics.contacts.solvers.SpeculativeContactSolver;

public class Physics {
//Manages all physics stuff

	private final int MAX_BODIES = 1000;
	private Body[] bodies;
	private int numBodies;

	private final int MAX_SHAPEPAIRS = 100000;
	private ShapePair[] shapePairs;
	private int numShapePairs;

	private ContactGeneratorFactory cgf;
	private ContactSolver contactSolver;
	
	private Vec2f gravity = new Vec2f(0, -10f);

	public Physics(int Height, int Width, float deltaTime) {
		bodies = new Body[MAX_BODIES];
		numBodies = 0;

		shapePairs = new ShapePair[MAX_SHAPEPAIRS];
		numShapePairs = 0;

		cgf = new ContactGeneratorFactory();
		contactSolver = new SpeculativeContactSolver(deltaTime, 4, 1);
	}

	public void step(float deltaTime) {
		// This needs to run every frame.

		// Gravity
		for (int i = 0; i < numBodies; i++) {
			Body body = bodies[i];
			if (body.getImpulseRatio() > 0) {
				body.getAcc().add(new Vec2f(gravity).div(deltaTime));

			}
		}

		// Acceleration integration
		float aabbTolerance = 5;
		for (int i = 0; i < numBodies; i++) {
			Body body = bodies[i];
			if (body.getImpulseRatio() > 0) {
				body.getVel().addMult(body.getAcc(), deltaTime);
				Vec2f nextPos = new Vec2f(body.getPos()).addMult(body.getVel(), deltaTime);
				body.updateAABB(nextPos, aabbTolerance);
				body.getAcc().zero();
			} else {
				body.updateAABB(body.getPos(), aabbTolerance);

			}
		}

		// Broadphase
		numShapePairs = 0;
		shapePairs = new ShapePair[MAX_SHAPEPAIRS];
		for (int i = 0; i < numBodies; i++) {
			Body bodyA = bodies[i];
			for (int j = 0; j < bodyA.getNumShapes(); j++) {
				Shape shapeA = bodyA.getShape(j);
				for (int k = i + 1; k < numBodies; k++) {
					Body bodyB = bodies[k];
					for (int l = 0; l < bodyB.getNumShapes(); l++) {
						Shape shapeB = bodyB.getShape(l);
						if (shapeA.getAABB().overlaps(shapeB.getAABB())) {
							shapePairs[numShapePairs++] = new ShapePair(shapeA, shapeB);
						}
					}
				}
			}
		}

		// Contacts management
		cgf.generate(contactSolver, shapePairs, numShapePairs);

		// set velocity for penetrations
		contactSolver.solveVelocity(shapePairs, numShapePairs);

		// Velocity integration
		for (Body body : bodies) {
			if (body instanceof Body) {
				if (body.getImpulseRatio() > 0) {
					body.getPos().addMult(body.getVel(), deltaTime);
					body.rotate(0);
				}
			}
		}

		// adjust position for penetrations
		contactSolver.solvePosition(shapePairs, numShapePairs);
	}

	public Physics addBody(Body body) {
		bodies[numBodies++] = body;
		return this;
	}

	public int getMAX_BODIES() {
		return MAX_BODIES;
	}

	public Body getBodies(int index) {
		return bodies[index];
	}

	public int getNumBodies() {
		return numBodies;
	}

	public ShapePair getShapePairs(int index) {
		return shapePairs[index];
	}

	public int getNumShapePairs() {
		return numShapePairs;
	}
	
	public Vec2f setGravity(Vec2f gravity) {
		this.gravity = gravity;
		return this.gravity;
	}
	
	public void clearBodies() {
		for (int i = 0; i < bodies.length; i++) {
			bodies[i] = null;
		}
		numBodies = 0;
	}
}
