package kane.genericGame;

import com.google.gson.Gson;
import kane.genericGame.hud.HudElement;
import kane.genericGame.hud.HudBar;
import kane.genericGame.userInteraction.*;
import kane.physics.Body;
import kane.physics.Physics;
import kane.renderer.*;
import kane.sound.SoundEngine;

import static kane.genericGame.hud.Inventory.INVENTORY;
import static kane.genericGame.userInteraction.Keyboard.KEYBOARD;
import static kane.genericGame.userInteraction.Mouse.MOUSE;
import static kane.physics.Physics.PHYSICS;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.sound.SoundEngine.SOUND;
import static org.lwjgl.glfw.GLFW.*;

public abstract class Game implements KeyboardInterface, MouseInterface, ContactManagementInterface{

    public static final float BACKGROUND_SPEED = 0.5f;

    public int mapLen;
    public int mapHeight;
    public final String title;

    private Gson gson;

    public final static long TARGET_FPS = 60;
    public final static long SECOND = 1000000000;
    public final static long MILLI_SECOND = 1000000;
    public final static long NANO_SECOND_FPS = SECOND / TARGET_FPS;
    public final static float DELTATIME = 1.0f / TARGET_FPS;
    public static long time = 0;

    public boolean pause;

    protected GameEvent[] events = new GameEvent[5000];
    protected int numEvents = 0;

    public HudBar healthBar;
    public Mob player;
    public HudElement[] hudElements = new HudElement[100];
    public int numElements = 0;

    private float sanity;

    protected abstract void initGame();

    protected abstract void mechanicsLoop();

    protected abstract void postMechanicsLoops();

    protected Game(String title){
        this.title = title;
        JsonManager.initJsonManager();
        ResolutionSpecification.initializeResSpecs(600, 800, 600, 800);
        Physics.initializePhysics(this);
        Renderer.initializeRenderer(title);
        RENDERER.initPostProcessBatch();
        long window = RENDERER.window;
        Keyboard.initializeKeyboard(this, window);
        Mouse.initializeMouse(this, window);
        SoundEngine.initializeSound();
    }

    public void changeResolution(Resolution res){
        switch (res){
            case SOL800x600:
                RES_SPECS.width = 800;
                RES_SPECS.height = 600;
                break;
            case SOL1024x768:
                RES_SPECS.width = 1024;
                RES_SPECS.height = 768;
                break;
            case SOL1152x864:
                RES_SPECS.width = 1152;
                RES_SPECS.height = 864;
                break;
            case SOL1280x960:
                RES_SPECS.width = 1280;
                RES_SPECS.height = 960;
                break;
            case SOL1280x768:
                RES_SPECS.width = 1280;
                RES_SPECS.height = 768;
                break;
            case SOL1280x1024:
                RES_SPECS.width = 1280;
                RES_SPECS.height = 1024;
                break;
            case SOL1280x800:
                RES_SPECS.width = 1280;
                RES_SPECS.height = 800;
                break;
            case SOL1680x1050:
                RES_SPECS.width = 1680;
                RES_SPECS.height = 1050;
                break;
            case SOL1176x664:
                RES_SPECS.width = 1176;
                RES_SPECS.height = 664;
                break;
            case SOL1280x720:
                RES_SPECS.width = 1280;
                RES_SPECS.height = 720;
                break;
            case SOL1360x768:
                RES_SPECS.width = 1360;
                RES_SPECS.height = 768;
                break;
            case SOL1366x768:
                RES_SPECS.width = 1366;
                RES_SPECS.height = 768;
                break;
            case SOL1600x900:
                RES_SPECS.width = 1600;
                RES_SPECS.height = 900;
                break;
            case SOL1768x992:
                RES_SPECS.width = 1768;
                RES_SPECS.height = 992;
                break;
            case SOL1920x1080:
                RES_SPECS.width = 1920;
                RES_SPECS.height = 1080;
                break;
            case SOL1600x1024:
                RES_SPECS.width = 1600;
                RES_SPECS.height = 1024;
                break;
            default:
                break;
        }
        RES_SPECS.gameWidth = (int) ((float) RES_SPECS.GAME_HEIGHT / RES_SPECS.height * RES_SPECS.width);

        RENDERER.changeResolution();
        CAMERA.changeResolution();

        for (HudElement hudElement : hudElements){
            hudElement.changeResolution();
        }
        INVENTORY.changeResolution();
    }

    protected void run(){
        long startFpsTime = System.nanoTime();
        long lastFrameTime = System.nanoTime();
        float accumulatedTime = 0.0f;
        int numFrames = 0;

        initGame();

        while (!glfwWindowShouldClose(RENDERER.window)){
            long frameStartTime = System.nanoTime();
            float frameTime = Math.min((frameStartTime - lastFrameTime) / (float) SECOND, 0.25f);
            time += frameStartTime - lastFrameTime;
            lastFrameTime = frameStartTime;
            accumulatedTime += frameTime;


            while (accumulatedTime >= DELTATIME){
                gameStep();
                accumulatedTime -= DELTATIME;
            }

            RENDERER.renderGame();
            numFrames++;

            if (System.nanoTime() - startFpsTime >= SECOND){
                RENDERER.setWindowTitle(title + " | FPS: " + numFrames);
                startFpsTime = System.nanoTime();
                numFrames = 0;
            }

            reduceToFPSLimit(frameStartTime);
        }

        RENDERER.exit();
        SOUND.exit();
    }

    private static void reduceToFPSLimit(long frameStartTime){
        long sleepDuration = NANO_SECOND_FPS - (System.nanoTime() - frameStartTime);
        if (sleepDuration > 0){
            try{
                Thread.sleep(sleepDuration / MILLI_SECOND);
            } catch (InterruptedException ignored){
            }
        }
    }

    private void gameStep(){
        if (!pause){
            PHYSICS.preStep();
        }
        userInteraction();
        if (!pause){
            coreMechanicsLoop();
            mechanicsLoop();
            eventsLoop();
            PHYSICS.step();
            SOUND.soundListener.refreshPos();
            postMechanicsLoops();

        }
    }

    protected void coreMechanicsLoop(){
        // Mob Mechanics
        for (int i = 0; i < PHYSICS.numBodies; i++){
            Body body = PHYSICS.bodies[i];
            if (body instanceof Mob mob){
                mob.invulnerabilityCooldown();
            }
        }
    }

    protected void eventsLoop(){
        for (int i = 0; i < numEvents; i++){
            GameEvent event = events[i];
            if (event.getFrameCounter() == 0){
                event.start();
            } else if (event.getFrameCounter() < event.EVENT_DURATION){
                event.procedure();
            } else{
                event.end();
                remEvent(i);
                i--;
            }
            event.countFrame();
        }
    }

    protected void userInteraction(){
        glfwPollEvents();
        MOUSE.update();
        KEYBOARD.update();
    }

    public void addEvent(GameEvent e){
        events[numEvents++] = e;
    }

    private void remEvent(int id){
        for (int i = id + 1; i < numEvents; i++){
            events[i - 1] = events[i];
        }
        numEvents--;
    }

    public void setSanity(float sanity){
        this.sanity = sanity;
        RENDERER.updateSanity(sanity);
    }

    public float getSanity(){
        return sanity;
    }

    public void addGuiElement(HudElement hudElement){
        hudElements[numElements++] = hudElement;}

}
