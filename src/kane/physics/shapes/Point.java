package kane.physics.shapes;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

import java.awt.*;

public class Point extends Shape{

    public Point(int relPosX, int relPosY, Body body, Color color, Material material, int renderLayer){
        super(relPosX, relPosY, ShapeType.POINT, body, color, material, renderLayer, 2, 1);
    }

    @Override
    public void updateAABB(Vec2f nextAbsPos, float tolerance){
        Vec2f absPos = getAbsPos();
        aabb.getMin().set(new Vec2f(Math.min(absPos.x, nextAbsPos.x) - tolerance,
                Math.min(absPos.y, nextAbsPos.y - tolerance)));
        aabb.getMax().set(new Vec2f(Math.max(absPos.x, nextAbsPos.x) + tolerance,
                Math.max(absPos.y, nextAbsPos.y + tolerance)));
    }

    @Override
    public float getVolume(){
        return 0;
    }

    @Override
    public boolean isPointInShape(Vec2f point){
        return point.equals(getAbsPos());
    }

    @Override
    protected void mirrorX(){
    }

    @Override
    protected void mirrorY(){
    }

}
