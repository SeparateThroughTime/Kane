package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Circle;
import kane.physics.shapes.Point;

public class CirclePointContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        Circle circleA = (Circle) shapePair.shapeA;
        Point pointB = (Point) shapePair.shapeB;
        Vec2f distanceBetween = new Vec2f(pointB.getAbsPos()).sub(circleA.getAbsPos());
        Vec2f normal = new Vec2f(distanceBetween).normalize();
        float projDistance = distanceBetween.dot(normal);
        float radius = circleA.rad;
        float d = radius - projDistance;
        Vec2f closestPointOnA = new Vec2f(circleA.getAbsPos()).addMult(normal, circleA.rad);

        Contact newContact = new Contact(normal, -d, closestPointOnA);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }
    }
}
