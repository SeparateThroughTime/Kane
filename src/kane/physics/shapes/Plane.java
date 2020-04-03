package kane.physics.shapes;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapeType;

public class Plane extends Shape{
//Planes are Lines without startpoint or endpoint. The length is only for rendering.
	private Vec2f normal;
	private float distance;
	private float len;

	public Plane(Vec2f normal, float distance, float len, Body body, int color) {
		super(0, 0, ShapeType.PLANE, body, color);
		this.normal = normal.normalize();
		this.distance = distance;
		this.len = len;
	}

	public Vec2f getPoint() {
		return new Vec2f(normal).mult(distance);
	}
	
	public float getLen() {
		return len;
	}
	
	public Vec2f getNormal() {
		return normal;
	}

	@Override
	public void updateAABB(Vec2f nextPos, float tolerance) {
		final float depth = 50f;
		Vec2f perp = new Vec2f(normal).perpRight();
		Vec2f min = new Vec2f(getPoint());
		Vec2f max = new Vec2f(min).addMult(perp, len);
		aabb.getMin().set(min);
		aabb.getMax().set(max);
		aabb.sortMinMax();
		aabb.getMin().sub(tolerance);
		aabb.getMax().add(tolerance);
		if (normal.getX() > 0) {
			aabb.getMin().setX(aabb.getMin().getX() - depth);
		} else {
			aabb.getMax().setX(aabb.getMax().getX() + depth);
		}
		if (normal.getY() > 0) {
			aabb.getMin().setY(aabb.getMin().getY() - depth);
		} else {
			aabb.getMax().setY(aabb.getMax().getY() + depth);
		}
		
	}

	@Override
	public float getVolume() {
		return 0;
	}
}
