package kane.physics;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.contacts.ContactGeneratorFactory;
import kane.physics.contacts.ContactSolver;
import kane.physics.contacts.ContactSolver;

public class Physics {
//Manages all physics stuff

	private final int MAX_BODIES = 1000;
	private Body[] bodies;
	private int numBodies;

	// The boolean saves if the shapepair overlaps
	// The int saves the index the shapepair has inside of "shapePairs"
	// The coding is: 1. Dimension = bodyA, 2. Dim = shapeA, 3. Dim = bodyB, 4. Dim
	// = shapeB.
	private boolean[][][][] aabbOverlaps;
	private int[][][][] shapePairIds;

	private final int MAX_SHAPEPAIRS = 100000;
	private ShapePair[] shapePairs;
	private int numShapePairs;

	private ContactGeneratorFactory cgf;
	private ContactSolver contactSolver;
	private ContactListener contactListener;

	private Vec2f gravity = new Vec2f(0, -10f);

	public Physics(int Height, int Width, float deltaTime, ContactListener contactListener) {
		bodies = new Body[MAX_BODIES];
		numBodies = 0;
		aabbOverlaps = new boolean[MAX_BODIES][Body.MAX_SHAPES][MAX_BODIES][Body.MAX_SHAPES];
		shapePairIds = new int[MAX_BODIES][Body.MAX_SHAPES][MAX_BODIES][Body.MAX_SHAPES];

		shapePairs = new ShapePair[MAX_SHAPEPAIRS];
		numShapePairs = 0;

		this.contactListener = contactListener;
		cgf = new ContactGeneratorFactory(contactListener);
		contactSolver = new ContactSolver(deltaTime, 4, 1);
	}

	public void step(float deltaTime) {
		// This needs to run every frame.

		// Gravity
		for (int i = 0; i < numBodies; i++) {
			Body body = bodies[i];
			if (Scalar.greaterThan(body.getImpulseRatio(), 0)) {
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
		for (int i = 0; i < numBodies; i++) {
			Body bodyA = bodies[i];
			for (int j = 0; j < bodyA.getNumShapes(); j++) {
				Shape shapeA = bodyA.getShape(j);
				for (int k = i + 1; k < numBodies; k++) {
					Body bodyB = bodies[k];
					for (int l = 0; l < bodyB.getNumShapes(); l++) {
						Shape shapeB = bodyB.getShape(l);
						if (!aabbOverlaps[i][j][k][l]) {
							if (shapeA.getAABB().overlaps(shapeB.getAABB())) {
								aabbOverlaps[i][j][k][l] = true;
								shapePairIds[i][j][k][l] = numShapePairs;
								shapePairs[numShapePairs++] = new ShapePair(shapeA, shapeB);
							}
						} else {
							if (!shapeA.getAABB().overlaps(shapeB.getAABB())) {
								aabbOverlaps[i][j][k][l] = false;
								shapePairs[shapePairIds[i][j][k][l]] = null;
							}
						}
					}
				}
			}
		}

		// Remove empty entries in shapePairs
		int countGaps = 0;
		for (int m = 0; m < numShapePairs; m++) {
			if (shapePairs[m] == null) {
				countGaps++;
			} else {
				shapePairs[m - countGaps] = shapePairs[m];
				int i = shapePairs[m].getShapeA().getBody().ID;
				int j = shapePairs[m].getShapeA().ID;
				int k = shapePairs[m].getShapeB().getBody().ID;
				int l = shapePairs[m].getShapeB().ID;
				shapePairIds[i][j][k][l] = m - countGaps;
			}
		}
		numShapePairs -= countGaps;

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
	
	public Vec2f getGravity() {
		return this.gravity;
	}

	public void clearBodies() {
		for (int i = 0; i < bodies.length; i++) {
			bodies[i] = null;
		}
		numBodies = 0;
	}
}
