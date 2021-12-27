package kane.physics.shapes;

import java.awt.Color;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

/**
 * This is a Shape of the Type Box
 */
public class Box extends Shape {
	private Vec2f rad;

	/**
	 * 
	 * @param relPosX  -position of x in relation to the body
	 * @param relPosY  -position of y in relation to the body
	 * @param body
	 * @param rad      -lengths of the box
	 * @param color    -0xrrggbb
	 * @param material
	 */
	public Box(int relPosX, int relPosY, Body body, Vec2f rad, Color color, Material material, int renderLayer) {
		super(relPosX, relPosY, ShapeType.BOX, body, color, material, renderLayer);
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

	/**
	 * Get the point bottom left of the box.
	 * 
	 * @return
	 */
	public Vec2f getMin() {
		return getAbsPos().sub(rad);
	}

	/**
	 * Get the point up right of the box.
	 * 
	 * @return
	 */
	public Vec2f getMax() {
		return getAbsPos().add(rad);
	}

	@Override
	public float getVolume() {
		return rad.getX() * rad.getY() * 4;
	}

	/**
	 * Get the lengths of the box.
	 * 
	 * @return
	 */
	public Vec2f getRad() {
		return rad;
	}

	@Override
	public boolean isPointInShape(Vec2f point) {
		Vec2f radius = new Vec2f(getRad());
		Vec2f min = new Vec2f(getMin());
		Vec2f max = new Vec2f(getMax());

		// Get closest Point on boxA
		Vec2f closestPointA = new Vec2f(point);
		closestPointA.setX(Scalar.clamp(closestPointA.getX(), min.getX(), max.getX()));
		closestPointA.setY(Scalar.clamp(closestPointA.getY(), min.getY(), max.getY()));

		// get Normal with difference of Pos of boxes
		Vec2f relPos = new Vec2f(point).sub(getAbsPos());
		float overlapX = Math.abs(relPos.getX()) - radius.getX();
		float overlapY = Math.abs(relPos.getY()) - radius.getY();
		float overlap = 0f;
		Vec2f normal = new Vec2f();
		if (overlapX > overlapY) {
			overlap = overlapX;
			normal.set(Scalar.sign(relPos.getX()), 0).normalize();
		} else {
			overlap = overlapY;
			normal.set(0, Scalar.sign(relPos.getY())).normalize();
		}

		if (overlap <= 0) {
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
