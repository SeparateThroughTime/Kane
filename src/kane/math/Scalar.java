package kane.math;

public class Scalar {
	//diverse Math functions
	
	public static float PI = (float)Math.PI;
	public static float TWO_PI = 2 * PI;
	public static float QUARTER_PI = 0.25f * PI;
	public static float SEVEN_QUARTER_PI = 1.75f * PI;
	public static float TOLERANCE = 0.0001f;
	
	public static int getY(int y, int height) {
		// Convert Y to Cartesian Coordinate System
		return height - 1 - y;
	}
	
	public static boolean isPointInRect(float x, float y, float x0, float y0, float x1, float y1) {

		if (x > x0 && y > y0 && x < x1 && y < y1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isPointInCircle(float x, float y, float cx, float cy, float r) {
		float dx = x - cx;
		float dy = y - cy;
		float lenSquared = dx * dx + dy * dy;
		return lenSquared <= r * r;
	}
	
	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
	
	public static int sign (float val) {
		return val < 0 ? -1 : 1;
	}
	
	//found in: https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Java
	//altered for the Vec2f Class
	public static Vec2f findIntersection(Vec2f posL1, Vec2f dirL1, Vec2f posL2, Vec2f dirL2) {
		Vec2f l1e = new Vec2f(posL1).add(dirL1);
		Vec2f l1s = new Vec2f(posL1).addMult(dirL1, 2);
		Vec2f l2e = new Vec2f(posL2).add(dirL2);
		Vec2f l2s = new Vec2f(posL2).addMult(dirL2, 2);
		
		float a1 = l1e.getY() - l1s.getY();
		float b1 = l1s.getX() - l1e.getX();
		float c1 = a1 * l1s.getX() + b1 * l1s.getY();
		
		float a2 = l2e.getY() - l2s.getY();
		float b2 = l2s.getX() - l2e.getX();
		float c2 = a2 * l2s.getX() + b2 * l2s.getY();
		
		float delta = a1 * b2 - a2 * b1;
        return new Vec2f((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
	}
	
	public static boolean equals(float v1, float v2) {
		return Math.abs(v1 - v2) < TOLERANCE;
	}
	
	public static boolean greaterThan(float v1, float v2) {
		boolean res = equals(v1, v2);
		if (res) {
			return false;
		}
		return v1 > v2;
	}
	
	public static boolean smallerThan(float v1, float v2) {
		boolean res = equals(v1, v2);
		if (res) {
			return false;
		}
		return v1 < v2;
	}
	
	public static float lerp(float v1, float v2, float factor) {
		return v1 + (v2 - v1) * factor;
	}
	
	public static float volumeTriangle(Vec2f a, Vec2f b, Vec2f c) {
		Vec2f ab = new Vec2f(b).sub(a);
		float g = ab.length();
		Vec2f perp = new Vec2f(ab).perpRight().normalize();
		float h = new Vec2f(c).sub(a).dot(perp);
		return Math.abs(g * h * 0.5f);
	}
	
	public static int round(float f) {
		return (int)(f + 0.5f);
	}
}
