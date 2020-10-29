package kane.physics;

import kane.math.Vec2f;
import kane.physics.contacts.ActiveAttributes;
import kane.physics.contacts.PassiveAttributes;
import kane.renderer.Sprite;

/**
 * Shape is an abstract class, which is the base for all Shapes.
 */
public abstract class Shape{

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
	protected float invMass;
	protected final Vec2f centerOfMass;
	protected float momentOfInertia;
	
	protected static int MAX_ACTIVE_ATTRIBUTES = 5;
	protected static int MAX_PASSIVE_ATTRIBUTES = 5;
	protected ActiveAttributes[] activeAttributes;
	protected PassiveAttributes[] passiveAttributes;
	protected int numActiveAttributes;
	protected int numPassiveAttributes;
	
	//protected Sprite sprite;
	//protected SpriteState currentSpriteState;
	protected int currentSpriteStateFrame;
	protected boolean hasSprite;
	protected Sprite sprite;
	
	/**
	 * Update the AABB of Shape including its next position.
	 * @param nextAbsPos -next absolute position
	 * @param tolerance -added to each side of AABB
	 */
	public abstract void updateAABB(Vec2f nextAbsPos, float tolerance);
	/**
	 * Get volume of shape.
	 * @return
	 */
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
		this.activeAttributes = new ActiveAttributes[MAX_ACTIVE_ATTRIBUTES];
		this.passiveAttributes = new PassiveAttributes[MAX_PASSIVE_ATTRIBUTES];
		this.material = material;
		this.hasSprite = false;
		this.centerOfMass = new Vec2f();
	}
	
	/**
	 * Dummy for the rotation of Polygons and circle, if body is rotated.
	 * @param angle
	 */
	public void rotate(float angle) {
	}
	
	public void rotate(float angle, Vec2f referencePoint) {
		
	}
	
	public void setImpulseRatio(float invMass) {
		this.invMass = invMass;
	}
	
	public float getImpulseRatio() {
		return invMass;
	}
	
	public float calculateMomentOfInertia() {
		return momentOfInertia;
	}
	
	/**
	 * Set collision
	 * @param collision
	 */
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	
	/**
	 * Get collision.
	 * @return
	 */
	public boolean getCollision() {
		return collision;
	}
	
	/**
	 * Get relative position of shape.
	 */
	public Vec2f getRelPos() {
		return relPos;
	}
	
	/**
	 * Get original relativ position of shape.
	 * Should never be used except in the rotate method of Body.
	 * @return
	 */
	public Vec2f getRelPosAlign() {
		return relPosAlign;
	}
	
	/**
	 * Get Type.
	 * @return
	 */
	public ShapeType getType() {
		return type;
	}
	
	/**
	 * Get AABB.
	 * @return
	 */
	public AABB getAABB() {
		return aabb;
	}
	
	/**
	 * Get body.
	 * @return
	 */
	public Body getBody() {
		return body;
	}
	
	/**
	 * Get absolute position.
	 * @return
	 */
	public Vec2f getAbsPos() {
		Vec2f absPos = new Vec2f(body.getPos()).add(relPos);
		return absPos;
	}
	
	/**
	 * get Color.
	 * @return
	 */
	public int getColor() {
		return color;
	}
	
	public Vec2f getCenterOfMass() {
		return centerOfMass;
	}

	/**
	 * Set color.
	 * @param color -0xrrggbb
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * Get visible.
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set visible.
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * add an active attribute for the Shape.
	 * @param aa
	 */
	public void addActiveAttribute(ActiveAttributes aa) {
		activeAttributes[numActiveAttributes++] = aa;
	}
	
	/**
	 * Get an active attribute with index.
	 * @param index
	 * @return
	 */
	public ActiveAttributes getActiveAttribute(int index) {
		return activeAttributes[index];
	}
	
	/**
	 * Checks if Shape has specific active attribute.
	 * @param a -active attribute
	 * @return -true if attribute exists in shape
	 */
	public boolean hasActiveAtrribute(ActiveAttributes a) {
		boolean b = false;
		for (int i = 0; i < numActiveAttributes; i++) {
			if(activeAttributes[i] == a) {
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * Checks if Shape has specific active passive.
	 * @param a -active passive
	 * @return -true if passive exists in shape
	 */
	public boolean hasPassiveAtrribute(PassiveAttributes a) {
		boolean b = false;
		for (int i = 0; i < numPassiveAttributes; i++) {
			if(passiveAttributes[i] == a) {
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * add an passive attribute for the Shape.
	 * @param pa
	 */
	public void addPassiveAttribute(PassiveAttributes pa) {
		passiveAttributes[numPassiveAttributes++] = pa;
	}
	
	/**
	 * Get an passive attribute with index.
	 * @param index
	 * @return
	 */
	public PassiveAttributes getPassiveAttribute(int index) {
		return passiveAttributes[index];
	}
	
	/**
	 * Get number of active attributes.
	 * @return
	 */
	public int getNumActiveAttributes() {
		return numActiveAttributes;
	}
	
	/**
	 * Get number of passive attributes.
	 * @return
	 */
	public int getNumPassiveAttributes() {
		return numPassiveAttributes;
	}
	
	/**
	 * Get material.
	 * @return
	 */
	public Material getMaterial() {
		return material;
	}
	
	// TODO: RESPRITE
	/**
	 * set Sprite
	 * @param sprite
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		hasSprite = true;
	}
	
	/**
	 * check if Shape has a sprite.
	 * @return
	 */
	public boolean hasSprite() {
		return hasSprite();
	}
//	TODO RESPRITE
//	public Sprite getSprite() {
//		return sprite;
//	}
//	
//	public void setCurrentSpriteState(SpriteState state) {
//		this.currentSpriteState = state;
//		this.currentSpriteStateFrame = 0;
//	}
//	
//	public SpriteState getCurrentSpriteState() {
//		return currentSpriteState;
//	}
//	
//	public void stepCurrentSpriteStateFrame() {
//		currentSpriteStateFrame++;
//		if(currentSpriteStateFrame >= sprite.getFrameCount(currentSpriteState)) {
//			currentSpriteStateFrame = 0;
//		}
//	}
	
	/**
	 * get the current frame state of the sprite.
	 * @return
	 */
	public int getCurrenSpriteStateFrame() {
		return currentSpriteStateFrame;
	}
	
	@Override
	public String toString() {
		return ""+ID;
	}
}
