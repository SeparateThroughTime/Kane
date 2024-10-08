package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Circle;

public class CircleCircleContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        // Contact generation Circle-Circle
        Circle circleA = (Circle) shapePair.shapeA;
        Circle circleB = (Circle) shapePair.shapeB;
        Vec2f distanceBetween = new Vec2f(circleB.getAbsPos()).sub(circleA.getAbsPos());
        Vec2f normal = new Vec2f(distanceBetween).normalize();
        float projDistance = distanceBetween.dot(normal);
        float bothRadius = circleA.rad + circleB.rad;
        float d = bothRadius - projDistance;
        Vec2f closestPointOnA = new Vec2f(circleA.getAbsPos()).addMult(normal, circleA.rad);

        Contact newContact = new Contact(normal, -d, closestPointOnA);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;

        }
    }

}
