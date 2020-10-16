package kane.physics.contacts;

import kane.math.Vec2f;

/**
 * Diverse functions for contactGenerators.
 */
public class GeneratorFunctions {

	/**
	 * Calculate nearest and farest point of the polygon, in direction a specific direction
	 * @param perp -direction
	 * @param points
	 * @return -a float array where the first value is the nearest and the second value is the farest.
	 */
	public static float[] projectPolygon(Vec2f perp, Vec2f[] points) {
		float dotProduct, min, max;
		dotProduct = min = max = points[0].dot(perp);
		for (int i = 1; i < points.length; i++) {
			dotProduct = points[i].dot(perp);
			min = dotProduct > min ? min : dotProduct;
			max = dotProduct < max ? max : dotProduct;
		}
		return new float[] {min, max};
	}
	
	/**
	 * Calculate the distance between two intervals.
	 * @param minMaxA
	 * @param minMaxB
	 * @return
	 */
	public static float intervalDist(float[] minMaxA, float[] minMaxB) {
		float minA = minMaxA[0];
		float maxA = minMaxA[1];
		float minB = minMaxB[0];
		float maxB = minMaxB[1];
		if (minA < minB) {
			return minB - maxA;
		}
		else {
			return minA - maxB;
		}
	}

}
