package kane.physics;

import kane.math.Vec2f;

/**
 * A Body can have many Shapes.
 * The Body is the moving (or not moving) object in the world and the Shapes are bound to him.
 */
public class Body {

	public final int ID;
	private static int numBodies = 0;

	protected final Vec2f pos;
	protected final Vec2f vel;
	protected final Vec2f acc;
	protected float angle;
	protected boolean rotateByCollision;
	protected float rotationFactor;
	protected float invMass;

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
	}

	/**
	 * 
	 * @param posX -position X
	 * @param posY -position Y
	 * @param rotateByCollision -boolean, if body can rotate when it collides
	 */
	public Body(int posX, int posY, boolean rotateByCollision) {
		this(posX, posY);
		this.rotateByCollision = rotateByCollision;
	}

	/**
	 * Adds a new Shape to the body
	 * @param shape
	 * @return -true if successful. Otherwise false
	 */
	public boolean addShape(Shape shape) {
		if (numShapes < MAX_SHAPES) {
			shapes[numShapes++] = shape;
			updateMass();
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
			mass += shapeVol * shape.getMaterial().getDensity();
		}
		if (mass == 0) {
			invMass = 0;
		} else {
			invMass = 1 / mass;
		}
	}

	/**
	 * update AABB of the body, including its next position.
	 * @param nextPos -next position
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
	 * Rotate the body and all rotatable Shapes.
	 * Angle is between 0 and 1.
	 * Only Polygons and Cricles are roratable.
	 * Static shapes wont move.
	 * @param angle -angle between 0 an 1
	 */
	public void rotate(float angle) {
		this.angle += angle;

		for (int i = 0; i < numShapes; i++) {
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
	 * @return
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Get shape with index.
	 * @param index
	 * @return
	 */
	public Shape getShape(int index) {
		return shapes[index];
	}

	/**
	 * Get number of shapes.
	 * @return
	 */
	public int getNumShapes() {
		return numShapes;
	}

	/**
	 * Get position of body.
	 * @return
	 */
	public Vec2f getPos() {
		return pos;
	}

	/**
	 * Get velocity of body.
	 * @return
	 */
	public Vec2f getVel() {
		return vel;
	}

	/**
	 * Get acceleration of body.
	 * @return
	 */
	public Vec2f getAcc() {
		return acc;
	}

	/**
	 * Get impulse ratio (inverted Mass) of body.
	 * @return
	 */
	public float getImpulseRatio() {
		return invMass;
	}

	//TODO: Use for rotation by collision?
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
	 * Reset the number of bodies
	 * ONLY USE IF YOU ARE DELETING ALL BODIES!
	 */
	public static void resetNumBodies() {
		numBodies = 0;
	}
}
