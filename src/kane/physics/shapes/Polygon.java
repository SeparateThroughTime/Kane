package kane.physics.shapes;

import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.ShapeType;

/**
 * This is a Shape of the Type Polygon. A Polygon has a minimum of 3 point.
 */
public class Polygon extends Shape {

	private final Vec2f[] points;
	private final Vec2f[] pointsAlign;
	private final int numPoints;
	private float angle;

	/**
	 * Create polygon with specified points.
	 * @param relPosX -position of x in relation to the body
	 * @param RelPosY -position of y in relation to the body
	 * @param body
	 * @param color -0xrrggbb
	 * @param points -relative points of the shape
	 * @param material
	 */
	public Polygon(int relPosX, int RelPosY, Body body, int color, Vec2f[] points, Material material) {
		super(relPosX, RelPosY, ShapeType.POLYGON, body, color, material);
		numPoints = points.length;
		this.points = new Vec2f[numPoints];
		this.pointsAlign = new Vec2f[numPoints];
		for (int i = 0; i < numPoints; i++) {
			this.points[i] = new Vec2f(points[i]);
			this.pointsAlign[i] = new Vec2f(points[i]);
		}
		this.angle = 0;
	}
	
	/**
	 * Create equilateral polygon with a number of points.
	 * @param relPosX -position of x in relation to the body
	 * @param RelPosY -position of y in relation to the body
	 * @param body
	 * @param color -0xrrggbb
	 * @param numPoints -number of points
	 * @param material
	 */
	public Polygon(int relPosX, int RelPosY, Body body, int color, int numPoints, float radius, Material material) {
		super(relPosX, RelPosY, ShapeType.POLYGON, body, color, material);
		this.numPoints = numPoints;
		this.points = new Vec2f[numPoints];
		this.pointsAlign = new Vec2f[numPoints];
		Vec2f dir = new Vec2f(1, 0);
		for (int i = 0; i < numPoints; i++) {
			this.points[i] = new Vec2f(dir).mult(radius);
			this.pointsAlign[i] = new Vec2f(points[i]);
			dir.rotate(1f / numPoints);
		}
		this.angle = 0;
	}

	@Override
	public void updateAABB(Vec2f nextAbsPos, float tolerance) {
		Vec2f absPos = getAbsPos();
		float minX, maxX, minY, maxY;
		minX = maxX = points[0].getX();
		minY = maxY = points[0].getY();
		for (int i = 0; i < points.length; i++) {
			float x = points[i].getX();
			float y = points[i].getY();
			if (x < minX) {
				minX = x;
			} else if (x > maxX) {
				maxX = x;
			}
			if (y < minY) {
				minY = y;
			} else if (y > maxY) {
				maxY = y;
			}
		}

		aabb.getMin().set(Math.min(absPos.getX(), nextAbsPos.getX()) + minX - tolerance,
				Math.min(absPos.getY(), nextAbsPos.getY()) + minY - tolerance);
		aabb.getMax().set(Math.max(absPos.getX(), nextAbsPos.getX()) + maxX + tolerance,
				Math.max(absPos.getY(), nextAbsPos.getY()) + maxY + tolerance);

	}

	@Override
	public float getVolume() {
		if (numPoints < 3) {
			return 0f;
		}
		float volume = 0;
		for (int i = 0; i < numPoints - 2; i++) {
			int j = i + 1;
			int k = i + 2;
			Vec2f pointA = points[i];
			Vec2f pointB = points[j];
			Vec2f pointC = points[k];
			assert (pointA != null);
			assert (pointB != null);
			assert (pointC != null);
			volume += Scalar.volumeTriangle(pointA, pointB, pointC);
		}

		return volume;
	}

	@Override
	/**
	 * Rotates the shape by given angle.
	 * @angle
	 */
	public void rotate(float angle) {
		// The angle is between 0 and 1
		this.angle += angle;
		float bodyAngle = body.getAngle();

		for (int i = 0; i < numPoints; i++) {
			points[i].set(pointsAlign[i]).rotate(this.angle + bodyAngle);
		}
	}

	/**
	 * align the shape to original angle in relation to body angle.
	 */
	public void align() {
		float bodyAngle = body.getAngle();
		for (int i = 0; i < numPoints; i++) {
			points[i].set(pointsAlign[i]);
			points[i].rotate(bodyAngle);
		}
		angle = 0;
	}

	/**
	 * Get point with index.
	 * @param index
	 * @return -point with specific index
	 */
	public Vec2f getPoint(int index) {
		return points[index];
	}

	/**
	 * Get the number of points.
	 * @return
	 */
	public int getNumPoints() {
		return numPoints;
	}

	/**
	 * Get all points.
	 * @return -Array of points
	 */
	public Vec2f[] getPoints() {
		return points;
	}

}
