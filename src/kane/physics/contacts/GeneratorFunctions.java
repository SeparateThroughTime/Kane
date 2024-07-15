package kane.physics.contacts;

import kane.math.Vec2f;

public class GeneratorFunctions{

    public static float[] projectPolygon(Vec2f perp, Vec2f[] points){
        float dotProduct, min, max;
        min = max = points[0].dot(perp);
        for (int i = 1; i < points.length; i++){
            dotProduct = points[i].dot(perp);
            min = Math.min(dotProduct, min);
            max = Math.max(dotProduct, max);
        }
        return new float[]{min, max};
    }

    public static float intervalDist(float[] minMaxA, float[] minMaxB){
        float minA = minMaxA[0];
        float maxA = minMaxA[1];
        float minB = minMaxB[0];
        float maxB = minMaxB[1];
        if (minA < minB){
            return minB - maxA;
        } else{
            return minA - maxB;
        }
    }

}
