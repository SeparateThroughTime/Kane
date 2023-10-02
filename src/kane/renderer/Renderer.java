package kane.renderer;

import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_DOUBLEBUFFER;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_API;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL46.*;
import static kane.physics.Physics.PHYSICS;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.Kane.GAME;
import static kane.genericGame.ResourceManager.RESOURCE_MANAGER;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import kane.genericGame.Game;
import kane.genericGame.ResourceManager;
import kane.genericGame.gameEvent.camera.MoveBackground;
import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;

/**
 * The Renderer renders the game.
 */
public class Renderer {

	static final int MAX_BATCH_SIZE = 1000;
	static final int MAX_GLTEXTURES = 16;
	private ArrayList<SpriteBatch> batches;
	private ArrayList<LineBatch> lineBatches;
	private SpriteBatch backgroundBatch;

	public static Renderer RENDERER;

	public float multiplicator;
	protected Shape[] renderedTextures;
	protected int numRenderedTextures;
	public Background background;

	public boolean showContacts = false;
	public boolean showAABBs = false;

	public long window;

	public Shader shader;

	private Renderer(String title) {
		this.multiplicator = 1f;

		initGLFW(title);
		batches = new ArrayList<>();
		lineBatches = new ArrayList<>();

		shader = RESOURCE_MANAGER.getShader("shaders/default.glsl");
		shader.compile();

	}

	public static void initializeRenderer(String title) {
		if (RENDERER == null) {
			RENDERER = new Renderer(title);
		}
	}

	public void initGLFW(String title) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);

		window = glfwCreateWindow(RES_SPECS.width, RES_SPECS.height, title, 0, 0);
		if (window == 0) {
			throw new RuntimeException("Failed to create GLFW window");
		}

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
	}

	public void addShape(Shape shape) {
		if (!shape.hasSprite) {
			return;
		}

		boolean added = false;
		for (SpriteBatch batch : batches) {
			if (batch.hasRoom() && batch.RENDER_LAYER == shape.renderLayer) {
				batch.add(shape);
				added = true;
				break;
			}
		}

		if (!added) {
			SpriteBatch newBatch = new SpriteBatch(MAX_BATCH_SIZE, shape.renderLayer);
			newBatch.start();
			batches.add(newBatch);
			newBatch.add(shape);
			Collections.sort(batches);
		}
	}

	public void addLine(Shape shape) {
		boolean added = false;
		for (LineBatch batch : lineBatches) {
			if (batch.hasRoom()) {
				batch.add(shape);
				added = true;
				break;
			}
		}

		if (!added) {
			LineBatch newBatch = new LineBatch(MAX_BATCH_SIZE, shape.renderLayer);
			newBatch.start();
			lineBatches.add(newBatch);
			newBatch.add(shape);
		}
	}

	/**
	 * Needs to run after Resolution is changed.
	 */
	public void changeResolution() {
		glfwSetWindowSize(window, RES_SPECS.width, RES_SPECS.height);
		CAMERA.changeResolution();
		multiplicator = (float) RES_SPECS.height / RES_SPECS.GAME_HEIGHT;
	}

	protected void clearWindow() {
		glClearColor(0f, 0f, 0f, 1f);
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public void renderGame() {
		clearWindow();
		CAMERA.update();
//		drawBackground();

		for (SpriteBatch batch : batches) {
			batch.render();
		}

		for (LineBatch batch : lineBatches) {
			batch.render();
		}

		glfwSwapBuffers(window);
	}

	public void changeBackground(String filepath) {
		background = new Background(filepath);
		int width = background.spriteController.sprite.FRAME_WIDTH;
		
		backgroundBatch = new SpriteBatch(numRenderedTextures, 0);
	}

	public void moveBackground() {
		GAME.addEvent(new MoveBackground());
	}
}
