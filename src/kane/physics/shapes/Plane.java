package kane.physics.shapes;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

import java.awt.*;

public class Plane extends Shape{
    private final Vec2f normal;
    private final float distance;
    private final float len;

    public Plane(Vec2f normal, float distance, float len, Body body, Color color, Material material, int renderLayer){
        super(0, 0, ShapeType.PLANE, body, color, material, renderLayer, 8, 4);
        this.normal = normal.normalize();
        this.distance = distance;
        this.len = len;

        if (body.addShape(this) == null) {
            throw new RuntimeException("Shape creation failed: Body hast too many shapes.");
        };
    }

    public Vec2f getPoint(){
        return new Vec2f(normal).mult(distance);
    }

    public float getLen(){
        return len;
    }
    public float getDistance(){
        return distance;
    }

    public Vec2f getNormal(){
        return normal;
    }

    @Override
    public void updateAABB(Vec2f nextPos, float tolerance){
        final float depth = 50f;
        Vec2f perp = new Vec2f(normal).perpRight();
        Vec2f min = new Vec2f(getPoint());
        Vec2f max = new Vec2f(min).addMult(perp, len);
        aabb.getMin().set(min);
        aabb.getMax().set(max);
        aabb.sortMinMax();
        aabb.getMin().sub(tolerance);
        aabb.getMax().add(tolerance);
        if (normal.x > 0){
            aabb.getMin().x = aabb.getMin().x - depth;
        } else{
            aabb.getMax().x = aabb.getMax().x + depth;
        }
        if (normal.y > 0){
            aabb.getMin().y = aabb.getMin().y - depth;
        } else{
            aabb.getMax().y = aabb.getMax().y + depth;
        }

    }

    @Override
    public float getVolume(){
        return 0;
    }

    @Override
    public boolean isPointInShape(Vec2f point){
        Vec2f pointOnPlane = getPoint();
        Vec2f distanceToPlane = new Vec2f(pointOnPlane).sub(point);
        float d = distanceToPlane.dot(getNormal());

        return -d <= 0;
    }

    @Override
    protected void mirrorX(){
        normal.set(-normal.x, normal.y);
    }

    @Override
    protected void mirrorY(){
        normal.set(normal.x, -normal.y);
    }

}
