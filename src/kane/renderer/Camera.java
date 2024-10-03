package kane.renderer;

import static kane.Kane.GAME;
import static kane.genericGame.hud.Inventory.INVENTORY;
import static kane.renderer.ResolutionSpecification.RES_SPECS;

import java.awt.Color;
import java.util.ArrayList;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.gameEvent.camera.BindCameraToMap;
import kane.genericGame.gameEvent.camera.MoveCameraDown;
import kane.genericGame.gameEvent.camera.MoveCameraLeft;
import kane.genericGame.gameEvent.camera.MoveCameraRight;
import kane.genericGame.gameEvent.camera.MoveCameraUp;
import kane.genericGame.gameEvent.camera.SlowCameraX;
import kane.genericGame.gameEvent.camera.SlowCameraY;
import kane.genericGame.hud.HudBar;
import kane.genericGame.hud.Inventory;
import kane.math.Vec2f;
import kane.physics.AABB;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Shape;
import kane.physics.shapes.Box;
import kane.physics.shapes.Point;

public class Camera extends Body{

    public static Camera CAMERA;

    private final Material mInterface = new Material(1, 0);
    public AABB window;
    public AABB aiRange;
    public Vec2f windowRad;
    public Vec2f zeroPoint;
    public Vec2f movementAccX;
    public Vec2f movementAccY;
    public int movementSpeedY;
    private final ArrayList<HudBar> hudBars = new ArrayList<>();

    private Camera(boolean forGame){
        super((int) RES_SPECS.halfGameWidth, (int) RES_SPECS.halfGameHeight);

        if(forGame){
            movementAccX = new Vec2f(GAME.player.getWalkAcc()).mult(0.5f);
            movementAccY = new Vec2f(movementAccX).perpLeft();
            movementSpeedY = GAME.player.getWalkSpeed() * 2;
        }

        this.zeroPoint = new Vec2f();
        reactToGravity = false;
        createCamera();
    }


    public static void initializeCamera(boolean forGame){
        if (CAMERA == null){
            CAMERA = new Camera(forGame);
        }
    }

    private void createCamera(){
        this.windowRad = new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.5f);

        createMainShape();
        createLeftBox();
        createRightBox();
        createLowerBox();
        createUpperBox();
        createMidXBox();
        createMidYBox();
    }

    private void createMidYBox(){
        addShape(new Box(0, 0, this, new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.125f), Color.RED,
                mInterface, 0));
        shapes[6].collision = false;
        shapes[6].addActiveAttribute(ActiveAttributes.CAMERA_MID_Y);
        shapes[6].visible = false;
    }

    private void createMidXBox(){
        addShape(new Box(0, 0, this, new Vec2f(RES_SPECS.gameWidth * 0.125f, RES_SPECS.GAME_HEIGHT * 0.5f), Color.RED,
                mInterface, 0));
        shapes[5].collision = false;
        shapes[5].addActiveAttribute(ActiveAttributes.CAMERA_MID_X);
        shapes[5].visible = false;
    }

    private void createUpperBox(){
        addShape(new Box(0, (int) (RES_SPECS.GAME_HEIGHT * 0.3125f), this,
                new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.1875f), Color.GREEN, mInterface, 0));
        shapes[4].collision = false;
        shapes[4].addActiveAttribute(ActiveAttributes.CAMERA_UP);
        shapes[4].visible = false;
    }

    private void createLowerBox(){
        addShape(new Box(0, -(int) (RES_SPECS.GAME_HEIGHT * 0.3125f), this,
                new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.1875f), Color.GREEN, mInterface, 0));
        shapes[3].collision = false;
        shapes[3].addActiveAttribute(ActiveAttributes.CAMERA_DOWN);
        shapes[3].visible = false;
    }

    private void createRightBox(){
        addShape(new Box((int) (RES_SPECS.gameWidth * 0.3125f), 0, this,
                new Vec2f(RES_SPECS.gameWidth * 0.1875f, RES_SPECS.GAME_HEIGHT * 0.5f), Color.GREEN, mInterface, 0));
        shapes[2].collision = false;
        shapes[2].addActiveAttribute(ActiveAttributes.CAMERA_RIGHT);
        shapes[2].visible = false;
    }

    private void createLeftBox(){
        addShape(new Box(-(int) (RES_SPECS.gameWidth * 0.3125f), 0, this,
                new Vec2f(RES_SPECS.gameWidth * 0.1875f, RES_SPECS.GAME_HEIGHT * 0.5f), Color.GREEN, mInterface, 0));
        shapes[1].collision = false;
        shapes[1].addActiveAttribute(ActiveAttributes.CAMERA_LEFT);
        shapes[1].visible = false;
    }

    private void createMainShape(){
        addShape(new Point(0, 0, this, Color.BLUE, mInterface, 0));
        shapes[0].collision = false;
        shapes[0].visible = false;
    }

    public void update(){
        Vec2f min = new Vec2f(pos).sub(windowRad);
        Vec2f max = new Vec2f(pos).add(windowRad);
        Vec2f aiMin = new Vec2f(min).add(100, 100);
        Vec2f aiMax = new Vec2f(max).add(100, 100);
        window = new AABB(min, max);
        aiRange = new AABB(aiMin, aiMax);
        zeroPoint = min;
    }

    public void changeResolution(){
        clearBody();
        createCamera();
        INVENTORY.changeResolution();
    }

    public void bindCameraToMap(){
        GAME.addEvent(new BindCameraToMap());
    }

    public void moveCameraLeft(){
        GAME.addEvent(new MoveCameraLeft());
    }

    public void moveCameraRight(){
        GAME.addEvent(new MoveCameraRight());
    }

    public void moveCameraUp(){
        GAME.addEvent(new MoveCameraUp());
    }

    public void moveCameraDown(){
        GAME.addEvent(new MoveCameraDown());
    }

    public void SlowCameraX(){
        GAME.addEvent(new SlowCameraX());
    }

    public void SlowCameraY(){
        GAME.addEvent(new SlowCameraY());
    }

    public void initInventory(){
        Shape mainShape = addShape(new Point(0, 0, this, Color.BLUE, mInterface, 3));
        Shape[] slotShapes = new Shape[8];
        slotShapes[0] = addShape(new Box(-144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        slotShapes[1] = addShape(new Box(-48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        slotShapes[2] = addShape(new Box(48, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        slotShapes[3] = addShape(new Box(144, 48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        slotShapes[4] = addShape(new Box(-144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        slotShapes[5] = addShape(new Box(-48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        slotShapes[6] = addShape(new Box(48, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        slotShapes[7] = addShape(new Box(144, -48, this, new Vec2f(32, 32), Color.RED, mInterface, 4));
        Inventory.initializeInventory(mainShape, slotShapes, RES_SPECS);
    }

    public HudBar addHudBar(String filepath){
        int hudPos = hudBars.size();
        Shape hudShape = addShape(new Point(-RES_SPECS.gameWidth / 2 + HudBar.HUD_HEIGHT + HudBar.HUD_WIDTH / 2,
                -RES_SPECS.GAME_HEIGHT / 2 + RES_SPECS.GAME_HEIGHT - (int) (HudBar.HUD_HEIGHT * (hudPos + 1) * 1.5),
                this, Color.BLUE, mInterface, 3));
        HudBar hudBar = new HudBar(filepath, hudShape);

        hudBars.add(hudBar);
        return hudBar;

    }

}
