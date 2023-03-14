package kane.physics.shapes;

import java.awt.Color;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

/**
 * This is a Shape of the Type LineSegment
 */
public class LineSegment extends Shape{
	
	private Vec2f relPosA;
	private Vec2f relPosB;
	
	/**
	 * 
	 * @param relPosA -position of the first point in relation to the body
	 * @param relPosB -position of the second point in relation to the body
	 * @param body
	 * @param color -0xrrggbb
	 * @param material
	 */
	public LineSegment(Vec2f relPosA, Vec2f relPosB, Body body, Color color, Material material, int renderLayer) {
		super(0, 0, ShapeType.LINESEGMENT, body, color, material, renderLayer, 0, 0);
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
	
	/**
	 * Get relative position of point A.
	 * @return
	 */
	public Vec2f getRelPosA() {
		return relPosA;
	}
	
	/**
	 * Get relative position of point B.
	 * @return
	 */
	public Vec2f getRelPosB() {
		return relPosB;
	}
	
	/**
	 * Get center of line segment.
	 * @return
	 */
	public Vec2f getCenter() {
		return new Vec2f(relPosA).add(relPosB).mult(0.5f).add(new Vec2f (body.getPos()));
	}

	@Override
	public boolean isPointInShape(Vec2f point) {
		return false;
	}

	@Override
	protected void mirrorX() {
		relPosA.set(-relPosA.getX(), relPosA.getY());
		relPosB.set(-relPosB.getX(), relPosB.getY());
	}

	@Override
	protected void mirrorY() {
		relPosA.set(relPosA.getX(), -relPosA.getY());
		relPosB.set(relPosB.getX(), -relPosB.getY());
	}

}
