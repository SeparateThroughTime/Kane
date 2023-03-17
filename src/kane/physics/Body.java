package kane.physics;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.math.Vec2f;
import kane.renderer.SpriteState;

/**
 * A Body can have many Shapes. The Body is the moving (or not moving) object in
 * the world and the Shapes are bound to him.
 */
public class Body {

	public final int ID;
	private static int numBodies = 0;

	public final Vec2f pos;
	public final Vec2f vel;
	public final Vec2f acc;
	public float angle;
	public float angleVel;
	public boolean rotateByCollision;
	public float rotationFactor;
	public float invMass;
	public final Vec2f centerOfMass = new Vec2f();
	public float momentOfInertia;
	public boolean reactToGravity;
	private boolean removed;
	
	public Shape[] shapes;
	public static final int MAX_SHAPES = 20;
	public int numShapes;

	/**
	 * 
	 * @param posX -position X
	 * @param posY -position Y
	 */
	public Body(int posX, int posY) {
		this(new Vec2f(posX, posY));
	}
	
	public Body(Vec2f pos) {
		invMass = 0;
		vel = new Vec2f();
		acc = new Vec2f();
		this.pos = pos;
		shapes = new Shape[MAX_SHAPES];
		numShapes = 0;
		this.rotateByCollision = false;
		this.ID = numBodies;
		numBodies++;
		calculateCenterOfMass();
		reactToGravity = true;
		removed = false;
	}

	/**
	 * 
	 * @param posX              -position X
	 * @param posY              -position Y
	 * @param rotateByCollision -boolean, if body can rotate when it collides
	 */
	public Body(int posX, int posY, boolean rotateByCollision) {
		this(posX, posY);
		this.rotateByCollision = rotateByCollision;
	}

	public void calculateCenterOfMass() {
		centerOfMass.zero();
		float sumOfMass = 0;
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			if (shape.invMass != 0) {
				float mass = 1 / shape.invMass;
				Vec2f shapeCenterOfMass = new Vec2f(shape.centerOfMass);
				shapeCenterOfMass.add(shape.relPos);
				centerOfMass.addMult(shapeCenterOfMass, mass);
				sumOfMass += mass;
			}
		}
		if(sumOfMass > 0.f) {
			centerOfMass.div(sumOfMass);
		}
	}

	public float calculateMomentOfInertia() {
		momentOfInertia = 0;
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			if(shape.invMass > 0) {
				float newShapeMoI = new Vec2f(shape.centerOfMass).add(shape.relPos).sub(centerOfMass).lengthSquared();
				newShapeMoI /= shape.invMass;
				newShapeMoI += shape.momentOfInertia;
				momentOfInertia += newShapeMoI;
			}
		}
		if(momentOfInertia == 0) {
			momentOfInertia = Float.POSITIVE_INFINITY;
		}
		return momentOfInertia;
	}

	/**
	 * Adds a new Shape to the body
	 * 
	 * @param shape
	 * @return -true if successful. Otherwise false
	 */
	public Shape addShape(Shape shape) {
		if (numShapes < MAX_SHAPES) {
			shapes[numShapes++] = shape;
			updateMass();
			calculateCenterOfMass();
			calculateMomentOfInertia();
			return shape;
		} else {
			return null;
		}
	}

	/**
	 * Delete all Shapes of the body.
	 */
	public void clearBody() {
		shapes = new Shape[MAX_SHAPES];
		numShapes = 0;
	}

	/**
	 * Update Mass of the body. Needs to run when Shapes are changing.
	 */
	public void updateMass() {
		float mass = 0;
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			float shapeVol = shape.getVolume();
			float shapeMass = shapeVol * shape.material.getDensity();
			mass += shapeMass;
			if(shapeMass == 0) {
				shape.invMass = 0;
			} else {
				shape.invMass = 1 / shapeMass;
			}
		}
		if (mass == 0) {
			invMass = 0;
		} else {
			invMass = 1 / mass;
		}
	}

	/**
	 * update AABB of the body, including its next position.
	 * 
	 * @param nextPos   -next position
	 * @param tolerance -added to every side of AABB
	 */
	public void updateAABB(Vec2f nextPos, float tolerance) {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			Vec2f posDif = new Vec2f(nextPos).sub(pos);
			Vec2f nextAbsPos = new Vec2f(shape.getAbsPos()).add(posDif);
			shape.updateAABB(nextAbsPos, tolerance);
		}
	}

	/**
	 * Rotate the body and all rotatable Shapes. Angle is between 0 and 1. Only
	 * Polygons and Cricles are roratable. Static shapes wont move.
	 * 
	 * @param angle -angle between 0 an 1
	 */
	public void rotate(float angle) {
		this.angle += angle;

		for (int i = 0; i < numShapes; i++) {
//			shapes[i].rotate(angle, new Vec2f(getCenterOfMass()).add(getPos()));

			shapes[i].relPos.set(shapes[i].relPosAlign).rotate(this.angle);

			// angle is 0, so body angle is the only altered angle.
			shapes[i].rotate(0);
		}
	}

	/**
	 * Align the Body to ist original angle.
	 */
	public void align() {
		this.angle = 0;
		for (int i = 0; i < numShapes; i++) {
			shapes[i].relPos.set(shapes[i].relPosAlign);

			// angle is 0, so body angle is the only altered angle.
			shapes[i].rotate(0);
		}
	}
	
	/**
	 * Get shape with activeAttribute.
	 * If more shapes exist the first one is returned
	 * @param activeAttribute
	 * @return
	 */
	public Shape getShape(ActiveAttributes activeAttribute) {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			if(shape.hasActiveAtrribute(activeAttribute)) {
				return shape;
			}
		}
		return null;
	}
	
	/**
	 * Get shape with passiveAttribute.
	 * If more shapes exist the first one is returned
	 * @param passiveAttribute
	 * @return
	 */
	public Shape getShape(PassiveAttributes passiveAttribute) {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			if(shape.hasPassiveAtrribute(passiveAttribute)) {
				return shape;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "" + ID;
	}

	/**
	 * Reset the number of bodies ONLY USE IF YOU ARE DELETING ALL BODIES!
	 */
	public static void resetNumBodies() {
		numBodies = 0;
	}
	
	public void mirrorX() {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			Vec2f relPos = shape.relPos;
			shape.relPos.set(-relPos.x, relPos.y);
			
			shape.mirrorX();
		}
	}
	
	public void mirrorY() {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			Vec2f relPos = shape.relPos;
			shape.relPos.set(relPos.x, -relPos.y);
			
			shape.mirrorY();
		}
	}
	
	public boolean hasShapeWithPassiveAttribute(PassiveAttributes passiveA) {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			if (shape.hasPassiveAtrribute(passiveA)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasShapeWithActiveAttribute(ActiveAttributes activeA) {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			if (shape.hasActiveAtrribute(activeA)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns all shapes with sprites
	 * @return
	 */
	private Shape[] getSpriteShapes() {
		Shape[] spriteShapes = new Shape[0];
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			if (shape.hasSprite) {
				spriteShapes = ArrayOperations.add(spriteShapes, shape);
			}
		}
		return spriteShapes;
	}
	
	public void setCurrentSpriteState(SpriteState state) {
		Shape[] spriteShapes = getSpriteShapes();
		for (Shape shape : spriteShapes) {
			shape.setCurrentSpriteState(state);
		}
	}
	
	public void remove() {
		removed = false;
	}
	
	public boolean isRemoved() {
		return removed;
	}
}
