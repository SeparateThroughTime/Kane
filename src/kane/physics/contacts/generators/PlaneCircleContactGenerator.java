package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Circle;
import kane.physics.shapes.Plane;

public class PlaneCircleContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        Plane planeA = (Plane) shapePair.shapeA;
        Circle circleB = (Circle) shapePair.shapeB;
        Vec2f pointOnPlane = planeA.getPoint();
        Vec2f distanceToPlane = new Vec2f(pointOnPlane).sub(circleB.getAbsPos());
        float projDistance = distanceToPlane.dot(planeA.getNormal());
        float projRadius = -circleB.getRad();
        float d = projRadius - projDistance;
        Vec2f closestPointOnPlane = new Vec2f(circleB.getAbsPos()).addMult(planeA.getNormal(), projDistance);

        Contact newContact = new Contact(planeA.getNormal(), d, closestPointOnPlane);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }
    }
}
