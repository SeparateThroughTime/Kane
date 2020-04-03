package kane.physics.shapes;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapeType;

public class Box extends Shape{
	private Vec2f rad;

	public Box(int relPosX, int RelPosY, Body body, Vec2f rad, int color) {
		super(relPosX, RelPosY, ShapeType.BOX, body, color);
		this.rad = rad;
	}

	@Override
	public void updateAABB(Vec2f nextAbsPos, float tolerance) {
		Vec2f r = new Vec2f(rad).add(tolerance);
		float rx = r.getX();
		float ry = r.getY();
		Vec2f absPos = getAbsPos();
		aabb.getMin().set(Math.min(absPos.getX(), nextAbsPos.getX()) - rx,
				Math.min(absPos.getY(), nextAbsPos.getY() - ry));
		aabb.getMax().set(Math.max(absPos.getX(), nextAbsPos.getX()) + rx,
				Math.max(absPos.getY(), nextAbsPos.getY() + ry));
		
	}
	
	public Vec2f getMin() {
		return getAbsPos().sub(rad);
	}
	
	public Vec2f getMax() {
		return getAbsPos().add(rad);
	}
	

	@Override
	public float getVolume() {
		return rad.getX() * rad.getY() * 4;
	}

	public Vec2f getRad() {
		return rad;
	}

}
