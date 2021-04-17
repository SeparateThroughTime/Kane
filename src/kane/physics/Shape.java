package kane.physics;

import java.awt.Color;
import java.util.ArrayList;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.math.Vec2f;
import kane.math.Vec2i;
import kane.renderer.SpriteController;

/**
 * Shape is an abstract class, which is the base for all Shapes.
 */
public abstract class Shape {

	public static final int MAX_RENDER_LAYER = 20;

	public final int ID;
	// Layer is from 1-20. First Layer is rendered first, last Layer is rendered
	// last.
	public final int RENDER_LAYER;

	protected final Vec2f relPos;
	protected final Vec2f relPosAlign;
	protected final ShapeType type;
	protected final AABB aabb;
	protected final Body body;
	protected Color color;
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

	protected boolean hasSprite;
	protected SpriteController[] spriteControllers;

	/**
	 * Update the AABB of Shape including its next position.
	 * 
	 * @param nextAbsPos -next absolute position
	 * @param tolerance  -added to each side of AABB
	 */
	public abstract void updateAABB(Vec2f nextAbsPos, float tolerance);

	/**
	 * Get volume of shape.
	 * 
	 * @return
	 */
	public abstract float getVolume();
	
	public abstract boolean isPointInShape(Vec2f point);
	
	public boolean isPointInShape(Vec2i point) {
		Vec2f pointF = new Vec2f(point.getX(), point.getY());
		return isPointInShape(pointF);
	}

	public Shape(int relPosX, int RelPosY, ShapeType type, Body body, Color color, Material material, int renderLayer) {
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
		this.RENDER_LAYER = renderLayer > MAX_RENDER_LAYER ? MAX_RENDER_LAYER : renderLayer;
	}

	/**
	 * Dummy for the rotation of Polygons and circle, if body is rotated.
	 * 
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
	 * 
	 * @param collision
	 */
	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	/**
	 * Get collision.
	 * 
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
	 * Get original relativ position of shape. Should never be used except in the
	 * rotate method of Body.
	 * 
	 * @return
	 */
	public Vec2f getRelPosAlign() {
		return relPosAlign;
	}

	/**
	 * Get Type.
	 * 
	 * @return
	 */
	public ShapeType getType() {
		return type;
	}

	/**
	 * Get AABB.
	 * 
	 * @return
	 */
	public AABB getAABB() {
		return aabb;
	}

	/**
	 * Get body.
	 * 
	 * @return
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Get absolute position.
	 * 
	 * @return
	 */
	public Vec2f getAbsPos() {
		Vec2f absPos = new Vec2f(body.getPos()).add(relPos);
		return absPos;
	}

	/**
	 * get Color.
	 * 
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	public Vec2f getCenterOfMass() {
		return centerOfMass;
	}

	/**
	 * Set color.
	 * 
	 * @param color -0xrrggbb
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Get visible.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set visible.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * add an active attribute for the Shape.
	 * 
	 * @param aa
	 */
	public void addActiveAttribute(ActiveAttributes aa) {
		activeAttributes[numActiveAttributes++] = aa;
	}
	
	public void remActiveAttribute(ActiveAttributes aa) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < numActiveAttributes; i++) {
			if(activeAttributes[i] == aa) {
				indices.add(i);
			}
		}
		for (int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			for (int j = index + 1; j < numActiveAttributes; j++) {
				activeAttributes[j - 1] = activeAttributes[j];
			}
			numActiveAttributes--;
		}
	}

	/**
	 * Get an active attribute with index.
	 * 
	 * @param index
	 * @return
	 */
	public ActiveAttributes getActiveAttribute(int index) {
		return activeAttributes[index];
	}

	/**
	 * Checks if Shape has specific active attribute.
	 * 
	 * @param a -active attribute
	 * @return -true if attribute exists in shape
	 */
	public boolean hasActiveAtrribute(ActiveAttributes a) {
		boolean b = false;
		for (int i = 0; i < numActiveAttributes; i++) {
			if (activeAttributes[i] == a) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * Checks if Shape has specific active passive.
	 * 
	 * @param a -active passive
	 * @return -true if passive exists in shape
	 */
	public boolean hasPassiveAtrribute(PassiveAttributes a) {
		boolean b = false;
		for (int i = 0; i < numPassiveAttributes; i++) {
			if (passiveAttributes[i] == a) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * add an passive attribute for the Shape.
	 * 
	 * @param pa
	 */
	public void addPassiveAttribute(PassiveAttributes pa) {
		passiveAttributes[numPassiveAttributes++] = pa;
	}

	/**
	 * Get an passive attribute with index.
	 * 
	 * @param index
	 * @return
	 */
	
	public void remPassiveAttribute(PassiveAttributes pa) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < numPassiveAttributes; i++) {
			if(passiveAttributes[i] == pa) {
				indices.add(i);
			}
		}
		for (int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			for (int j = index + 1; j < numPassiveAttributes; j++) {
				passiveAttributes[j - 1] = passiveAttributes[j];
			}
			numPassiveAttributes--;
		}
	}
	
	public PassiveAttributes getPassiveAttribute(int index) {
		return passiveAttributes[index];
	}

	/**
	 * Get number of active attributes.
	 * 
	 * @return
	 */
	public int getNumActiveAttributes() {
		return numActiveAttributes;
	}

	/**
	 * Get number of passive attributes.
	 * 
	 * @return
	 */
	public int getNumPassiveAttributes() {
		return numPassiveAttributes;
	}

	/**
	 * Get material.
	 * 
	 * @return
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * set Sprite
	 * 
	 * @param sprite
	 */
	public void setSpriteControllers(SpriteController[] spriteControllers) {
		this.spriteControllers = spriteControllers;
		hasSprite = true;
	}

	/**
	 * check if Shape has a sprite.
	 * 
	 * @return
	 */
	public boolean hasSprite() {
		return hasSprite;
	}

	public SpriteController[] getSpriteControllers() {
		return spriteControllers;
	}

	@Override
	public String toString() {
		return "" + ID;
	}
}
