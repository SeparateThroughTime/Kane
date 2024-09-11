package kane.renderer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL46.*;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.Kane.GAME;
import static kane.genericGame.ResourceManager.RESOURCE_MANAGER;

import java.util.ArrayList;
import java.util.Collections;

import kane.exceptions.CompileShaderException;
import kane.exceptions.InitFramebufferException;
import kane.exceptions.LinkShaderException;
import kane.exceptions.LoadShaderException;
import kane.genericGame.Game;
import kane.math.Vec2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import kane.genericGame.gameEvent.camera.MoveBackground;
import kane.physics.Shape;

public class Renderer{

    static final int MAX_BATCH_SIZE = 1000;
    static final int MAX_TEXTURES = 16;
    private final ArrayList<SpriteBatch> SpriteBatches;
    private final ArrayList<LineBatch> lineBatches;
    private BackgroundBatch backgroundBatch;
    private PostProcessBatch postProcessBatch;

    public static Renderer RENDERER;

    public float multiplier;
    public Background background;

    public boolean showContacts = false;
    public boolean showAABBs = false;

    public long window;

    public Shader shader;
    public Shader postShader;

    private Renderer(String title){
        this.multiplier = 1f;

        initGLFW(title);
        SpriteBatches = new ArrayList<>();
        lineBatches = new ArrayList<>();

        changeShader("shaders/default.vertex.glsl", "shaders/default.fragment.glsl");
        changePostShader("shaders/post.vertex.glsl", "shaders/post.fragment.glsl");
    }

    public void initPostProcessBatch(){
        try{
            postProcessBatch = new PostProcessBatch();
        } catch (InitFramebufferException e){
            e.printStackTrace();
            glfwDestroyWindow(RENDERER.window);
        }
    }

    public static void initializeRenderer(String title){
        if (RENDERER == null){
            RENDERER = new Renderer(title);
        }
    }

    public void initGLFW(String title){
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);

        window = glfwCreateWindow(RES_SPECS.width, RES_SPECS.height, title, 0, 0);
        if (window == 0){
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void addShape(Shape shape){
        if (!shape.hasSprite){
            return;
        }

        boolean added = false;
        for (SpriteBatch batch : SpriteBatches){
            if (batch.hasRoom() && batch.RENDER_LAYER == shape.renderLayer){
                batch.addShape(shape);
                added = true;
                break;
            }
        }

        if (!added){
            SpriteBatch newBatch = new SpriteBatch(MAX_BATCH_SIZE, shape.renderLayer);
            newBatch.start();
            SpriteBatches.add(newBatch);
            newBatch.addShape(shape);
            Collections.sort(SpriteBatches);
        }
    }

    public void addLine(Shape shape){
        boolean added = false;
        for (LineBatch batch : lineBatches){
            if (batch.hasRoom()){
                batch.addShape(shape);
                added = true;
                break;
            }
        }

        if (!added){
            LineBatch newBatch = new LineBatch(MAX_BATCH_SIZE);
            newBatch.start();
            lineBatches.add(newBatch);
            newBatch.addShape(shape);
        }
    }

    public void changeResolution(){
        glfwSetWindowSize(window, RES_SPECS.width, RES_SPECS.height);
        CAMERA.changeResolution();
        multiplier = (float) RES_SPECS.height / RES_SPECS.GAME_HEIGHT;
        postShader.uploadVec2i("resolution", new Vec2i(RES_SPECS.width, RES_SPECS.height));
    }

    public void changeShader(String vertexFilepath, String fragmentFilepath){
        try{
            shader = RESOURCE_MANAGER.getShader(vertexFilepath, fragmentFilepath);
            shader.compile();
            shader.uploadVec2i("resolution", new Vec2i(RES_SPECS.width, RES_SPECS.height));
        } catch (LoadShaderException | CompileShaderException | LinkShaderException e){
            e.printStackTrace();
        }
    }

    public void changePostShader(String vertexFilepath, String fragmentFilepath){
        try{
            postShader = RESOURCE_MANAGER.getShader(vertexFilepath, fragmentFilepath);
            postShader.compile();
            postShader.uploadVec2i("resolution", new Vec2i(RES_SPECS.width, RES_SPECS.height));
        } catch (LoadShaderException | CompileShaderException | LinkShaderException e){
            e.printStackTrace();
        }
    }

    protected void clearWindow(){

        glClearColor(1f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void renderGame(){
        postProcessBatch.bindFramebuffer();
        clearWindow();
        CAMERA.update();
        updateTime();

        backgroundBatch.render();

        for (SpriteBatch batch : SpriteBatches){
            batch.render();
        }

        for (LineBatch batch : lineBatches){
            batch.render();
        }

        postProcessBatch.detachFramebuffer();
        postProcessBatch.render();

        glfwSwapBuffers(window);
    }

    public void updateTime(){
        postShader.uploadFloat("time", (float) Game.time * 0.000000001f);
    }

    public void updateSanity(float sanity){
        postShader.uploadFloat("sanity", sanity);
    }

    public void changeBackground(String filepath){
        background = new Background(filepath);

        backgroundBatch = new BackgroundBatch(background);
        backgroundBatch.start();
    }

    public void moveBackground(){
        GAME.addEvent(new MoveBackground());
    }

    public void setWindowTitle(String title){
        glfwSetWindowTitle(window, title);
    }

    public void exit(){
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
