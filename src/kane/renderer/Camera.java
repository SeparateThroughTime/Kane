package kane.renderer;

import static kane.Kane.GAME;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.genericGame.JsonManager.JSON_MANAGER;

import java.util.ArrayList;

import kane.exceptions.LoadJsonException;
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
            CAMERA.loadShapes();
        }
    }

    public void loadShapes(){
        try{
            HudElement[] loadedShapes = JSON_MANAGER.loadHudElements("json\\hud\\camera.json");
            mainShape = loadedShapes[0];
            leftBox = loadedShapes[1];
            rightBox = loadedShapes[2];
            lowerBox = loadedShapes[3];
            upperBox = loadedShapes[4];
            midXBox = loadedShapes[5];
            midYBox = loadedShapes[6];


            for (HudElement element : loadedShapes){
                element.collision = false;
                element.visible = false;
            }
        } catch (LoadJsonException e){
            e.printStackTrace();
        }
    }

    private void initWindowRad(){
        this.windowRad = new Vec2f(RES_SPECS.gameWidth * 0.5f, RES_SPECS.GAME_HEIGHT * 0.5f);
    }

    private void createCamera(){
        initWindowRad();
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
