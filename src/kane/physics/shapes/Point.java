package kane.physics.shapes;

import static kane.renderer.Renderer.RENDERER;

import java.awt.Color;

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
	 * @param relPosX  -position of x in relation to the body
	 * @param relPosY  -position of y in relation to the body
	 * @param body
	 * @param color    -0xrrggbb
	 * @param material
	 */
	public Point(int relPosX, int relPosY, Body body, Color color, Material material, int renderLayer) {
		super(relPosX, relPosY, ShapeType.POINT, body, color, material, renderLayer, 2, 1);
	}

	@Override
	public void updateAABB(Vec2f nextAbsPos, float tolerance) {
		Vec2f absPos = getAbsPos();
		aabb.getMin().set(new Vec2f(Math.min(absPos.x, nextAbsPos.x) - tolerance,
				Math.min(absPos.y, nextAbsPos.y - tolerance)));
		aabb.getMax().set(new Vec2f(Math.max(absPos.x, nextAbsPos.x) + tolerance,
				Math.max(absPos.y, nextAbsPos.y + tolerance)));
	}

	@Override
	public float getVolume() {
		return 0;
	}

	@Override
	public boolean isPointInShape(Vec2f point) {
		if (point.equals(getAbsPos())) {
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
	
	public void addToRenderer() {
		RENDERER.addLine(this);
	}

}
