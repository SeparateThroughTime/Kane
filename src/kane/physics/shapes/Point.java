package kane.physics.shapes;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

/**
 * This is a Shape of the Type Point. Though it can be used with collision, its
 * not recommended, because you cannot calculate the volume and therefore the
 * mass of a point.
 */
public class Point extends Shape {

	/**
	 * 
	 * @param relPosX -position of x in relation to the body
	 * @param relPosY -position of y in relation to the body
	 * @param body
	 * @param color -0xrrggbb
	 * @param material
	 */
	public Point(int relPosX, int relPosY, Body body, int color, Material material) {
		super(relPosX, relPosY, ShapeType.POINT, body, color, material);
	}

	@Override
	public void updateAABB(Vec2f nextAbsPos, float tolerance) {
		Vec2f absPos = getAbsPos();
		aabb.getMin().set(new Vec2f(Math.min(absPos.getX(), nextAbsPos.getX()) - tolerance,
				Math.min(absPos.getY(), nextAbsPos.getY() - tolerance)));
		aabb.getMax().set(new Vec2f(Math.max(absPos.getX(), nextAbsPos.getX()) + tolerance,
				Math.max(absPos.getY(), nextAbsPos.getY() + tolerance)));
	}

	@Override
	public float getVolume() {
		return 0;
	}

}
