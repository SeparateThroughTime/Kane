package kane.physics.shapes;

import java.awt.Color;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;
import kane.physics.contacts.Contact;

/**
 * This is a Shape of the Type Plane. Planes are Lines without startpoint or
 * endpoint. The length is only for rendering.
 */
public class Plane extends Shape {
	private Vec2f normal;
	private float distance;
	private float len;

	/**
	 * 
	 * @param normal   -determines the angle of plane
	 * @param distance -determines the position of plane
	 * @param len      -length that is displayed
	 * @param body
	 * @param color    -0xrrggbb
	 * @param material
	 */
	public Plane(Vec2f normal, float distance, float len, Body body, Color color, Material material, int renderLayer) {
		super(0, 0, ShapeType.PLANE, body, color, material, renderLayer);
		this.normal = normal.normalize();
		this.distance = distance;
		this.len = len;
	}

	/**
	 * Get the "starting point" of plane.
	 * 
	 * @return
	 */
	public Vec2f getPoint() {
		return new Vec2f(normal).mult(distance);
	}

	/**
	 * Get length, that is displayed.
	 * 
	 * @return
	 */
	public float getLen() {
		return len;
	}

	/**
	 * Get normal of plane.
	 * 
	 * @return
	 */
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

	@Override
	public boolean isPointInShape(Vec2f point) {
		Vec2f pointOnPlane = getPoint();
		Vec2f distanceToPlane = new Vec2f(pointOnPlane).sub(point);
		float d = distanceToPlane.dot(getNormal());
		
		if (-d <= 0) {
			return true;
		}
		return false;
	}
}
