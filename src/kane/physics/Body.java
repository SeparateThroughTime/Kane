package kane.physics;

import kane.math.Vec2f;

public class Body {
//This is a abstract class for all shapes.

	protected final Vec2f pos;
	protected final Vec2f vel;
	protected final Vec2f acc;
	protected float angle;
	protected boolean rotateByCollision;
	protected float rotationFactor;
	protected float invMass;
	protected final Material material;

	protected final Shape[] shapes;
	protected final int MAX_SHAPES;
	protected int numShapes;

	public Body(int posX, int posY, Material material) {
		this.material = material;
		invMass = 0;
		vel = new Vec2f();
		acc = new Vec2f();
		pos = new Vec2f(posX, posY);
		MAX_SHAPES = 5;
		shapes = new Shape[MAX_SHAPES];
		numShapes = 0;
		this.rotateByCollision = false;
	}
	
	public Body(int posX, int posY, Material material, boolean rotateByCollision){
		this(posX, posY, material);
		this.rotateByCollision = rotateByCollision;
	}
	
	


	public boolean addShape(Shape shape) {
		if (numShapes < MAX_SHAPES) {
			shapes[numShapes++] = shape;
			updateMass();
			return true;
		} else {
			return false;
		}
	}

	public void updateMass() {
		boolean staticObj = false;
		float bodyVol = 0;
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			float shapeVol = shape.getVolume();
			bodyVol += shapeVol;
			if (shapeVol == 0) {
				invMass = 0;
				staticObj = true;
				break;
			}
		}
		if (staticObj) {
			invMass = 0;
		} else {
			float mass = bodyVol * material.getDensity();
			invMass = 1 / mass;
		}
	}

	public void updateAABB(Vec2f nextPos, float tolerance) {
		for (int i = 0; i < numShapes; i++) {
			Shape shape = shapes[i];
			Vec2f posDif = new Vec2f(nextPos).sub(pos);
			Vec2f nextAbsPos = new Vec2f(shape.getAbsPos()).add(posDif);
			shape.updateAABB(nextAbsPos, tolerance);
		}
	}
	
	public void rotate(float angle) {
		// Angle is between 0 and 1
		// Only Polygons and Circles are rotateable.
		// Boxes will move with the rotation but wont rotate.
		// Static shapes wont move.
		this.angle += angle;
		
		for (int i = 0; i < numShapes; i++) {
			shapes[i].getRelPos().set(shapes[i].getRelPosAlign()).rotate(this.angle);
			
			// angle is 0, so body angle is the only altered angle.
			shapes[i].rotate(0);
		}
	}
	
	public void align() {
		this.angle = 0;
		for (int i = 0; i < numShapes; i++) {
			shapes[i].getRelPos().set(shapes[i].getRelPosAlign());
			
			// angle is 0, so body angle is the only altered angle.
			shapes[i].rotate(0);
		}
	}
	
	public float getAngle() {
		return angle;
	}

	public Shape getShape(int index) {
		return shapes[index];
	}

	public int getNumShapes() {
		return numShapes;
	}

	public Vec2f getPos() {
		return pos;
	}

	public Vec2f getVel() {
		return vel;
	}

	public Vec2f getAcc() {
		return acc;
	}

	public float getImpulseRatio() {
		return invMass;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public float getRotationFactor() {
		return rotationFactor;
	}
	
	public void setRotationFactor(float rotationFactor) {
		this.rotationFactor = rotationFactor;
	}
}
