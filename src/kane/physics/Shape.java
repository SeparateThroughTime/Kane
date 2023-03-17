package kane.physics;

import java.awt.Color;
import java.util.ArrayList;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.math.Vec2f;
import kane.math.Vec2i;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

/**
 * Shape is an abstract class, which is the base for all Shapes.
 */
public abstract class Shape {

	public static final int MAX_RENDER_LAYER = 20;
	public static int MAX_COLIDED_SHAPES = 50;
	public static int MAX_ACTIVE_ATTRIBUTES = 5;
	public static int MAX_PASSIVE_ATTRIBUTES = 5;

	public final int ID;
	// Layer is from 1-20. First Layer is rendered first, last Layer is rendered
	// last.
	public final int RENDER_LAYER;

	public final Vec2f relPos;
	public final Vec2f relPosAlign;
	public final ShapeType type;
	public final AABB aabb;
	public final Body body;
	public Color color;
	public boolean collision;
	public boolean visible;
	public final Material material;
	public float invMass;
	public final Vec2f centerOfMass;
	public float momentOfInertia;
	protected Shape[] colidedShapes;
	protected int numColidedShapes;

	public ActiveAttributes[] activeAttributes;
	public PassiveAttributes[] passiveAttributes;
	public int numActiveAttributes;
	public int numPassiveAttributes;

	public boolean hasSprite;
	protected SpriteController[] spriteControllers;

	public final int numRenderVertices;
	public final int numRenderElements;

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
		Vec2f pointF = new Vec2f(point);
		return isPointInShape(pointF);
	}

	public Shape(int relPosX, int RelPosY, ShapeType type, Body body, Color color, Material material, int renderLayer,
			int numRenderVertices, int numRenderElements) {
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
		this.colidedShapes = new Shape[MAX_COLIDED_SHAPES];
		this.numRenderVertices = numRenderVertices;
		this.numRenderElements = numRenderElements;
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
			if (activeAttributes[i] == aa) {
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
			if (passiveAttributes[i] == pa) {
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
	 * set Sprite
	 * 
	 * @param sprite
	 */
	public void setSpriteControllers(SpriteController[] spriteControllers) {
		this.spriteControllers = spriteControllers;
		hasSprite = true;
	}

	public SpriteController[] getSpriteControllers() {
		return spriteControllers;
	}

	public void setCurrentSpriteState(SpriteState spriteState) {
		for (int i = 0; i < spriteControllers.length; i++) {
			SpriteController spriteController = spriteControllers[i];
			spriteController.setCurrentSpriteState(spriteState);
		}
	}

	public SpriteState getCurrentSpriteState() {
		if (hasSprite) {
			return spriteControllers[0].currentSpriteState;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "" + ID;
	}

	protected abstract void mirrorX();

	protected abstract void mirrorY();

	public Shape[] getColidedShapes() {
		return ArrayOperations.cutArray(colidedShapes, numColidedShapes);
	}

	public void addColidedShape(Shape shape) {
		this.colidedShapes[numColidedShapes++] = shape;
	}

	public void remColidedShape(Shape shape) {
		this.numColidedShapes -= ArrayOperations.remObjectStatic(colidedShapes, shape, numColidedShapes);
	}

	public Shape[] getColidedShapes(ActiveAttributes attribute) {
		Shape[] shapes = ArrayOperations.cutArray(colidedShapes, numColidedShapes);
		int removedShapes = 0;
		for (Shape shape : shapes) {
			if (!shape.hasActiveAtrribute(attribute)) {
				removedShapes += ArrayOperations.remObjectStatic(shapes, shape, numColidedShapes);
			}
		}
		return ArrayOperations.cutArray(shapes, numColidedShapes - removedShapes);
	}

	public Shape[] getColidedShapes(PassiveAttributes attribute) {
		Shape[] shapes = ArrayOperations.cutArray(colidedShapes, numColidedShapes);
		int removedShapes = 0;
		for (int i = 0; i < numColidedShapes - removedShapes; i++) {
			Shape shape = shapes[i];
			if (!shape.hasPassiveAtrribute(attribute)) {
				ArrayOperations.remSingleObjectStatic(shapes, i--, numColidedShapes - removedShapes++);
			}
		}
		return ArrayOperations.cutArray(shapes, numColidedShapes - removedShapes);
	}
	
	/**
	 * Get absolute position.
	 * 
	 * @return
	 */
	public Vec2f getAbsPos() {
		Vec2f absPos = new Vec2f(body.pos).add(relPos);
		return absPos;
	}
}
