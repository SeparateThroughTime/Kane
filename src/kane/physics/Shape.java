package kane.physics;

import kane.math.Vec2f;

public abstract class Shape{
//This is a abstract class for all shapes.

	protected final Vec2f relPos;
	protected final Vec2f relPosAlign;
	protected final ShapeType type;
	protected final AABB aabb;
	protected final Body body;
	protected int color;
	
	public abstract void updateAABB(Vec2f nextAbsPos, float tolerance);
	public abstract float getVolume();
	
	
	public Shape(int relPosX, int RelPosY, ShapeType type, Body body, int color) {
		relPos = new Vec2f(relPosX, RelPosY);
		relPosAlign = new Vec2f(relPosX, RelPosY);
		this.type = type;
		aabb = new AABB();
		this.body = body;
		this.color = color;
	}
	
	public void rotate(float angle) {
		//Dummy for the rotation of Polygons and circle, if body is rotated.
	}
	
	public Vec2f getRelPos() {
		return relPos;
	}
	
	public Vec2f getRelPosAlign() {
		//Should never be used except in the rotate method of Body.
		return relPosAlign;
	}
	
	public ShapeType getType() {
		return type;
	}
	
	public AABB getAABB() {
		return aabb;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vec2f getAbsPos() {
		Vec2f absPos = new Vec2f(body.getPos()).add(relPos);
		return absPos;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
