package kane.math;

public class Vec2f {
	// This is a Vector object, that works with floats.

	private float x;
	private float y;

	public Vec2f() {
	}

	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2f(Vec2f v) {
		x = v.getX();
		y = v.getY();
	}
	
	public Vec2f(Vec2i v) {
		x = v.getX();
		y = v.getY();
	}

	public float getX() {
		return x;
	}

	public Vec2f setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public Vec2f setY(float y) {
		this.y = y;
		return this;
	}

	public Vec2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vec2f set(Vec2f v) {
		x = v.getX();
		y = v.getY();
		return this;
	}

	public Vec2f zero() {
		x = y = 0;
		return this;
	}

	public Vec2f add(Vec2f v) {
		x += v.getX();
		y += v.getY();
		return this;
	}
	
	public Vec2f add(Vec2i v) {
		x += v.getX();
		y += v.getY();
		return this;
	}

	public Vec2f add(float f) {
		x += f;
		y += f;
		return this;
	}

	public Vec2f sub(Vec2f v) {
		x -= v.getX();
		y -= v.getY();
		return this;
	}

	public Vec2f sub(float f) {
		x -= f;
		y -= f;
		return this;
	}

	public Vec2f mult(Vec2f v) {
		x *= v.getX();
		y *= v.getY();
		return this;
	}

	public Vec2f mult(float f) {
		x *= f;
		y *= f;
		return this;
	}

	public Vec2f div(Vec2f v) {
		x /= v.getX();
		y /= v.getY();
		return this;
	}

	public Vec2f div(float f) {
		x /= f;
		y /= f;
		return this;
	}

	public float dot(Vec2f v) {
		return x * v.getX() + y * v.getY();
	}

	public float lengthSquared() {
		return dot(this);
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public Vec2f perpLeft() {
		float a = x;
		x = -y;
		y = a;
		return this;
	}

	public Vec2f addMult(Vec2f a, float b) {
		return this.add(new Vec2f(a).mult(b));
	}

	public Vec2f addMult(Vec2f a, Vec2f b) {
		return this.add(new Vec2f(a).mult(b));
	}

	public Vec2f perpRight() {
		float a = x;
		x = y;
		y = -a;
		return this;
	}

	public Vec2f normalize() {
		float l = length();
		if (l == 0) {
			l = 1;
		}
		x /= l;
		y /= l;
		return this;
	}

	public Vec2f negate() {
		x = -x;
		y = -y;
		return this;
	}
	
	public Vec2f rotate(float angle) {
		float tmp = x;
		float angleRAD = angle * Scalar.TWO_PI;
		float cosAngle = (float)Math.cos(angleRAD);
		float sinAngle = (float)Math.sin(angleRAD);
		x = x * cosAngle - y * sinAngle;
		y = tmp * sinAngle + y * cosAngle;
		
		return this;
	}
	
public float angleTo(Vec2f v) {
	float dot = new Vec2f(this).dot(v);
	float lenThis = new Vec2f(this).length();
	float lenV = new Vec2f(v).length();
	return (float)Math.acos(dot / (lenThis * lenV));
	}

	

	@Override
	public String toString() {
		return String.format("(%f, %f)", x, y);
	}
}
