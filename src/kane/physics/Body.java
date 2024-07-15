package kane.physics;

import static kane.physics.Physics.PHYSICS;
import static kane.genericGame.ResourceManager.RESOURCE_MANAGER;

import kane.exceptions.LoadSoundException;
import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.math.Vec2f;
import kane.renderer.SpriteState;
import kane.sound.SoundBuffer;
import kane.sound.SoundSource;
import kane.sound.SoundType;

import java.util.HashMap;

public class Body{

    public final int ID;

    public final Vec2f pos;
    public final Vec2f vel;
    public final Vec2f acc;
    public float angle;
    public float angleVel;
    public boolean rotateByCollision;
    public float rotationFactor;
    public float invMass;
    public final Vec2f centerOfMass = new Vec2f();
    public float momentOfInertia;
    public boolean reactToGravity;
    private boolean removed;

    public Shape[] shapes;
    public static final int MAX_SHAPES = 20;
    public int numShapes;

    public HashMap<SoundType, SoundSource> soundSources;

    public Body(int posX, int posY){
        this(new Vec2f(posX, posY));
    }

    public Body(Vec2f pos){
        invMass = 0;
        vel = new Vec2f();
        acc = new Vec2f();
        this.pos = pos;
        shapes = new Shape[MAX_SHAPES];
        numShapes = 0;
        this.rotateByCollision = false;
        this.ID = PHYSICS.numBodies;
        calculateCenterOfMass();
        reactToGravity = true;
        removed = false;
        PHYSICS.addBody(this);
        soundSources = new HashMap<>();
    }

    public Body(int posX, int posY, boolean rotateByCollision){
        this(posX, posY);
        this.rotateByCollision = rotateByCollision;
    }

    public void calculateCenterOfMass(){
        centerOfMass.zero();
        float sumOfMass = 0;
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            if (shape.invMass != 0){
                float mass = 1 / shape.invMass;
                Vec2f shapeCenterOfMass = new Vec2f(shape.centerOfMass);
                shapeCenterOfMass.add(shape.relPos);
                centerOfMass.addMult(shapeCenterOfMass, mass);
                sumOfMass += mass;
            }
        }
        if (sumOfMass > 0.f){
            centerOfMass.div(sumOfMass);
        }
    }

    public float calculateMomentOfInertia(){
        momentOfInertia = 0;
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            if (shape.invMass > 0){
                float newShapeMoI = new Vec2f(shape.centerOfMass).add(shape.relPos).sub(centerOfMass).lengthSquared();
                newShapeMoI /= shape.invMass;
                newShapeMoI += shape.momentOfInertia;
                momentOfInertia += newShapeMoI;
            }
        }
        if (momentOfInertia == 0){
            momentOfInertia = Float.POSITIVE_INFINITY;
        }
        return momentOfInertia;
    }

    public Shape addShape(Shape shape){
        if (numShapes < MAX_SHAPES){
            shapes[numShapes++] = shape;
            updateMass();
            calculateCenterOfMass();
            calculateMomentOfInertia();
            return shape;
        } else{
            return null;
        }
    }

    public void clearBody(){
        shapes = new Shape[MAX_SHAPES];
        numShapes = 0;
    }

    public void updateMass(){
        float mass = 0;
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            float shapeVol = shape.getVolume();
            float shapeMass = shapeVol * shape.material.density();
            mass += shapeMass;
            if (shapeMass == 0){
                shape.invMass = 0;
            } else{
                shape.invMass = 1 / shapeMass;
            }
        }
        if (mass == 0){
            invMass = 0;
        } else{
            invMass = 1 / mass;
        }
    }

    public void updateAABB(Vec2f nextPos, float tolerance){
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            Vec2f posDif = new Vec2f(nextPos).sub(pos);
            Vec2f nextAbsPos = new Vec2f(shape.getAbsPos()).add(posDif);
            shape.updateAABB(nextAbsPos, tolerance);
        }
    }

    public void rotate(float angle){
        this.angle += angle;

        for (int i = 0; i < numShapes; i++){
            //			shapes[i].rotate(angle, new Vec2f(getCenterOfMass()).add(getPos()));

            shapes[i].relPos.set(shapes[i].relPosAlign).rotate(this.angle);

            // angle is 0, so body angle is the only altered angle.
            shapes[i].rotate(0);
        }
    }

    public void align(){
        this.angle = 0;
        for (int i = 0; i < numShapes; i++){
            shapes[i].relPos.set(shapes[i].relPosAlign);

            // angle is 0, so body angle is the only altered angle.
            shapes[i].rotate(0);
        }
    }

    public Shape getShape(ActiveAttributes activeAttribute){
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            if (shape.hasActiveAttribute(activeAttribute)){
                return shape;
            }
        }
        return null;
    }

    public Shape getShape(PassiveAttributes passiveAttribute){
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            if (shape.hasPassiveAttribute(passiveAttribute)){
                return shape;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return "" + ID;
    }

    public void mirrorX(){
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            Vec2f relPos = shape.relPos;
            shape.relPos.set(-relPos.x, relPos.y);

            shape.mirrorX();
        }
    }

    public void mirrorY(){
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            Vec2f relPos = shape.relPos;
            shape.relPos.set(relPos.x, -relPos.y);

            shape.mirrorY();
        }
    }

    public boolean hasShapeWithPassiveAttribute(PassiveAttributes passiveA){
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            if (shape.hasPassiveAttribute(passiveA)){
                return true;
            }
        }
        return false;
    }

    public boolean hasShapeWithActiveAttribute(ActiveAttributes activeA){
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            if (shape.hasActiveAttribute(activeA)){
                return true;
            }
        }
        return false;
    }

    private Shape[] getSpriteShapes(){
        Shape[] spriteShapes = new Shape[0];
        for (int i = 0; i < numShapes; i++){
            Shape shape = shapes[i];
            if (shape.hasSprite){
                spriteShapes = ArrayOperations.add(spriteShapes, shape);
            }
        }
        return spriteShapes;
    }

    public void setCurrentSpriteState(SpriteState state){
        Shape[] spriteShapes = getSpriteShapes();
        for (Shape shape : spriteShapes){
            shape.setCurrentSpriteState(state);
        }
    }

    public void remove(){
        removed = true;
    }

    public boolean isRemoved(){
        return removed;
    }

    public SoundSource getSoundSource(SoundType soundType){
        if (!soundSources.containsKey(soundType)){
            return null;
        }

        return soundSources.get(soundType);
    }

    public void updateSoundSourcePos(){
        for (SoundType soundType : SoundType.values()){
            if (!soundSources.containsKey(soundType)){
                continue;
            }
            soundSources.get(soundType).setPos(pos);
        }
    }

    public void addSoundSource(String soundFile, SoundType soundType){

        SoundBuffer soundBuffer;
        try{
            soundBuffer = RESOURCE_MANAGER.getSoundBuffer(soundFile);
        } catch (LoadSoundException e){
            e.printStackTrace();
            return;
        }

        if (soundSources.containsKey(soundType)){
            soundSources.get(soundType).cleanUp();
        }

        boolean loop = soundType == SoundType.WALK;
        SoundSource soundSource = new SoundSource(loop, false, true);
        soundSource.setBuffer(soundBuffer.getBufferId());

        soundSources.put(soundType, soundSource);
    }

}
