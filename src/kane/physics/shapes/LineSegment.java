package kane.physics.shapes;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapeType;

public class LineSegment extends Shape{
	
	private Vec2f relPosA;
	private Vec2f relPosB;
	
	public LineSegment(Vec2f relPosA, Vec2f relPosB, Body body, int color) {
		super(0, 0, ShapeType.LINESEGMENT, body, color);
		this.relPosA = relPosA;
		this.relPosB = relPosB;
	}

	@Override
	public void updateAABB(Vec2f nextRelPos, float tolerance) {
		Vec2f min = new Vec2f(getAbsPos()).add(relPosA);
		Vec2f max = new Vec2f(getAbsPos()).add(relPosB);
		aabb.getMin().set(min);
		aabb.getMax().set(max);
		aabb.sortMinMax();
		aabb.getMin().sub(tolerance);
		aabb.getMax().add(tolerance);
	}

	@Override
	public float getVolume() {
		return 0;
	}
	
	public Vec2f getRelPosA() {
		return relPosA;
	}
	
	public Vec2f getRelPosB() {
		return relPosB;
	}
	
	public Vec2f getCenter() {
		//Returns the center
		return new Vec2f(relPosA).add(relPosB).mult(0.5f).add(new Vec2f (body.getPos()));
	}

}
