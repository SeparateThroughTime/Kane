package kane.physics.contacts.generators;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Polygon;

public class PlanePolygonContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        Plane planeA = (Plane) shapePair.shapeA;
        Polygon poliB = (Polygon) shapePair.shapeB;

        final int NUM_POINTS_B = poliB.getNumPoints();
        Vec2f[] points = new Vec2f[NUM_POINTS_B];
        for (int i = 0; i < NUM_POINTS_B; i++){
            points[i] = new Vec2f(poliB.getPoint(i)).add(poliB.getAbsPos());
        }

        Vec2f pointOnPlane = planeA.getPoint();

        // get nearest Point of poli
        Vec2f point = new Vec2f();
        float projDistance = 0;
        float greatestD = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < NUM_POINTS_B; i++){
            Vec2f pointI = points[i];
            Vec2f distanceToPlaneI = new Vec2f(pointOnPlane).sub(pointI);
            float projDistanceI = distanceToPlaneI.dot(planeA.getNormal());
            if (Scalar.greaterEqual(projDistanceI, projDistance) || i == 0){
                if (Scalar.equals(projDistanceI, projDistance) && i != 0){
                    // if two edges collide
                    point = new Vec2f(point).add(pointI).mult(0.5f);
                } else{
                    projDistance = projDistanceI;
                    point = pointI;
                }
            }

            float d = point.dot(planeA.getNormal());
            if (d > greatestD){
                greatestD = d;
            }
        }

        Vec2f closestPointOnPlane = new Vec2f(point).addMult(planeA.getNormal(), projDistance);

        Contact newContact = new Contact(planeA.getNormal(), -projDistance, closestPointOnPlane);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }

    }

}
