package kane.math;

/**
 * diverse Math functions
 */
public class Scalar {
	public static float PI = (float) Math.PI;
	public static float TWO_PI = 2 * PI;
	public static float QUARTER_PI = 0.25f * PI;
	public static float SEVEN_QUARTER_PI = 1.75f * PI;
	public static float TOLERANCE = 0.0001f;

	/**
	 * // Convert Y to Cartesian Coordinate System
	 * 
	 * @param y      -value of y
	 * @param height -height of the game
	 * @return -value of y in Cartesian Coordinate System
	 */
	public static int getY(int y, int height) {
		return height - 1 - y;
	}

	/**
	 * Checks if a Point is inside of a Rectangle
	 * 
	 * @param x  -x position of point
	 * @param y  -y position of point
	 * @param x0 -x0 position of rectangle
	 * @param y0 -y0 position of rectangle
	 * @param x1 -x1 position of rectangle
	 * @param y1 -y1 position of rectangle
	 * @return -returns true when point is in recrangle
	 */
	public static boolean isPointInRect(float x, float y, float x0, float y0, float x1, float y1) {

		if (x > x0 && y > y0 && x < x1 && y < y1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if a Point is inside of a Circle
	 * 
	 * @param x  -x position of point
	 * @param y  -y position of point
	 * @param cx -x position of circle
	 * @param cy -y position of circle
	 * @param r  -radius of circle
	 * @return -returns true when point is in circle
	 */
	public static boolean isPointInCircle(float x, float y, float cx, float cy, float r) {
		float dx = x - cx;
		float dy = y - cy;
		float lenSquared = dx * dx + dy * dy;
		return lenSquared <= r * r;
	}

	/**
	 * clamps the value between the minimus an maximum. So, if the value ist lower
	 * then the minimum or higher then the maximum, those will be returned.
	 * 
	 * @param val -value
	 * @param min -minimum
	 * @param max -maximum
	 * @return -clamped value
	 */
	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

	/**
	 * Determines if the value is positive or negative
	 * 
	 * @param val -value
	 * @return -returns -1 for a negative value and 1 for a positive value
	 */
	public static int sign(float val) {
		return val < 0 ? -1 : 1;
	}

	// found in:
	// https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Java
	// altered for the Vec2f Class
	/**
	 * Determines the intersection between two lines.
	 * 
	 * @param posL1 -position of first line
	 * @param dirL1 -direction of first line
	 * @param posL2 -position of second line
	 * @param dirL2 -direction of second line
	 * @return -position of intersection
	 */
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

	/**
	 * compare two floats with a tolerance of 0.01%
	 * 
	 * @param v1 -first value
	 * @param v2 -second value
	 * @return -returns true if equal
	 */
	public static boolean equals(float v1, float v2) {
		return Math.abs(v1 - v2) < TOLERANCE;
	}

	/**
	 * checks if first value is greater than second value with a tolerance of 0.01%
	 * 
	 * @param v1 -first value
	 * @param v2 -second value
	 * @return -returns true if greater than
	 */
	public static boolean greaterThan(float v1, float v2) {
		boolean res = equals(v1, v2);
		if (res) {
			return false;
		}
		return v1 > v2;
	}

	/**
	 * checks if first value is smaller than second value with a tolerance of 0.01%
	 * 
	 * @param v1 -first value
	 * @param v2 -second value
	 * @return -returns true if smaller than
	 */
	public static boolean smallerThan(float v1, float v2) {
		boolean res = equals(v1, v2);
		if (res) {
			return false;
		}
		return v1 < v2;
	}

	/**
	 * Does a linear interpolation.
	 * So the new vector is a point between v1 and v2 depending on the factor.
	 * If factor is 0, v1 will be returned. If factor is 1, v2 will be returned.
	 * If factor is 0.5, the middle of v1 and v2 will be returned.
	 * @param v1 -first point
	 * @param v2 -second point
	 * @param factor
	 * @return -point between v1 and v2 depending on the factor
	 */
	public static float lerp(float v1, float v2, float factor) {
		return v1 + (v2 - v1) * factor;
	}

	/**
	 * Calculates the volume of a triangle.
	 * @param a -first point of triangle
	 * @param b -second point of triangle
	 * @param c -third point of triangle
	 * @return -volume of triangle
	 */
	public static float volumeTriangle(Vec2f a, Vec2f b, Vec2f c) {
		Vec2f ab = new Vec2f(b).sub(a);
		float g = ab.length();
		Vec2f perp = new Vec2f(ab).perpRight().normalize();
		float h = new Vec2f(c).sub(a).dot(perp);
		return Math.abs(g * h * 0.5f);
	}

	/**
	 * Rounds a float
	 * @param f -float
	 * @return -rounded float as int
	 */
	public static int round(float f) {
		return (int) (f + 0.5f);
	}
}
