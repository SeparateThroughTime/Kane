package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Circle;
import kane.physics.shapes.LineSegment;

public class LineSegmentCircleContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        LineSegment lineA = (LineSegment) shapePair.shapeA;
        Circle circleB = (Circle) shapePair.shapeB;

        Vec2f lineAbsPosA = new Vec2f(lineA.getAbsPos()).add(lineA.getRelPosA());
        Vec2f lineAbsPosB = new Vec2f(lineA.getAbsPos()).add(lineA.getRelPosB());

        Vec2f lineAB = new Vec2f(lineAbsPosB).sub(lineAbsPosA);
        Vec2f distanceToPoint = new Vec2f(circleB.getAbsPos()).sub(lineAbsPosA);
        // f determines if the Point of Box B is inside the area between the Edges (Just in that axis)
        // if f is between 0 an 1 the Point is in the area.
        float f = distanceToPoint.dot(lineAB) / lineAB.lengthSquared();
        f = Math.max(Math.min(f, 1), 0);

        Vec2f pointOnA = new Vec2f(lineAbsPosA).addMult(lineAB, f);

        Vec2f distanceToClosest = new Vec2f(circleB.getAbsPos()).sub(pointOnA);
        Vec2f normal = new Vec2f(distanceToClosest).normalize();
        float d = distanceToClosest.dot(normal) - circleB.rad;

        Contact newContact = new Contact(normal, d, pointOnA);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }

    }

}
