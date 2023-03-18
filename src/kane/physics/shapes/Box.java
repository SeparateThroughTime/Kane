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
		super(relPosX, relPosY, ShapeType.BOX, body, color, material, renderLayer, 4, 2);
		this.rad = rad;
	}

	@Override
	public void updateAABB(Vec2f nextAbsPos, float tolerance) {
		Vec2f r = new Vec2f(rad).add(tolerance);
		float rx = r.x;
		float ry = r.y;
		Vec2f absPos = getAbsPos();
		aabb.getMin().set(Math.min(absPos.x, nextAbsPos.x) - rx,
				Math.min(absPos.y, nextAbsPos.y - ry));
		aabb.getMax().set(Math.max(absPos.x, nextAbsPos.x) + rx,
				Math.max(absPos.y, nextAbsPos.y + ry));

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
		return rad.x * rad.y * 4;
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
		closestPointA.x = Scalar.clamp(closestPointA.x, min.x, max.x);
		closestPointA.y = Scalar.clamp(closestPointA.y, min.y, max.y);

		// get Normal with difference of Pos of boxes
		Vec2f relPos = new Vec2f(point).sub(getAbsPos());
		float overlapX = Math.abs(relPos.x) - radius.x;
		float overlapY = Math.abs(relPos.y) - radius.y;
		float overlap = 0f;
		Vec2f normal = new Vec2f();
		if (overlapX > overlapY) {
			overlap = overlapX;
			normal.set(Scalar.sign(relPos.x), 0).normalize();
		} else {
			overlap = overlapY;
			normal.set(0, Scalar.sign(relPos.y)).normalize();
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
