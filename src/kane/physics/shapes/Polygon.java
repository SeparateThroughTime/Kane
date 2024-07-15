package kane.physics.shapes;

import java.awt.Color;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

public class Polygon extends Shape{

    private final Vec2f[] points;
    private final Vec2f[] pointsAlign;
    private final int numPoints;
    private float angle;

    public Polygon(int relPosX, int RelPosY, Body body, Color color, Vec2f[] points, Material material,
                   int renderLayer){
        super(relPosX, RelPosY, ShapeType.POLYGON, body, color, material, renderLayer, points.length + 1,
                points.length);
        numPoints = points.length;
        this.points = new Vec2f[numPoints];
        this.pointsAlign = new Vec2f[numPoints];
        for (int i = 0; i < numPoints; i++){
            this.points[i] = new Vec2f(points[i]);
            this.pointsAlign[i] = new Vec2f(points[i]);
        }
        this.angle = 0;
        calculateCenterOfMass();
    }

    public Polygon(int relPosX, int RelPosY, Body body, Color color, int numPoints, float radius, Material material,
                   int renderLayer){
        super(relPosX, RelPosY, ShapeType.POLYGON, body, color, material, renderLayer, numPoints + 1, numPoints);
        this.numPoints = numPoints;
        this.points = new Vec2f[numPoints];
        this.pointsAlign = new Vec2f[numPoints];
        Vec2f dir = new Vec2f(1, 0);
        for (int i = 0; i < numPoints; i++){
            this.points[i] = new Vec2f(dir).mult(radius);
            this.pointsAlign[i] = new Vec2f(points[i]);
            dir.rotate(1f / numPoints);
        }
        this.angle = 0;
        calculateCenterOfMass();
    }

    private void calculateCenterOfMass(){
        centerOfMass.zero();
        for (int i = 0; i < numPoints; i++){
            centerOfMass.add(this.points[i]);
        }
        centerOfMass.div(numPoints);
    }

    public float calculateMomentOfInertia(){
        float mass = 1 / invMass;
        momentOfInertia = mass / points.length;
        for (int i = 0; i < numPoints; i++){
            momentOfInertia += new Vec2f(points[i]).sub(centerOfMass).lengthSquared();
        }
        return momentOfInertia;
    }

    @Override
    public void updateAABB(Vec2f nextAbsPos, float tolerance){
        Vec2f absPos = getAbsPos();
        float minX, maxX, minY, maxY;
        minX = maxX = points[0].x;
        minY = maxY = points[0].y;
        for (Vec2f point : points){
            float x = point.x;
            float y = point.y;
            if (x < minX){
                minX = x;
            } else if (x > maxX){
                maxX = x;
            }
            if (y < minY){
                minY = y;
            } else if (y > maxY){
                maxY = y;
            }
        }

        aabb.getMin().set(Math.min(absPos.x, nextAbsPos.x) + minX - tolerance,
                Math.min(absPos.y, nextAbsPos.y) + minY - tolerance);
        aabb.getMax().set(Math.max(absPos.x, nextAbsPos.x) + maxX + tolerance,
                Math.max(absPos.y, nextAbsPos.y) + maxY + tolerance);

    }

    @Override
    public float getVolume(){
        if (numPoints < 3){
            return 0f;
        }
        float volume = 0;
        for (int i = 0; i < numPoints - 2; i++){
            int j = i + 1;
            int k = i + 2;
            Vec2f pointA = points[i];
            Vec2f pointB = points[j];
            Vec2f pointC = points[k];
            assert (pointA != null);
            assert (pointB != null);
            assert (pointC != null);
            volume += Scalar.volumeTriangle(pointA, pointB, pointC);
        }

        return volume;
    }

    @Override
    public void rotate(float angle){
        this.angle += angle;
        float bodyAngle = body.angle;

        for (int i = 0; i < numPoints; i++){
            points[i].set(pointsAlign[i]).rotate(this.angle + bodyAngle);
        }
    }

    public void rotate(float angle, Vec2f referencePoint){
        for (int i = 0; i < numPoints; i++){
            Vec2f rotRefPoint = new Vec2f(points[i]).add(getAbsPos()).sub(referencePoint).rotate(angle);
            points[i].set(rotRefPoint.add(referencePoint).sub(getAbsPos()));
        }
    }

    public void align(){
        float bodyAngle = body.angle;
        for (int i = 0; i < numPoints; i++){
            points[i].set(pointsAlign[i]);
            points[i].rotate(bodyAngle);
        }
        angle = 0;
    }

    public Vec2f getPoint(int index){
        return points[index];
    }

    public int getNumPoints(){
        return numPoints;
    }

    public Vec2f[] getPoints(){
        return points;
    }

    @Override
    public boolean isPointInShape(Vec2f point){
        float bestD = Float.POSITIVE_INFINITY;
        Vec2f bestNormal = new Vec2f();
        Vec2f bestPointOnB = new Vec2f();

        // Loop every "LineSegment" of Poli
        for (int i = 0; i < numPoints; i++){
            int j = i == numPoints - 1 ? 0 : i + 1;

            Vec2f lineAbsPosA = new Vec2f(points[i]);
            Vec2f lineAbsPosB = new Vec2f(points[j]);

            Vec2f lineAB = new Vec2f(lineAbsPosB).sub(lineAbsPosA);
            Vec2f distanceToPoint = new Vec2f(point).sub(lineAbsPosA);
            // f determines if the Point of Box B is inside the area between the Edges (Just
            // in that axis)
            // if f is between 0 an 1 the Point is in the area.
            float f = distanceToPoint.dot(lineAB) / lineAB.lengthSquared();
            f = Math.max(Math.min(f, 1), 0);

            Vec2f pointOnB = new Vec2f(lineAbsPosA).addMult(lineAB, f);

            Vec2f distanceToClosest = new Vec2f(point).sub(pointOnB);
            Vec2f normal = new Vec2f(distanceToClosest).normalize();
            float d = distanceToClosest.dot(normal);

            if (d < bestD){
                bestD = d;
                bestNormal = normal;
                bestPointOnB = pointOnB;
            }
        }

        // If the point is inside the Polygon, bestD is positive again but should be
        // negative. This negates bestD and the normal.
        float distancePointToPoli = Math.abs(new Vec2f(getAbsPos()).sub(point).dot(bestNormal));
        float distancePoliPointToPoli = Math.abs(new Vec2f(getAbsPos()).sub(bestPointOnB).dot(bestNormal));
        if (distancePointToPoli < distancePoliPointToPoli){
            bestD = -bestD;
            bestNormal.mult(-1);
        }

        return bestD <= 0;
    }

    @Override
    protected void mirrorX(){
        for (int i = 0; i < numPoints; i++){
            Vec2f point = points[i];
            point.set(-point.x, point.y);
        }
    }

    @Override
    protected void mirrorY(){
        for (int i = 0; i < numPoints; i++){
            Vec2f point = points[i];
            point.set(point.x, -point.y);
        }
    }

}
