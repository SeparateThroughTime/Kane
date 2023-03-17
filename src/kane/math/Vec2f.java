package kane.math;

/**
 * This is a Vector object, that works with floats.
 */
public class Vec2f {

	public float x;
	public float y;

	/**
	 * Create a new vector with positioning 0|0
	 */
	public Vec2f() {
	}

	/**
	 * Create a new Vector
	 * 
	 * @param x -x of vector
	 * @param y -y of vector
	 */
	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Do a copy of an existing vector
	 * 
	 * @param v -existing vector
	 */
	public Vec2f(Vec2f v) {
		x = v.x;
		y = v.y;
	}

	/**
	 * Do a copy of an existing int vector
	 * 
	 * @param v -existing int vector
	 */
	public Vec2f(Vec2i v) {
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
	public Vec2f set(float x, float y) {
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
	public Vec2f set(Vec2f v) {
		x = v.x;
		y = v.y;
		return this;
	}

	/**
	 * Set x and y to 0
	 * 
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f zero() {
		x = y = 0;
		return this;
	}

	/**
	 * Add another vector to this one
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f add(Vec2f v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Add an int vector to this one
	 * 
	 * @param v -int vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f add(Vec2i v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Add a float to this vector
	 * 
	 * @param f -float
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f add(float f) {
		x += f;
		y += f;
		return this;
	}
	
	public Vec2f add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Subtract another vector from this vector
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f sub(Vec2f v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	/**
	 * Subtract a float from this vector
	 * 
	 * @param f -float
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f sub(float f) {
		x -= f;
		y -= f;
		return this;
	}

	/**
	 * Multiply another vector with this vector
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f mult(Vec2f v) {
		x *= v.x;
		y *= v.y;
		return this;
	}

	/**
	 * Multiply a float with this vector
	 * 
	 * @param f -float
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f mult(float f) {
		x *= f;
		y *= f;
		return this;
	}

	/**
	 * Divide another vector from this vector
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f div(Vec2f v) {
		x /= v.x;
		y /= v.y;
		return this;
	}

	/**
	 * Divide a float from this vector
	 * 
	 * @param f -float
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f div(float f) {
		x /= f;
		y /= f;
		return this;
	}

	/**
	 * Does a scalar product of this vector and another.
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public float dot(Vec2f v) {
		return x * v.x + y * v.y;
	}

	/**
	 * Returns the square root of the length of the vector
	 * 
	 * @return -sqare root of length
	 */
	public float lengthSquared() {
		return dot(this);
	}

	/**
	 * Returns the length of the vector
	 * 
	 * @return -length
	 */
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/**
	 * Turn the vector counter clockwise 90�
	 * 
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f perpLeft() {
		float a = x;
		x = -y;
		y = a;
		return this;
	}

	/**
	 * Add the multiplication of a vector and a float to this vector.
	 * 
	 * @param a -vector
	 * @param b -float
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f addMult(Vec2f a, float b) {
		return this.add(new Vec2f(a).mult(b));
	}

	/**
	 * Add the multiplication of two vectors.
	 * 
	 * @param a -first vector
	 * @param b -second vector
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f addMult(Vec2f a, Vec2f b) {
		return this.add(new Vec2f(a).mult(b));
	}

	/**
	 * Turn the vector clockwise 90�
	 * 
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f perpRight() {
		float a = x;
		x = y;
		y = -a;
		return this;
	}

	/**
	 * Normalize the vector
	 * 
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f normalize() {
		float l = length();
		if (l == 0) {
			l = 1;
		}
		x /= l;
		y /= l;
		return this;
	}

	/**
	 * Negate the vector
	 * 
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f negate() {
		x = -x;
		y = -y;
		return this;
	}

	/**
	 * Turn the vector counter clockwise with specific angle
	 * 
	 * @param angle
	 * @return -Returns itself to work with new value.
	 */
	public Vec2f rotate(float angle) {
		float tmp = x;
		// changes Scale of Shapes with high angles
//		float cosAngle = (float) Scalar.cos(angle);
//		float sinAngle = (float) Scalar.sin(angle);
		float cosAngle = (float) Math.cos(angle);
		float sinAngle = (float) Math.sin(angle);
		x = x * cosAngle - y * sinAngle;
		y = tmp * sinAngle + y * cosAngle;

		return this;
	}

	/**
	 * Calculates the angle between this and another vector
	 * 
	 * @param v -other vector
	 * @return -Returns itself to work with new value.
	 */
	public float angleTo(Vec2f v) {
		float dot = new Vec2f(this).dot(v);
		float lenThis = new Vec2f(this).length();
		float lenV = new Vec2f(v).length();
		return (float) Math.acos(dot / (lenThis * lenV));
	}

	public Vec2i toVec2i() {
		return new Vec2i((int) x, (int) y);
	}

	@Override
	public String toString() {
		return String.format("(%f, %f)", x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass() == obj.getClass()) {
			Vec2f vec = (Vec2f) obj;
			if (vec.x == x && vec.y == y) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
