package kane.physics;

import kane.math.Vec2f;
import kane.physics.contacts.AvtiveAttributes;
import kane.physics.contacts.PassiveAttributes;

public abstract class Shape{
//This is a abstract class for all shapes.

	public final int ID;
	
	protected final Vec2f relPos;
	protected final Vec2f relPosAlign;
	protected final ShapeType type;
	protected final AABB aabb;
	protected final Body body;
	protected int color;
	protected boolean collision;
	protected boolean visible;
	protected final Material material;
	
	protected static int MAX_ACTIVE_ATTRIBUTES = 5;
	protected static int MAX_PASSIVE_ATTRIBUTES = 5;
	protected AvtiveAttributes[] activeAttributes;
	protected PassiveAttributes[] passiveAttributes;
	protected int numActiveAttributes;
	protected int numPassiveAttributes;
	
	public abstract void updateAABB(Vec2f nextAbsPos, float tolerance);
	public abstract float getVolume();
	
	
	public Shape(int relPosX, int RelPosY, ShapeType type, Body body, int color, Material material) {
		relPos = new Vec2f(relPosX, RelPosY);
		relPosAlign = new Vec2f(relPosX, RelPosY);
		this.type = type;
		aabb = new AABB();
		this.body = body;
		this.color = color;
		this.ID = body.numShapes;
		this.collision = true;
		this.visible = true;
		this.numActiveAttributes = 0;
		this.numPassiveAttributes = 0;
		this.activeAttributes = new AvtiveAttributes[MAX_ACTIVE_ATTRIBUTES];
		this.passiveAttributes = new PassiveAttributes[MAX_PASSIVE_ATTRIBUTES];
		this.material = material;
	}
	
	public void rotate(float angle) {
		//Dummy for the rotation of Polygons and circle, if body is rotated.
	}
	
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	
	public boolean getCoolision() {
		return collision;
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
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void addActiveAttribute(AvtiveAttributes aa) {
		activeAttributes[numActiveAttributes++] = aa;
	}
	
	public AvtiveAttributes getActiveAttribute(int index) {
		return activeAttributes[index];
	}
	
	public void addPassiveAttribute(PassiveAttributes pa) {
		passiveAttributes[numPassiveAttributes++] = pa;
	}
	
	public PassiveAttributes getPassiveAttribute(int index) {
		return passiveAttributes[index];
	}
	
	public int getNumActiveAttributes() {
		return numActiveAttributes;
	}
	
	public int getNumPassiveAttributes() {
		return numPassiveAttributes;
	}
	
	public Material getMaterial() {
		return material;
	}
}
