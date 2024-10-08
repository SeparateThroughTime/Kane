package kane.physics;

import kane.physics.contacts.Contact;

public class ShapePair{

    public Shape shapeA;
    public Shape shapeB;
    public Contact contact;
    public boolean penetration;
    public boolean collideable;

    public ShapePair(Shape shapeA, Shape shapeB){
        this.shapeA = shapeA;
        this.shapeB = shapeB;
        contact = null;
        collideable = true;
        penetration = false;
    }

    public void flipShapes(){
        Shape tmp = shapeA;
        shapeA = shapeB;
        shapeB = tmp;
    }

    @Override
    public String toString(){
        return "Body A: " + shapeA.body + ", Shape A: " + shapeA + ", Body B: " + shapeB.body + ", Shape B: " + shapeB;
    }
}
