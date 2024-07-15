package kane.physics.shapes;

import java.awt.Color;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

import static kane.renderer.Renderer.RENDERER;

public class LineSegment extends Shape{

    private final Vec2f relPosA;
    private final Vec2f relPosB;

    public LineSegment(Vec2f relPosA, Vec2f relPosB, Body body, Color color, Material material, int renderLayer){
        super(0, 0, ShapeType.LINESEGMENT, body, color, material, renderLayer, 2, 1);
        this.relPosA = relPosA;
        this.relPosB = relPosB;
    }

    @Override
    public void updateAABB(Vec2f nextRelPos, float tolerance){
        Vec2f min = new Vec2f(getAbsPos()).add(relPosA);
        Vec2f max = new Vec2f(getAbsPos()).add(relPosB);
        aabb.getMin().set(min);
        aabb.getMax().set(max);
        aabb.sortMinMax();
        aabb.getMin().sub(tolerance);
        aabb.getMax().add(tolerance);
    }

    @Override
    public float getVolume(){
        return 0;
    }

    public Vec2f getRelPosA(){
        return relPosA;
    }

    public Vec2f getRelPosB(){
        return relPosB;
    }

    public Vec2f getCenter(){
        return new Vec2f(relPosA).add(relPosB).mult(0.5f).add(new Vec2f(body.pos));
    }

    @Override
    public boolean isPointInShape(Vec2f point){
        return false;
    }

    @Override
    protected void mirrorX(){
        relPosA.set(-relPosA.x, relPosA.y);
        relPosB.set(-relPosB.x, relPosB.y);
    }

    @Override
    protected void mirrorY(){
        relPosA.set(relPosA.x, -relPosA.y);
        relPosB.set(relPosB.x, -relPosB.y);
    }

    public void addToRenderer(){
        RENDERER.addLine(this);
    }

}
