package kane.physics.contacts;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapePair;

import static kane.physics.Physics.PHYSICS;

import kane.genericGame.Game;

public class ContactSolver implements ContactAcceptor{
    public static ContactSolver CONTACT_SOLVER;

    protected int velocityIterations;
    protected int positionIterations;

    private ContactSolver(int velocityIterations, int positionIterations){
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
    }

    public static void initializeContactSolver(int velocityIterations, int positionIterations){
        if (CONTACT_SOLVER == null){
            CONTACT_SOLVER = new ContactSolver(velocityIterations, positionIterations);
        }
    }

    public void solveVelocity(){
        for (int i = 0; i < velocityIterations; i++){
            for (int j = 0; j < PHYSICS.numShapePairs; j++){
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

                if (!(shapeA.collision && shapeB.collision && shapePair.collideable)){
                    continue;
                }

                Vec2f velAB = new Vec2f(velB).sub(velA);
                Contact contact = shapePair.contact;
                Vec2f normal = contact.normal;
                float projVelAB = velAB.dot(normal);
                float velToRemove = projVelAB + contact.distance / Game.DELTATIME;
                float impulse = Math.min(velToRemove * massAB, 0f);

                velA.addMult(normal, impulse * impulseRateA);
                velB.addMult(normal, -impulse * impulseRateB);
            }
        }
    }

    public void solveFriction(){
        for (int i = 0; i < velocityIterations; i++){
            for (int j = 0; j < PHYSICS.numShapePairs; j++){
                ShapePair shapePair = PHYSICS.shapePairs[j];
                Shape shapeA = shapePair.shapeA;
                Shape shapeB = shapePair.shapeB;
                Body bodyA = shapeA.body;
                Body bodyB = shapeB.body;

                if (!(shapeA.collision && shapeB.collision && shapePair.collideable)){
                    continue;
                }

                Vec2f velA = bodyA.vel;
                Vec2f velB = bodyB.vel;
                Vec2f accA = bodyA.acc;
                Vec2f accB = bodyB.acc;

                float impulseRateA = bodyA.invMass;
                float impulseRateB = bodyB.invMass;
                float massAB = 1 / (impulseRateA + impulseRateB);

                Vec2f accAB = new Vec2f(accB).sub(accA);
                Contact contact = shapePair.contact;
                Vec2f normal = contact.normal;
                float distance = shapePair.contact.distance;
                Vec2f velAB = new Vec2f(velB).sub(velA);
                Vec2f perp = new Vec2f(normal).perpRight();
                float projVelAB = velAB.dot(perp);

                if ((Scalar.equals(velAB.dot(normal), 0f) && Scalar.equals(accAB.dot(normal), 0f)) || distance > 0){
                    continue;
                }

                // Friction Impulse
                float frictionA = shapePair.shapeA.material.friction();
                float frictionB = shapePair.shapeB.material.friction();
                float relFriction = frictionA * frictionB;

                float projVelToRemoveAB = projVelAB - projVelAB * relFriction;
                float frictionImpulse = projVelToRemoveAB * massAB;

                accA.addMult(perp, -(frictionImpulse * impulseRateA) / Game.DELTATIME);
                accB.addMult(perp, -(frictionImpulse * impulseRateB) / Game.DELTATIME);
            }
        }
    }

    public void solvePosition(){
        /*
         Theory was: Bodies collide. In the next frame they are still in collision, so
         the energy conversion doesn't work.
         So this was an attempt to replace the bodies. But still, the impulse is too
         high.
         Maybe this code will be useful in the future...
        		for (int i = 0; i < positionIterations; i++) {
        			for (int j = 0; j < numShapePairs; j++) {
        				ShapePair shapePair = shapePairs[j];
        				Shape shapeA = shapePair.getShapeA();
        				Shape shapeB = shapePair.getShapeB();
        				Body bodyA = shapeA.getBody();
        				Body bodyB = shapeB.getBody();

        				if (shapeA.getCollision() && shapeB.getCollision() && shapePair.isCollideable()) {
        					Contact contact = shapePair.getContact();
        					float distance = contact.getDistance();
        					if (distance < 0) {
        						Vec2f normal = contact.getNormal();
        						float impulseRateA = bodyA.getImpulseRate();
        						float impulseRateB = bodyB.getImpulseRate();
        						float massAB = 1 / (impulseRateA + impulseRateB);
        						bodyA.getPos().addMult(normal, distance * impulseRateA * massAB);
        						bodyB.getPos().addMult(normal, -distance * impulseRateB * massAB);
        					}
        				}
        			}
        		}
        */
    }

    @Override
    public boolean accept(Contact contact){
        return true;
    }

}
