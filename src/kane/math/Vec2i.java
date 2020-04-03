package kane.math;

public class Vec2i{
//This is a Vector object, that works with integers.

	private int x;
	private int y;
	
	public Vec2i() {
	}
	
	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2i(Vec2i v) {
		x = v.getX();
		y = v.getY();
	}
	
	public int getX() {
		return x;
	}
	
	public Vec2i setX(int x) {
		this.x = x;
		return this;
	}
	
	public int getY() {
		return y;
	}
	
	public Vec2i setY(int y) {
		this.y = y;
		return this;
	}
	
	public Vec2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vec2i set(Vec2i v) {
		x = v.getX();
		y = v.getY();
		return this;
	}
	
	public Vec2i zero() {
		x = y = 0;
		return this;
	}
	
	public Vec2i add(Vec2i v) {
		x += v.getX();
		y += v.getY();
		return this;
	}
	
	public Vec2i add(int f) {
		x += f;
		y += f;
		return this;
	}
	
	
	public Vec2i sub(Vec2i v) {
		x -= v.getX();
		y -= v.getY();
		return this;
	}
	
	public int dot(Vec2i v) {
		return x*v.getX() + y*v.getY();
	}
	
	public int lengthSquared() {
		return dot(this);
	}
	
	public int length() {
		return Scalar.round((float)Math.sqrt(lengthSquared()));
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}
}
