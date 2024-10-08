package kane.renderer;

import static kane.Kane.GAME;
import static kane.renderer.ResolutionSpecification.RES_SPECS;

import java.util.ArrayList;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.hud.HudElement;
import kane.genericGame.gameEvent.camera.BindCameraToMap;
import kane.genericGame.gameEvent.camera.MoveCameraDown;
import kane.genericGame.gameEvent.camera.MoveCameraLeft;
import kane.genericGame.gameEvent.camera.MoveCameraRight;
import kane.genericGame.gameEvent.camera.MoveCameraUp;
import kane.genericGame.gameEvent.camera.SlowCameraX;
import kane.genericGame.gameEvent.camera.SlowCameraY;
import kane.genericGame.hud.HudBar;
import kane.math.Vec2f;
import kane.physics.AABB;
import kane.physics.Body;

public class Camera extends Body{

    public static Camera CAMERA;

    public AABB window;
    public AABB aiRange;
    public Vec2f windowRad;
    public Vec2f zeroPoint;
    public Vec2f movementAccX;
    public Vec2f movementAccY;
    public int movementSpeedY;
    private final ArrayList<HudBar> hudBars = new ArrayList<>();

    public HudElement mainShape;
    public HudElement leftBox;
    public HudElement rightBox;
    public HudElement lowerBox;
    public HudElement upperBox;
    public HudElement midXBox;
    public HudElement midYBox;

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
            CAMERA.createShapes();
        }
    }

    public void createShapes(){
        createMainShape();
        createLeftBox();
        createRightBox();
        createLowerBox();
        createUpperBox();
        createMidXBox();
        createMidYBox();
    }

    private void initWindowRad(){
        this.windowRad = new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.5f);
    }

    private void createCamera(){
        initWindowRad();
    }

    private void createMidYBox(){
        midYBox = new HudElement(new Vec2f(0, 0), new Vec2f(50, 12.5f), 0, true);
        addShape(midYBox);
        GAME.addGuiElement(midYBox);
        midYBox.collision = false;
        midYBox.addActiveAttribute(ActiveAttributes.CAMERA_MID_Y);
        midYBox.visible = false;
    }

    private void createMidXBox(){
        midXBox = new HudElement(new Vec2f(0, 0), new Vec2f(12.5f, 50), 0, true);
        addShape(midXBox);
        GAME.addGuiElement(midXBox);
        midXBox.collision = false;
        midXBox.addActiveAttribute(ActiveAttributes.CAMERA_MID_X);
        midXBox.visible = false;
    }

    private void createUpperBox(){
        upperBox = new HudElement(new Vec2f(0, 31.25f), new Vec2f(0.5f, 18.75f), 0, true);
        addShape(upperBox);
        GAME.addGuiElement(upperBox);
        upperBox.collision = false;
        upperBox.addActiveAttribute(ActiveAttributes.CAMERA_UP);
        upperBox.visible = false;
    }

    private void createLowerBox(){
        lowerBox = new HudElement(new Vec2f(0, -32.25f), new Vec2f(50, 18.75f), 0, true);
        addShape(lowerBox);
        GAME.addGuiElement(lowerBox);
        lowerBox.collision = false;
        lowerBox.addActiveAttribute(ActiveAttributes.CAMERA_DOWN);
        lowerBox.visible = false;
    }

    private void createRightBox(){
        rightBox = new HudElement(new Vec2f(31.25f, 0), new Vec2f(18.75f, 50), 0, true);
        addShape(rightBox);
        GAME.addGuiElement(rightBox);
        rightBox.collision = false;
        rightBox.addActiveAttribute(ActiveAttributes.CAMERA_RIGHT);
        rightBox.visible = false;
    }

    private void createLeftBox(){
        leftBox = new HudElement(new Vec2f(-31.25f, 0), new Vec2f(18.75f, 50), 0, true);
        addShape(leftBox);
        GAME.addGuiElement(leftBox);
        leftBox.collision = false;
        leftBox.addActiveAttribute(ActiveAttributes.CAMERA_LEFT);
        leftBox.visible = false;
    }

    private void createMainShape(){
        mainShape = new HudElement(new Vec2f(0, 0), new Vec2f(0, 0), 0, true);
        addShape(mainShape);
        GAME.addGuiElement(mainShape);
        mainShape.collision = false;
        mainShape.visible = false;
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
        initWindowRad();
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

}
