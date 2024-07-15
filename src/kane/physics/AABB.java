package kane.physics;

import kane.math.Vec2f;

public class AABB{

    private final Vec2f min;
    private final Vec2f max;

    public AABB(Vec2f min, Vec2f max){
        this.min = min;
        this.max = max;
    }

    public AABB(){
        min = new Vec2f();
        max = new Vec2f();
    }

    public boolean overlaps(AABB other){
        if (max.x < other.getMin().x || other.getMax().x < min.x){
            return false;
        }
        return !(max.y < other.getMin().y) && !(other.getMax().y < min.y);
    }

    public Vec2f getMin(){
        return min;
    }

    public Vec2f getMax(){
        return max;
    }

    public void sortMinMax(){
        Vec2f minTmp = new Vec2f(min);
        Vec2f maxTmp = new Vec2f(max);
        min.set(Math.min(minTmp.x, maxTmp.x), Math.min(minTmp.y, maxTmp.y));
        max.set(Math.max(minTmp.x, maxTmp.x), Math.max(minTmp.y, maxTmp.y));
    }

}
