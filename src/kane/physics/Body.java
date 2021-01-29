package kane.physics;

import java.awt.font.ShapeGraphicAttribute;

import kane.math.Vec2f;

/**
 * A Body can have many Shapes. The Body is the moving (or not moving) object in
 * the world and the Shapes are bound to him.
 */
public class Body {

	public final int ID;
	private static int numBodies = 0;

	protected final Vec2f pos;
	protected final Vec2f vel;
	protected final Vec2f acc;
	protected float angle;
	protected float angleVel;
	protected boolean rotateByCollision;
	protected float rotationFactor;
	protected float invMass;
	protected final Vec2f centerOfMass = new Vec2f();
	protected float momentOfInertia;
	private boolean reactToGravity;

	public float getAngleVel() {
		return angleVel;
	}

	public void setAngleVel(float angleVel) {
		this.angleVel = angleVel;
	}

	protected Shape[] shapes;
	public static final int MAX_SHAPES = 10;
	protected int numShapes;

	/**
	 * 
	 * @param posX -position X
	 * @param posY -position Y
	 */
	public Body(int posX, int posY) {
		invMass = 0;
		vel = new Vec2f();
		acc = new Vec2f();
		pos = new Vec2f(posX, posY);
		shapes = new Shape[MAX_SHAPES];
		numShapes = 0;
		this.rotateByCollision = false;
		this.ID = numBodies;
		numBodies++;
		calculateCenterOfMass();
		reactToGravity = true;
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
			if (shape.getImpulseRatio() != 0) {
				float mass = 1 / shape.getImpulseRatio();
				Vec2f shapeCenterOfMass = new Vec2f(shape.getCenterOfMass());
				shapeCenterOfMass.add(shape.getRelPos());
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
			if(shape.getImpulseRatio() > 0) {
				float newShapeMoI = new Vec2f(shape.centerOfMass).add(shape.getRelPos()).sub(centerOfMass).lengthSquared();
				newShapeMoI /= shape.getImpulseRatio();
				newShapeMoI += shape.calculateMomentOfInertia();
				momentOfInertia += newShapeMoI;
			}
		}
		if(momentOfInertia == 0) {
			momentOfInertia = Float.POSITIVE_INFINITY;
		}
		return momentOfInertia;
	}

	public Vec2f getCenterOfMass() {
		return centerOfMass;
	}

	/**
	 * Adds a new Shape to the body
	 * 
	 * @param shape
	 * @return -true if successful. Otherwise false
	 */
	public boolean addShape(Shape shape) {
		if (numShapes < MAX_SHAPES) {
			shapes[numShapes++] = shape;
			updateMass();
			calculateCenterOfMass();
			calculateMomentOfInertia();
			return true;
		} else {
			return false;
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
			float shapeMass = shapeVol * shape.getMaterial().getDensity();
			mass += shapeMass;
			if(shapeMass == 0) {
				shape.setImpulseRatio(0);
			} else {
				shape.setImpulseRatio(1 / shapeMass);
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

			shapes[i].getRelPos().set(shapes[i].getRelPosAlign()).rotate(this.angle);

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
			shapes[i].getRelPos().set(shapes[i].getRelPosAlign());

			// angle is 0, so body angle is the only altered angle.
			shapes[i].rotate(0);
		}
	}

	/**
	 * Get angle
	 * 
	 * @return
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Get shape with index.
	 * 
	 * @param index
	 * @return
	 */
	public Shape getShape(int index) {
		return shapes[index];
	}

	/**
	 * Get number of shapes.
	 * 
	 * @return
	 */
	public int getNumShapes() {
		return numShapes;
	}

	/**
	 * Get position of body.
	 * 
	 * @return
	 */
	public Vec2f getPos() {
		return pos;
	}

	/**
	 * Get velocity of body.
	 * 
	 * @return
	 */
	public Vec2f getVel() {
		return vel;
	}

	/**
	 * Get acceleration of body.
	 * 
	 * @return
	 */
	public Vec2f getAcc() {
		return acc;
	}

	/**
	 * Get impulse ratio (inverted Mass) of body.
	 * 
	 * @return
	 */
	public float getImpulseRate() {
		return invMass;
	}

	public float getMomentOfInertia() {
		return momentOfInertia;
	}
	
	public void setReactToGravity(boolean b) {
		reactToGravity = b;
	}
	
	public boolean isReactToGravity() {
		return reactToGravity;
	}

	// TODO: Use for rotation by collision?
//	/**
//	 * Get rotation factor of body.
//	 * @return
//	 */
//	public float getRotationFactor() {
//		return rotationFactor;
//	}
//
//	/**
//	 * set rotation factor of body.
//	 * @param rotationFactor
//	 */
//	public void setRotationFactor(float rotationFactor) {
//		this.rotationFactor = rotationFactor;
//	}

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
}
