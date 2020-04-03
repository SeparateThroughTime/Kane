package kane.physics.contacts;

import kane.physics.ShapePair;

public abstract class ContactSolver implements ContactAcceptor{
//A Contact Solver changes velocity and position if two objects collide.
	
	protected int velocityIterations;
	protected int positionIterations;
	protected final float deltaTime;
	
	public ContactSolver (float deltaTime, int velocityIterations, int positionIterations) {
		this.velocityIterations = velocityIterations;
		this.positionIterations = positionIterations;
		this.deltaTime = deltaTime;
	}
	
	public abstract void solveVelocity(ShapePair[] shapePairs, int numShapePairs);
	public abstract void solvePosition(ShapePair[] shapePairs, int numShapePairs);

}
