package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;

public class CircleBoxContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        Circle circleA = (Circle) shapePair.shapeA;
        Box boxB = (Box) shapePair.shapeB;

        Vec2f boxBMin = new Vec2f(boxB.getMin());
        Vec2f boxBMax = new Vec2f(boxB.getMax());

        Vec2f relPos = new Vec2f(boxB.getAbsPos()).sub(circleA.getAbsPos());
        float overlapX = Math.abs(relPos.x) - boxB.rad.x;
        float overlapY = Math.abs(relPos.y) - boxB.rad.x;
        float smallestOverlap = Math.max(overlapX, overlapY);

        Vec2f normal = new Vec2f();
        Vec2f closestPoint = new Vec2f();
        float d;
        if (smallestOverlap < 0){
            // Penetration
            closestPoint.set(boxB.getAbsPos());
            if (overlapX > overlapY){
                normal.set(Scalar.sign(-relPos.x), 0);
            } else{
                normal.set(0, Scalar.sign(-relPos.y));
            }
            d = smallestOverlap - circleA.rad;
            closestPoint.set(circleA.getAbsPos()).addMult(normal, -smallestOverlap);
        } else{
            // Separation
            closestPoint.set(circleA.getAbsPos());
            closestPoint.x = Scalar.clamp(closestPoint.x, boxBMin.x, boxBMax.x);
            closestPoint.y = Scalar.clamp(closestPoint.y, boxBMin.y, boxBMax.y);

            Vec2f distanceToClosest = new Vec2f(circleA.getAbsPos()).sub(closestPoint);
            normal.set(distanceToClosest).normalize();

            d = distanceToClosest.dot(normal) - circleA.rad;

        }

        Contact newContact = new Contact(new Vec2f(normal).mult(-1), d, closestPoint.addMult(normal, d));
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }
    }

}
