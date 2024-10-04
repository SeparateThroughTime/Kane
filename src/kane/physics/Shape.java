package kane.physics;

import static kane.renderer.Renderer.RENDERER;

import java.awt.Color;
import java.util.ArrayList;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.math.Vec2f;
import kane.math.Vec2i;
import kane.renderer.SpriteBatch;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public abstract class Shape{

    public static final int MAX_RENDER_LAYER = 20;
    public static int MAX_COLLIDED_SHAPES = 50;
    public static int MAX_ACTIVE_ATTRIBUTES = 5;
    public static int MAX_PASSIVE_ATTRIBUTES = 5;
    public static int MAX_SPRITE_CONTROLLERS = 10;

    public final int ID;

    public int renderLayer;

    public final Vec2f relPos;
    public final ShapeType type;
    public final AABB aabb;
    public final Body body;
    public Color color;
    public boolean collision;
    public boolean visible;
    public final Material material;
    public float invMass;
    public final Vec2f centerOfMass;
    protected Shape[] collidedShapes;
    protected int numCollidedShapes;

    public ActiveAttributes[] activeAttributes;
    public PassiveAttributes[] passiveAttributes;
    public int numActiveAttributes;
    public int numPassiveAttributes;

    public boolean hasSprite;
    protected SpriteController[] spriteControllers;

    public final int numRenderVertices;
    public final int numRenderElements;
    public SpriteBatch renderBatch;

    public abstract void updateAABB(Vec2f nextAbsPos, float tolerance);

    public abstract float getVolume();

    public abstract boolean isPointInShape(Vec2f point);

    public boolean isPointInShape(Vec2i point){
        Vec2f pointF = new Vec2f(point);
        return isPointInShape(pointF);
    }

    public Shape(float relPosX, float RelPosY, ShapeType type, Body body, Color color, Material material, int renderLayer,
                 int numRenderVertices, int numRenderElements){
        relPos = new Vec2f(relPosX, RelPosY);
        this.type = type;
        aabb = new AABB();
        this.body = body;
        this.color = color;
        this.ID = body.numShapes;
        this.collision = true;
        this.visible = true;
        this.numActiveAttributes = 0;
        this.numPassiveAttributes = 0;
        this.activeAttributes = new ActiveAttributes[MAX_ACTIVE_ATTRIBUTES];
        this.passiveAttributes = new PassiveAttributes[MAX_PASSIVE_ATTRIBUTES];
        this.material = material;
        this.hasSprite = false;
        this.centerOfMass = new Vec2f();
        this.renderLayer = Math.min(renderLayer, MAX_RENDER_LAYER);
        this.collidedShapes = new Shape[MAX_COLLIDED_SHAPES];
        this.numRenderVertices = numRenderVertices;
        this.numRenderElements = numRenderElements;
        this.spriteControllers = new SpriteController[0];
    }

    public void rotate(float angle){
    }

    public void rotate(float angle, Vec2f referencePoint){

    }

    public void addActiveAttribute(ActiveAttributes aa){
        activeAttributes[numActiveAttributes++] = aa;
    }

    public void remActiveAttribute(ActiveAttributes aa){
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numActiveAttributes; i++){
            if (activeAttributes[i] == aa){
                indices.add(i);
            }
        }
        for (int index : indices){
            for (int j = index + 1; j < numActiveAttributes; j++){
                activeAttributes[j - 1] = activeAttributes[j];
            }
            numActiveAttributes--;
        }
    }

    public ActiveAttributes getActiveAttribute(int index){
        return activeAttributes[index];
    }

    public boolean hasActiveAttribute(ActiveAttributes a){
        boolean b = false;
        for (int i = 0; i < numActiveAttributes; i++){
            if (activeAttributes[i] == a){
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean hasPassiveAttribute(PassiveAttributes a){
        boolean b = false;
        for (int i = 0; i < numPassiveAttributes; i++){
            if (passiveAttributes[i] == a){
                b = true;
                break;
            }
        }
        return b;
    }

    public void addPassiveAttribute(PassiveAttributes pa){
        passiveAttributes[numPassiveAttributes++] = pa;
    }

    public void remPassiveAttribute(PassiveAttributes pa){
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numPassiveAttributes; i++){
            if (passiveAttributes[i] == pa){
                indices.add(i);
            }
        }
        for (int index : indices){
            for (int j = index + 1; j < numPassiveAttributes; j++){
                passiveAttributes[j - 1] = passiveAttributes[j];
            }
            numPassiveAttributes--;
        }
    }

    public PassiveAttributes getPassiveAttribute(int index){
        return passiveAttributes[index];
    }

    public void setSpriteControllers(SpriteController[] spriteControllers){
        if (spriteControllers.length > MAX_SPRITE_CONTROLLERS){
            throw new IndexOutOfBoundsException("ERROR: Shape has to many SpriteController!");
        }

        for (SpriteController spriteController : this.spriteControllers){
            spriteController.stopAnimation();
        }

        this.spriteControllers = spriteControllers;

        for (SpriteController spriteController : spriteControllers){
            spriteController.startAnimation();
        }

        if (!hasSprite){
            hasSprite = true;
            RENDERER.addShape(this);

        }

        renderBatch.addSpriteControllers(spriteControllers);
    }

    public SpriteController[] getSpriteControllers(){
        return spriteControllers;
    }

    public void setCurrentSpriteState(SpriteState spriteState){
        for (SpriteController spriteController : spriteControllers){
            spriteController.setCurrentSpriteState(spriteState);
        }
    }

    public SpriteState getCurrentSpriteState(){
        if (hasSprite){
            return spriteControllers[0].currentSpriteState;
        } else{
            return null;
        }
    }

    @Override
    public String toString(){
        return "" + ID;
    }

    protected abstract void mirrorX();

    protected abstract void mirrorY();

    public Shape[] getCollidedShapes(){
        return ArrayOperations.cutArray(collidedShapes, numCollidedShapes);
    }

    public void addCollidedShape(Shape shape){
        this.collidedShapes[numCollidedShapes++] = shape;
    }

    public void remCollidedShape(Shape shape){
        this.numCollidedShapes -= ArrayOperations.remObjectStatic(collidedShapes, shape, numCollidedShapes);
    }

    public Shape[] getCollidedShapes(ActiveAttributes attribute){
        Shape[] shapes = ArrayOperations.cutArray(collidedShapes, numCollidedShapes);
        int removedShapes = 0;
        for (Shape shape : shapes){
            if (!shape.hasActiveAttribute(attribute)){
                removedShapes += ArrayOperations.remObjectStatic(shapes, shape, numCollidedShapes);
            }
        }
        return ArrayOperations.cutArray(shapes, numCollidedShapes - removedShapes);
    }

    public Shape[] getCollidedShapes(PassiveAttributes attribute){
        Shape[] shapes = ArrayOperations.cutArray(collidedShapes, numCollidedShapes);
        int removedShapes = 0;
        for (int i = 0; i < numCollidedShapes - removedShapes; i++){
            Shape shape = shapes[i];
            if (!shape.hasPassiveAttribute(attribute)){
                ArrayOperations.remSingleObjectStatic(shapes, i--, numCollidedShapes - removedShapes++);
            }
        }
        return ArrayOperations.cutArray(shapes, numCollidedShapes - removedShapes);
    }

    public Vec2f getAbsPos(){
        return new Vec2f(body.pos).add(relPos);
    }
}
