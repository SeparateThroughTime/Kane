package kane.math;

/**
 * This is a Vector object, that works with integers.
 */
public class Vec2i {

	public int x;
	public int y;

	/**
	 * Create a new vector with positioning 0|0
	 */
	public Vec2i() {
	}

	/**
	 * Create a new Vector
	 * 
	 * @param x -x of vector
	 * @param y -y of vector
	 */
	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Do a copy of an existing vector
	 * 
	 * @param v -existing vector
	 */
	public Vec2i(Vec2i v) {
		x = v.x;
		y = v.y;
	}

	/**
	 * set x and y
	 * 
	 * @param x
	 * @param y
	 * @return -Returns itself to work with new value.
	 */
	public Vec2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * set x and y to those of a existing vector
	 * 
	 * @param v -existing vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2i set(Vec2i v) {
		x = v.x;
		y = v.y;
		return this;
	}

	/**
	 * Set x and y to 0
	 * 
	 * @return -Returns itself to work with new value.
	 */
	public Vec2i zero() {
		x = y = 0;
		return this;
	}

	/**
	 * Add another vector to this one
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2i add(Vec2i v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Add a int to this vector
	 * 
	 * @param f -int
	 * @return -Returns itself to work with new value.
	 */
	public Vec2i add(int f) {
		x += f;
		y += f;
		return this;
	}

	/**
	 * Subtract another vector from this vector
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2i sub(Vec2i v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	/**
	 * Does a scalar product of this vector and another.
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public int dot(Vec2i v) {
		return x * v.x + y * v.y;
	}

	/**
	 * Returns the square root of the length of the vector
	 * 
	 * @return -sqare root of length
	 */
	public int lengthSquared() {
		return dot(this);
	}

	/**
	 * Returns the length of the vector
	 * 
	 * @return -length
	 */
	public int length() {
		return Scalar.round((float) Math.sqrt(lengthSquared()));
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}
}
