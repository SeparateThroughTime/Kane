package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

public class BoxPointContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        Box boxA = (Box) shapePair.shapeA;
        Point pointB = (Point) shapePair.shapeB;
        Vec2f radius = new Vec2f(boxA.getRad());
        Vec2f min = new Vec2f(boxA.getMin());
        Vec2f max = new Vec2f(boxA.getMax());

        // Get the closest Point on boxA
        Vec2f closestPointA = new Vec2f(pointB.getAbsPos());
        closestPointA.x = Scalar.clamp(closestPointA.x, min.x, max.x);
        closestPointA.y = Scalar.clamp(closestPointA.y, min.y, max.y);

        // get Normal with difference of Pos of boxes
        Vec2f relPos = new Vec2f(pointB.getAbsPos()).sub(boxA.getAbsPos());
        float overlapX = Math.abs(relPos.x) - radius.x;
        float overlapY = Math.abs(relPos.y) - radius.y;
        float overlap;
        Vec2f normal = new Vec2f();
        if (overlapX > overlapY){
            overlap = overlapX;
            normal.set(Scalar.sign(relPos.x), 0).normalize();
        } else{
            overlap = overlapY;
            normal.set(0, Scalar.sign(relPos.y)).normalize();
        }

        Vec2f closestPointB = new Vec2f(pointB.getAbsPos());

        Contact newContact = new Contact(normal, overlap, closestPointB);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }
    }

}
