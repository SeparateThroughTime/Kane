package kane.math;

import static kane.renderer.ResolutionSpecification.RES_SPECS;

public class Scalar{


    public static float PI = (float) Math.PI;
    public static float TOLERANCE = 0.0001f;

    public static float pseudoUnitToScreenPercentX(float pseudoUnit){
        return pseudoUnit / RES_SPECS.gameWidth * 100;
    }

    public static float pseudoUnitToScreenPercentY(float pseudoUnit){
        return pseudoUnit / RES_SPECS.GAME_HEIGHT * 100;
    }

    public static float screenPercentXToPseudoUnit(float percent){
        return percent / 100 * RES_SPECS.gameWidth;
    }

    public static float screenPercentYToPseudoUnit(float percent){
        return percent / 100 * RES_SPECS.GAME_HEIGHT;
    }

    public static Vec2f pseudoUnitToScreenPercent(Vec2f pseudoUnit){
        return new Vec2f(pseudoUnit).div(new Vec2f(RES_SPECS.gameWidth, RES_SPECS.GAME_HEIGHT)).mult(100f);
    }

    public static Vec2f screenPercentToPseudoUnit(Vec2f percent){
        return new Vec2f(percent).div(100f).mult(new Vec2f(RES_SPECS.gameWidth, RES_SPECS.GAME_HEIGHT));
    }

    public static int getY(int y, int height){
        return height - 1 - y;
    }

    public static boolean isPointInRect(float x, float y, float x0, float y0, float x1, float y1){
        return x > x0 && y > y0 && x < x1 && y < y1;
    }

    public static boolean isPointInCircle(float x, float y, float cx, float cy, float r){
        float dx = x - cx;
        float dy = y - cy;
        float lenSquared = dx * dx + dy * dy;
        return lenSquared <= r * r;
    }

    public static float clamp(float val, float min, float max){
        return Math.max(min, Math.min(max, val));
    }

    public static int sign(float val){
        return val < 0 ? -1 : 1;
    }

    // found in:
    // https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Java
    // altered for the Vec2f Class

    /**
     * found in:
     * <a href="https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Java">...</a>
     * altered for the Vec2f Class
     */
    public static Vec2f findIntersection(Vec2f posL1, Vec2f dirL1, Vec2f posL2, Vec2f dirL2){
        Vec2f l1e = new Vec2f(posL1).add(dirL1);
        Vec2f l1s = new Vec2f(posL1).addMult(dirL1, 2);
        Vec2f l2e = new Vec2f(posL2).add(dirL2);
        Vec2f l2s = new Vec2f(posL2).addMult(dirL2, 2);

        float a1 = l1e.y - l1s.y;
        float b1 = l1s.x - l1e.x;
        float c1 = a1 * l1s.x + b1 * l1s.y;

        float a2 = l2e.y - l2s.y;
        float b2 = l2s.x - l2e.x;
        float c2 = a2 * l2s.x + b2 * l2s.y;

        float delta = a1 * b2 - a2 * b1;
        return new Vec2f((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
    }

    public static boolean equals(float v1, float v2){
        return Math.abs(v1 - v2) < TOLERANCE;
    }

    public static boolean greaterThan(float v1, float v2){
        boolean res = equals(v1, v2);
        if (res){
            return false;
        }
        return v1 > v2;
    }

    public static boolean greaterEqual(float v1, float v2){
        boolean res = equals(v1, v2);
        return res || v1 > v2;
    }

    public static boolean smallerThan(float v1, float v2){
        boolean res = equals(v1, v2);
        if (res){
            return false;
        }
        return v1 < v2;
    }

    public static boolean smallerEqual(float v1, float v2){
        boolean res = equals(v1, v2);
        return res || v1 < v2;
    }

    /**
     * Does a linear interpolation. So the new vector is a point between v1 and v2
     * depending on the factor. If factor is 0, v1 will be returned. If factor is 1,
     * v2 will be returned. If factor is 0.5, the middle of v1 and v2 will be
     * returned.
     */
    public static float lerp(float v1, float v2, float factor){
        return v1 + (v2 - v1) * factor;
    }

    public static float volumeTriangle(Vec2f a, Vec2f b, Vec2f c){
        Vec2f ab = new Vec2f(b).sub(a);
        float g = ab.length();
        Vec2f perp = new Vec2f(ab).perpRight().normalize();
        float h = new Vec2f(c).sub(a).dot(perp);
        return Math.abs(g * h * 0.5f);
    }

    public static int round(float f){
        return (int) (f + 0.5f);
    }

    public static float sin(float alpha){
        return alpha - alpha * alpha * alpha / 6 + alpha * alpha * alpha * alpha * alpha / 120
                - alpha * alpha * alpha * alpha * alpha * alpha * alpha / 5040
                + alpha * alpha * alpha * alpha * alpha * alpha * alpha * alpha * alpha / 362880;
    }

    public static float cos(float alpha){
        return 1 - alpha * alpha / 2 + alpha * alpha * alpha * alpha / 24
                - alpha * alpha * alpha * alpha * alpha * alpha / 720
                + alpha * alpha * alpha * alpha * alpha * alpha * alpha * alpha / 40320;
    }
}
