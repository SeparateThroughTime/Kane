package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Circle;
import kane.physics.shapes.Polygon;

public class CirclePolygonContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        Circle circleA = (Circle) shapePair.shapeA;
        Polygon poliB = (Polygon) shapePair.shapeB;

        // Declarations
        Vec2f poliBAbsPos = poliB.getAbsPos();

        final int NUM_POINTS_B = poliB.getNumPoints();

        Vec2f[] poliBPoints = new Vec2f[NUM_POINTS_B];
        for (int i = 0; i < NUM_POINTS_B; i++){
            poliBPoints[i] = new Vec2f(poliB.getPoint(i)).add(poliBAbsPos);
        }

        float bestD = Float.POSITIVE_INFINITY;
        Vec2f bestNormal = new Vec2f();
        Vec2f bestPointOnB = new Vec2f();

        // Loop every "LineSegment" of Poli
        for (int i = 0; i < NUM_POINTS_B; i++){
            int j = i == NUM_POINTS_B - 1 ? 0 : i + 1;

            Vec2f lineAbsPosA = new Vec2f(poliBPoints[i]);
            Vec2f lineAbsPosB = new Vec2f(poliBPoints[j]);

            Vec2f lineAB = new Vec2f(lineAbsPosB).sub(lineAbsPosA);
            Vec2f distanceToPoint = new Vec2f(circleA.getAbsPos()).sub(lineAbsPosA);
            // f determines if the Point of Box B is inside the area between the Edges (Just
            // in that axis)
            // if f is between 0 an 1 the Point is in the area.
            float f = distanceToPoint.dot(lineAB) / lineAB.lengthSquared();
            f = Math.max(Math.min(f, 1), 0);

            Vec2f pointOnB = new Vec2f(lineAbsPosA).addMult(lineAB, f);

            Vec2f distanceToClosest = new Vec2f(circleA.getAbsPos()).sub(pointOnB);
            Vec2f normal = new Vec2f(distanceToClosest).normalize();
            float d = distanceToClosest.dot(normal) - circleA.rad;

            if (d < bestD){
                bestD = d;
                bestNormal = normal;
                bestPointOnB = pointOnB;
            }
        }

        Contact newContact = new Contact(bestNormal.mult(-1), bestD, bestPointOnB);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }

    }

}
