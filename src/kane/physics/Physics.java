package kane.physics;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.contacts.ContactGeneratorFactory;
import kane.physics.contacts.ContactSolver;

/**
 * This is the physics engine. It manages all physics stuff.
 */
public class Physics {

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

	private Vec2f gravity = new Vec2f(0, -10f);

	/**
	 * 
	 * @param deltaTime -time between each frame
	 * @param contactListener
	 */
	public Physics(float deltaTime, ContactListener contactListener) {
		bodies = new Body[MAX_BODIES];
		numBodies = 0;
		aabbOverlaps = new boolean[MAX_BODIES][Body.MAX_SHAPES][MAX_BODIES][Body.MAX_SHAPES];
		shapePairIds = new int[MAX_BODIES][Body.MAX_SHAPES][MAX_BODIES][Body.MAX_SHAPES];

		shapePairs = new ShapePair[MAX_SHAPEPAIRS];
		numShapePairs = 0;

		cgf = new ContactGeneratorFactory(contactListener);
		contactSolver = new ContactSolver(deltaTime, 4, 1);
	}

	/**
	 * This need to run every frame. Its the main part of the physics engine, where everything is happening.
	 * @param deltaTime -time between each frame
	 */
	public void step(float deltaTime) {

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
								// Workaround "interchanged Body IDs"
								// In this if, the ID of Body A is always smaller than the ID of Body B
								// (Tested). But in some cases there are still shapepairs where its interchanged. I
								// don't no, why they exist. But with this workaround, the algorithm for
								// managing the shapepairs can handle it.
								aabbOverlaps[k][l][i][j] = true;
								shapePairIds[k][l][i][j] = numShapePairs;
								shapePairs[numShapePairs++] = new ShapePair(shapeA, shapeB);
							}
						} else {
							if (!shapeA.getAABB().overlaps(shapeB.getAABB())) {
								aabbOverlaps[i][j][k][l] = false;
								shapePairs[shapePairIds[i][j][k][l]] = null;
								// Workaround "interchanged Body IDs"
								aabbOverlaps[k][l][i][j] = false;
								shapePairs[shapePairIds[k][l][i][j]] = null;
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
				// Workaround "interchanged Body IDs"
				shapePairIds[k][l][i][j] = m - countGaps;
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
					//??? TODO
//					body.rotate(0);
				}
			}
		}
		
		// Angle velocity integration
//		for (int i = 0; i < numBodies; i++) {
//			Body body = bodies[i];
//			if (body.getImpulseRatio() > 0) {
//				body.rotate(body.getAngleVel() * deltaTime);
//			}
//		}

		// adjust position for penetrations
		contactSolver.solvePosition(shapePairs, numShapePairs);
	}

	/**
	 * Add a body to the engine
	 * @param body
	 * @return
	 */
	public Physics addBody(Body body) {
		bodies[numBodies++] = body;
		return this;
	}

	/**
	 * Get maximum of number of bodies.
	 * @return
	 */
	public int getMAX_BODIES() {
		return MAX_BODIES;
	}

	/**
	 * Get a body with index.
	 * @param index
	 * @return
	 */
	public Body getBodies(int index) {
		return bodies[index];
	}

	/**
	 * Get the number of bodies.
	 * @return
	 */
	public int getNumBodies() {
		return numBodies;
	}

	/**
	 * get a shapePair with collision with specific index.
	 * @param index
	 * @return
	 */
	public ShapePair getShapePairs(int index) {
		return shapePairs[index];
	}

	/**
	 * Get number of ShapePairs.
	 * @return
	 */
	public int getNumShapePairs() {
		return numShapePairs;
	}

	/**
	 * Set gravity
	 * @param gravity
	 * @return
	 */
	public Vec2f setGravity(Vec2f gravity) {
		this.gravity = gravity;
		return this.gravity;
	}

	/**
	 * Get gravity.
	 * @return
	 */
	public Vec2f getGravity() {
		return this.gravity;
	}

	/**
	 * delete all bodies in the engine.
	 */
	public void clearBodies() {
		for (int i = 0; i < bodies.length; i++) {
			bodies[i] = null;
		}
		numBodies = 0;
		Body.resetNumBodies();
		for (int i = 0; i < getNumShapePairs(); i++) {
			shapePairs[i] = null;
		}
		numShapePairs = 0;
	}
}
