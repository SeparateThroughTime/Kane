package kane.physics.shapes;

import java.awt.Color;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

public class Circle extends Shape{
    public final float rad;

    public Circle(float rad, float relPosX, float relPosY, Color color, Body body, Material material, int renderLayer){
        super(relPosX, relPosY, ShapeType.CIRCLE, body, color, material, renderLayer, 0, 0);
        this.rad = rad;

        if (body.addShape(this) == null) {
            throw new RuntimeException("Shape creation failed: Body hast too many shapes.");
        };
    }


    @Override
    public void updateAABB(Vec2f nextAbsPos, float tolerance){
        float r = rad + tolerance;
        Vec2f absPos = new Vec2f(body.pos).add(relPos);
        aabb.getMin().set(Math.min(absPos.x, nextAbsPos.x) - r, Math.min(absPos.y, nextAbsPos.y - r));
        aabb.getMax().set(Math.max(absPos.x, nextAbsPos.x) + r, Math.max(absPos.y, nextAbsPos.y + r));

    }

    @Override
    public float getVolume(){
        return Scalar.PI * rad * rad;
    }

    @Override
    public boolean isPointInShape(Vec2f point){
        // Contact generation Circle-Circle
        Vec2f distanceBetween = new Vec2f(point).sub(getAbsPos());
        Vec2f normal = new Vec2f(distanceBetween).normalize();
        float projDistance = distanceBetween.dot(normal);
        float radius = rad;
        float d = radius - projDistance;

        return -d <= 0;
    }

    @Override
    protected void mirrorX(){
    }

    @Override
    protected void mirrorY(){
    }

}
