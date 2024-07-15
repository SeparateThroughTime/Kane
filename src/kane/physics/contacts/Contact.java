package kane.physics.contacts;

import kane.math.Vec2f;

public class Contact{

    public final Vec2f normal;
    public final float distance;
    public final Vec2f point;
    public final Vec2f supportA;
    public final Vec2f supportB;

    public float impulse;

    public Contact(Vec2f normal, float distance, Vec2f point){
        this.normal = new Vec2f(normal);
        this.distance = distance;
        this.point = new Vec2f(point);
        this.supportA = new Vec2f();
        this.supportB = new Vec2f();
        impulse = 0f;
    }
}
