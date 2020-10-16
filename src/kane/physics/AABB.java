package kane.physics;

import kane.math.Vec2f;

/**
 * A simple AABB-Box for fast collision detection.
 */
public class AABB {

	private final Vec2f min;
	private final Vec2f max;
	
	/**
	 * 
	 * @param min -buttom left
	 * @param max -top right
	 */
	public AABB(Vec2f min, Vec2f max) {
		this.min = min;
		this.max = max;
	}
	
	public AABB() {
		min = new Vec2f();
		max = new Vec2f();
	}
	
	/**
	 * Check if two AABBs are overlapping.
	 * @param other -other AABB
	 * @return -true if overlapping
	 */
	public boolean overlaps(AABB other) {
		if (max.getX() < other.getMin().getX() || other.getMax().getX() < min.getX()) {
			return false;
		}
		if((max.getY() < other.getMin().getY() || other.getMax().getY() < min.getY())) {
			return false;
		}
		return true;
	}
	
	/**
	 * transform a box with center and radius to a AABB
	 * @param center -position of the center
	 * @param radius -lengths of the box
	 * @return
	 */
	public static AABB creatFromCrenter(Vec2f center, Vec2f radius) {
		return new AABB(new Vec2f(center).sub(radius), new Vec2f(center).add(radius));
	}

	/**
	 * Get bottom left
	 * @return
	 */
	public Vec2f getMin() {
		return min;
	}

	/**
	 * Get top right
	 * @return
	 */
	public Vec2f getMax() {
		return max;
	}
	
	/**
	 * Sort min and max, so min is bottom left and max is top right.
	 * @return
	 */
	public AABB sortMinMax() {
		Vec2f minTmp = new Vec2f(min);
		Vec2f maxTmp = new Vec2f(max);
		min.set(Math.min(minTmp.getX(), maxTmp.getX()), Math.min(minTmp.getY(), maxTmp.getY()));
		max.set(Math.max(minTmp.getX(), maxTmp.getX()), Math.max(minTmp.getY(), maxTmp.getY()));
		return this;
	}
	
}
