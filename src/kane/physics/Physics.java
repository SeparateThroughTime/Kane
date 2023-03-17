package kane.physics;

import kane.math.Vec2f;
import static kane.physics.contacts.ContactGeneratorFactory.CONTACT_GENERATOR_FACTORY;
import static kane.physics.contacts.ContactListener.CONTACT_LISTENER;

import kane.physics.contacts.ContactGeneratorFactory;
import kane.physics.contacts.ContactSolver;

import static kane.physics.contacts.ContactSolver.CONTACT_SOLVER;

import kane.Kane;
import kane.genericGame.Game;;

/**
 * This is the physics engine. It manages all physics stuff.
 */
public class Physics {

	public static Physics PHYSICS;
	
	public static final float AABB_TOLERANCE = 20;
	public static final int MAX_BODIES = 1000;
	public Body[] bodies;
	public int numBodies;

	// The boolean saves if the shapepair overlaps
	// The int saves the index the shapepair has inside of "shapePairs"
	// The coding is: 1. Dimension = bodyA, 2. Dim = shapeA, 3. Dim = bodyB, 4. Dim
	// = shapeB.
	private boolean[][][][] aabbOverlaps;
	private int[][][][] shapePairIds;

	public final int MAX_SHAPEPAIRS = 100000;
	public ShapePair[] shapePairs;
	public int numShapePairs;

	public Vec2f gravity = new Vec2f(0, -35f);

	/**
	 * 
	 * @param deltaTime       -time between each frame
	 * @param contactListener
	 */
	public Physics() {
		bodies = new Body[MAX_BODIES];
		numBodies = 0;
		aabbOverlaps = new boolean[MAX_BODIES][Body.MAX_SHAPES][MAX_BODIES][Body.MAX_SHAPES];
		shapePairIds = new int[MAX_BODIES][Body.MAX_SHAPES][MAX_BODIES][Body.MAX_SHAPES];

		shapePairs = new ShapePair[MAX_SHAPEPAIRS];
		numShapePairs = 0;

		ContactGeneratorFactory.initializateContactGeneratorFactory();
		// TODO: If possible with new ContactSolver -> Change to:
		// contactSolver = new ContactSolver(deltaTime, 1, 1);
		ContactSolver.initializeContactSolver(4, 1);
	}
	
	public static void initializatePhysics() {
		if (PHYSICS == null) {
			PHYSICS = new Physics();
		}
	}

	/**
	 * This need to run every frame. Its the main part of the physics engine, where
	 * everything is happening.
	 * 
	 * @param deltaTime -time between each frame
	 */

	public void preStep() {
		// Gravity
		for (int i = 0; i < numBodies; i++) {
			Body body = bodies[i];
			if (body.invMass > 0 && body.reactToGravity && !body.isRemoved()) {
				body.acc.add(new Vec2f(gravity).div(Game.DELTATIME));

			}
		}
		
		// Solve Friction
		CONTACT_SOLVER.solveFriction();

	}

	public void step() {

		// Acceleration integration
		for (int i = 0; i < numBodies; i++) {
			Body body = bodies[i];
			if (!body.isRemoved()) {
				if (body.invMass > 0) {
					body.vel.addMult(body.acc, Game.DELTATIME);
					Vec2f nextPos = new Vec2f(body.pos).addMult(body.vel, Game.DELTATIME);
					body.updateAABB(nextPos, AABB_TOLERANCE);
					body.acc.zero();
				} else {
					body.updateAABB(body.pos, AABB_TOLERANCE);

				}
			}
		}

		// Broadphase
		for (int i = 0; i < numBodies; i++) {
			Body bodyA = bodies[i];
			for (int j = 0; j < bodyA.numShapes; j++) {
				Shape shapeA = bodyA.shapes[j];
				for (int k = i + 1; k < numBodies; k++) {
					Body bodyB = bodies[k];
					for (int l = 0; l < bodyB.numShapes; l++) {
						Shape shapeB = bodyB.shapes[l];
						if (!aabbOverlaps[i][j][k][l]) {
							if (shapeA.aabb.overlaps(shapeB.aabb) && !bodyA.isRemoved()
									&& !bodyB.isRemoved()) {
								aabbOverlaps[i][j][k][l] = true;
								shapePairIds[i][j][k][l] = numShapePairs;
								// Workaround "interchanged Body IDs"
								// In this if, the ID of Body A is always smaller than the ID of Body B
								// (Tested). But in some cases there are still shapepairs where its
								// interchanged. I
								// don't no, why they exist. But with this workaround, the algorithm for
								// managing the shapepairs can handle it.
								aabbOverlaps[k][l][i][j] = true;
								shapePairIds[k][l][i][j] = numShapePairs;
								shapePairs[numShapePairs++] = new ShapePair(shapeA, shapeB);
							}
						} else {
							if (!shapeA.aabb.overlaps(shapeB.aabb) || bodyA.isRemoved()
									|| bodyB.isRemoved()) {
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
				int i = shapePairs[m].shapeA.body.ID;
				int j = shapePairs[m].shapeA.ID;
				int k = shapePairs[m].shapeB.body.ID;
				int l = shapePairs[m].shapeB.ID;
				shapePairIds[i][j][k][l] = m - countGaps;
				// Workaround "interchanged Body IDs"
				shapePairIds[k][l][i][j] = m - countGaps;
			}
		}
		numShapePairs -= countGaps;
		

		// Contacts management
		CONTACT_GENERATOR_FACTORY.generate(CONTACT_SOLVER);

		

		// set velocity for penetrations
		CONTACT_SOLVER.solveVelocity();

		// Velocity integration
		for (int i = 0; i < numBodies; i++) {
			Body body = bodies[i];
			if (!body.isRemoved()) {
				if (body.invMass > 0) {
					body.pos.addMult(body.vel, Game.DELTATIME);
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
		CONTACT_SOLVER.solvePosition();
	}

	/**
	 * Add a body to the engine
	 * 
	 * @param body
	 * @return
	 */
	public void addBody(Body body) {
		bodies[numBodies++] = body;
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
		;
		for (int i = 0; i < numShapePairs; i++) {
			shapePairs[i] = null;
		}
		numShapePairs = 0;
	}
}
