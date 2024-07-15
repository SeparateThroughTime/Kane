package kane.physics.contacts.generators;

import kane.math.Vec2f;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;
import kane.physics.contacts.GeneratorFunctions;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Polygon;

public class LineSegmentPolygonContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        LineSegment lineA = (LineSegment) shapePair.shapeA;
        Polygon poliB = (Polygon) shapePair.shapeB;

        // Declarations
        Vec2f lineAAbsPos = lineA.getAbsPos();
        Vec2f poliBAbsPos = poliB.getAbsPos();
        Vec2f relPos = poliB.getAbsPos().sub(lineA.getCenter());

        final int NUM_POINTS_A = 2;
        final int NUM_POINTS_B = poliB.getNumPoints();
        Vec2f[] lineAPoints = new Vec2f[NUM_POINTS_A];
        lineAPoints[0] = new Vec2f(lineAAbsPos).add(lineA.getRelPosA());
        lineAPoints[1] = new Vec2f(lineAAbsPos).add(lineA.getRelPosB());

        Vec2f[] poliBPoints = new Vec2f[NUM_POINTS_B];
        for (int i = 0; i < NUM_POINTS_B; i++){
            poliBPoints[i] = new Vec2f(poliB.getPoint(i)).add(poliBAbsPos);
        }

        Vec2f[] lineADirs = new Vec2f[NUM_POINTS_A];
        Vec2f[] lineADirsPerps = new Vec2f[NUM_POINTS_A];
        for (int i = 0; i < NUM_POINTS_A; i++){
            int j = i < NUM_POINTS_A - 1 ? i + 1 : 0;
            lineADirs[i] = new Vec2f(lineAPoints[j]).sub(lineAPoints[i]).normalize();
            lineADirsPerps[i] = new Vec2f(lineADirs[i]).perpRight();
        }

        Vec2f[] poliBDirs = new Vec2f[NUM_POINTS_B];
        Vec2f[] poliBDirsPerps = new Vec2f[NUM_POINTS_B];
        for (int i = 0; i < NUM_POINTS_B; i++){
            int j = i < NUM_POINTS_B - 1 ? i + 1 : 0;
            poliBDirs[i] = new Vec2f(poliBPoints[j]).sub(poliBPoints[i]).normalize();
            poliBDirsPerps[i] = new Vec2f(poliBDirs[i]).perpRight();
        }

        float nearestIntervalD = Float.NEGATIVE_INFINITY;
        Vec2f normal = new Vec2f();

        // Check if there is a separation and get normal
        for (int i = 0; i < NUM_POINTS_A + NUM_POINTS_B; i++){
            Vec2f perp;
            if (i < NUM_POINTS_A){
                perp = lineADirsPerps[i];
            } else{
                perp = poliBDirsPerps[i - NUM_POINTS_A];
            }

            float[] minMaxA = GeneratorFunctions.projectPolygon(perp, lineAPoints);
            float[] minMaxB = GeneratorFunctions.projectPolygon(perp, poliBPoints);
            float d = GeneratorFunctions.intervalDist(minMaxA, minMaxB);

            if (d > nearestIntervalD){
                nearestIntervalD = d;
                normal = perp;
            }

            if (d > 0){
                break;
            }
        }

        // invert normal if its upside down
        if (relPos.dot(normal) < 0){
            normal.mult(-1f);
        }

        // Get contact point A
        Vec2f nearestPointA = new Vec2f();
        float smallestD = Float.POSITIVE_INFINITY;
        boolean lerpA = false;
        for (int i = 0; i < NUM_POINTS_A; i++){
            Vec2f point = lineAPoints[i];

            float d = new Vec2f(normal).mult(-1).dot(point);
            if (d < smallestD){
                smallestD = d;
                nearestPointA = point;
            } else if (d == smallestD){
                lerpA = true;
            }
        }
        // Get contact point B
        Vec2f nearestPointB = new Vec2f();
        smallestD = Float.POSITIVE_INFINITY;
        for (int i = 0; i < NUM_POINTS_B; i++){
            Vec2f point = poliBPoints[i];
            float d = point.dot(normal);
            if (d < smallestD){
                smallestD = d;
                nearestPointB = point;
            }
        }
        // Lerp either A or B contact point
        Vec2f anotherPoint;
        Vec2f contactPoint;
        if (lerpA){
            anotherPoint = new Vec2f(nearestPointB).addMult(normal, -nearestIntervalD);
            contactPoint = anotherPoint;
        } else{
            contactPoint = nearestPointA;
        }

        Contact newContact = new Contact(normal, nearestIntervalD, contactPoint);
        if (acceptor.accept(newContact)){
            shapePair.contact = newContact;
        }

    }

}
