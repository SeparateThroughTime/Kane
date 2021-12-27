package kane.physics.shapes;

import java.awt.Color;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

/**
 * This is a Shape of the Type Circle
 */
public class Circle extends Shape {
	private final float rad;

	/**
	 * 
	 * @param rad      -radius
	 * @param relPosX  -position of x in relation to the body
	 * @param relPosY  -position of y in relation to the body
	 * @param color    -0xrrggbb
	 * @param body
	 * @param material
	 */
	public Circle(float rad, int relPosX, int relPosY, Color color, Body body, Material material, int renderLayer) {
		super(relPosX, relPosY, ShapeType.CIRCLE, body, color, material, renderLayer);
		this.rad = rad;
	}

	/**
	 * Get the radius of the circle
	 * 
	 * @return
	 */
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

	@Override
	public boolean isPointInShape(Vec2f point) {
		// Contact generation Circle-Circle
		Vec2f distanceBetween = new Vec2f(point).sub(getAbsPos());
		Vec2f normal = new Vec2f(distanceBetween).normalize();
		float projDistance = distanceBetween.dot(normal);
		float radius = getRad();
		float d = radius - projDistance;

		if (-d <= 0) {
			return true;
		}
		return false;
	}

	@Override
	protected void mirrorX() {
	}

	@Override
	protected void mirrorY() {
	}

}
