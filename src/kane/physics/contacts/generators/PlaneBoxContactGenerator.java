package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.shapes.Box;
import kane.physics.shapes.Plane;

public class PlaneBoxContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        Plane planeA = (Plane) shapePair.shapeA;
        Box boxB = (Box) shapePair.shapeB;

        Vec2f[] points = new Vec2f[4];
        points[0] = new Vec2f(boxB.getMin());
        points[1] = new Vec2f(boxB.getMax());
        points[2] = new Vec2f(points[0].x, points[1].y);
        points[3] = new Vec2f(points[1].x, points[0].y);
        Vec2f pointOnPlane = planeA.getPoint();

        //get nearest Point of Box
        Vec2f point = new Vec2f();
        float projDistance = 0;
        for (int i = 0; i < 4; i++){
            Vec2f pointI = points[i];
            Vec2f distanceToPlaneI = new Vec2f(pointOnPlane).sub(pointI);
            float projDistanceI = distanceToPlaneI.dot(planeA.getNormal());
            if (projDistanceI > projDistance || i == 0){
                projDistance = projDistanceI;
                point = pointI;
            }
        }
        Vec2f closestPointOnPlane = new Vec2f(point).addMult(planeA.getNormal(), projDistance);

        Contact newContact = new Contact(planeA.getNormal(), -projDistance, closestPointOnPlane);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }

    }

}
