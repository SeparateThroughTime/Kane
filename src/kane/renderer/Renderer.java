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
import static kane.renderer.drawer.TriangleDrawer.TRIANGLE_DRAWER;
import static kane.physics.Physics.PHYSICS;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.Kane.GAME;
import static kane.renderer.drawer.LineDrawer.LINE_DRAWER;
import static kane.renderer.drawer.ImageDrawer.IMAGE_DRAWER;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import kane.genericGame.Game;
import kane.genericGame.gameEvent.camera.MoveBackground;
import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.contacts.Contact;
import kane.renderer.drawer.ImageDrawer;
import kane.renderer.drawer.LineDrawer;
import kane.renderer.drawer.TriangleDrawer;

/**
 * The Renderer renders the game.
 */
public class Renderer {
	
	private final int MAX_BATCH_SIZE = 1000;
	private ArrayList<RenderBatch> batches;
	
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

		shader = Shader.DEFAULT;
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
	}
	
	public void addShape(Shape shape) {
		if(shape.hasSprite) {
			boolean added = false;
			for (RenderBatch batch : batches) {
				if (batch.hasRoom) {
					batch.addShape(shape);
					added = true;
					break;
				}
			}
			
			if(!added) {
				RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
				newBatch.start();
				batches.add(newBatch);
				newBatch.addShape(shape);
			}
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
		
		for (RenderBatch batch : batches) {
			batch.render();
		}

//		drawAABBs();
//		drawContacts();
		
		
		glfwSwapBuffers(window);
	}

	/**
	 * Display AABBs
	 */
	// TODO display AABBs
//	private void drawAABBs() {
//		if (showAABBs) {
//			for (int i = 0; i < physics.getNumBodies(); i++) {
//				Body body = physics.getBodies(i);
//				for (int j = 0; j < body.getNumShapes(); j++) {
//					Shape shape = body.getShape(j);
//					AABB aabb = shape.getAABB();
//					Vec2f min = transformPosToVertex(aabb.getMin());
//					Vec2f max = transformPosToVertex(aabb.getMax());
//					Vec2f diameter = transformPosToVertex(new Vec2f(aabb.getMax()).sub(aabb.getMin()));
////					drawRect(aabb.getMin().getX(), aabb.getMax().getY(), diameter.getX(), diameter.getY(), Color.GREEN);
//				}
//			}
//		}
//	}

	/**
	 * Display contacts.
	 */
	// TODO displayContacts
	private void drawContacts() {
		if (showContacts) {
			for (int i = 0; i < PHYSICS.numShapePairs; i++) {
				ShapePair shapePair = PHYSICS.shapePairs[i];
				Contact contact = shapePair.contact;
				if (contact != null && shapePair.shapeA.visible == true
						&& shapePair.shapeB.visible == true) {
					Vec2f normal = contact.normal;
					Vec2f closestPointOnPlane = contact.point;
					Vec2f closestPointOnBox = new Vec2f(closestPointOnPlane).addMult(normal, contact.distance);
//					fillCircle((int) closestPointOnPlane.getX(), (int) closestPointOnPlane.getY(), 4, Color.PINK, g2d);
//					fillCircle((int) closestPointOnBox.getX(), (int) closestPointOnBox.getY(), 4, Color.YELLOW, g2d);
				}
			}
		}
	}

	// TODO drawImage
	private void drawImage(BufferedImage img, float scale, int posX, int posY) {
		posX -= Scalar.round(CAMERA.zeroPoint.x);
		posY -= Scalar.round(CAMERA.zeroPoint.y);
		int width = (int) (img.getWidth() * multiplicator * Sprite.SCALE * scale);
		int height = (int) (img.getHeight() * multiplicator * Sprite.SCALE * scale);
		posY += img.getHeight() * Sprite.SCALE * scale;
		posX = (int) (posX * multiplicator);
		posY = (int) (posY * multiplicator);
		posY = Scalar.getY(posY, RES_SPECS.height);
	}

	private void drawCircle(int x, int y, int rad, Color color) {
		x -= Scalar.round(CAMERA.zeroPoint.x);
		y -= Scalar.round(CAMERA.zeroPoint.y);
		x -= rad;
		y += rad;
		x = (int) (x * multiplicator);
		y = (int) (y * multiplicator);
		rad = (int) (rad * multiplicator);
		y = Scalar.getY(y, RES_SPECS.height);
	}

	private void fillCircle(int x, int y, int rad, Color color) {
		x -= Scalar.round(CAMERA.zeroPoint.x);
		y -= Scalar.round(CAMERA.zeroPoint.y);
		x -= rad;
		y += rad;
		x = (int) (x * multiplicator);
		y = (int) (y * multiplicator);
		rad = (int) (rad * multiplicator);
		y = Scalar.getY(y, RES_SPECS.height);
	}

	public void changeBackground(File file) {
		background = new Background(file, RES_SPECS.GAME_HEIGHT);
	}

	public void moveBackground() {
		GAME.addEvent(new MoveBackground());
	}
	
	public void uploadVarToShader(String varName, int val) {
		int varLocation = glGetUniformLocation(shader.shaderProgramID, varName);
		shader.use();
		glUniform1i(varLocation, val);
	}
}
