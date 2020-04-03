package kane.physics;

import kane.math.Vec2f;

public class AABB {

	private final Vec2f min;
	private final Vec2f max;
	
	public AABB(Vec2f min, Vec2f max) {
		this.min = min;
		this.max = max;
	}
	
	public AABB() {
		min = new Vec2f();
		max = new Vec2f();
	}
	
	public boolean overlaps(AABB other) {
		if (max.getX() < other.getMin().getX() || other.getMax().getX() < min.getX()) {
			return false;
		}
		if((max.getY() < other.getMin().getY() || other.getMax().getY() < min.getY())) {
			return false;
		}
		return true;
	}
	
	public static AABB creatFromCrenter(Vec2f center, Vec2f radius) {
		return new AABB(new Vec2f(center).sub(radius), new Vec2f(center).add(radius));
	}

	public Vec2f getMin() {
		return min;
	}

	public Vec2f getMax() {
		return max;
	}
	
	public AABB sortMinMax() {
		Vec2f minTmp = new Vec2f(min);
		Vec2f maxTmp = new Vec2f(max);
		min.set(Math.min(minTmp.getX(), maxTmp.getX()), Math.min(minTmp.getY(), maxTmp.getY()));
		max.set(Math.max(minTmp.getX(), maxTmp.getX()), Math.max(minTmp.getY(), maxTmp.getY()));
		return this;
	}
	
}
