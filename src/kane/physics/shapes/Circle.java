package kane.physics.shapes;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

public class Circle extends Shape {
//Its a circle.
	private final float rad;

	public Circle(float rad, int relPosX, int relPosY, int color, Body body, Material material) {
		super(relPosX, relPosY, ShapeType.CIRCLE, body, color, material);
		this.rad = rad;
	}

	public float getRad() {
		return rad;
	}

	@Override
	public void updateAABB(Vec2f nextAbsPos, float tolerance) {
		float r = rad + tolerance;
		Vec2f absPos = new Vec2f(body.getPos()).add(relPos);
		aabb.getMin().set(Math.min(absPos.getX(), nextAbsPos.getX()) - r,
				Math.min(absPos.getY(), nextAbsPos.getY() - r));
		aabb.getMax().set(Math.max(absPos.getX(), nextAbsPos.getX()) + r,
				Math.max(absPos.getY(), nextAbsPos.getY() + r));

	}

	@Override
	public float getVolume() {
		return Scalar.PI * rad * rad;
	}

}
