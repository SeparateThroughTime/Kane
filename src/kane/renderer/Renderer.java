package kane.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TransformAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;

import javax.swing.JPanel;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;

import kane.genericGame.Game;
import kane.genericGame.gameEvent.camera.MoveBackground;
import kane.math.Scalar;
import kane.math.Vec2f;
import kane.physics.AABB;
import kane.physics.Body;
import kane.physics.Material;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.ShapePair;
import kane.physics.ShapeType;
import kane.physics.contacts.Contact;
import kane.physics.shapes.Box;
import kane.physics.shapes.Circle;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Plane;
import kane.physics.shapes.Point;
import kane.physics.shapes.Polygon;
import kane.renderer.shaders.DefaultShader;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL46.*;

/**
 * The Renderer renders the game.
 */
public class Renderer {

	protected ResolutionSpecification resSpecs;
	protected float multiplicator;
	protected final Physics physics;
	protected final Game g;
	protected Shape[] renderedShapes;
	protected int numRenderedShapes;
	protected Camera camera;
	protected Background background;

	public boolean showContacts = false;
	public boolean showAABBs = false;

	protected long window;

	protected static final int POSITION_SIZE = 3;
	protected static final int COLOR_SIZE = 4;
	protected static final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE;
	protected static final int VERTEX_SIZE_BYTE = VERTEX_SIZE * Float.BYTES;
	protected static final int ELEMENT_SIZE = 3;

	protected float[] vertices;
	protected int countCurrentVertices;
	protected int[] elements;
	protected int countCurrentElements;

	protected int vertexArrayObjectID;
	protected int vertexBufferObjectID;
	protected int elementBufferObjectID;

	protected Shader shader;

	public Renderer(ResolutionSpecification resSpecs, Physics physics, Game g, String title) {
		this.resSpecs = resSpecs;
		this.physics = physics;
		this.g = g;
		this.multiplicator = 1f;

		initGLFW(title);

		shader = Shader.DEFAULT;
		shader.compile();

	}

	protected void initGLFW(String title) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);

		window = glfwCreateWindow(resSpecs.width, resSpecs.height, title, 0, 0);
		if (window == 0) {
			throw new RuntimeException("Failed to create GLFW window");
		}

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		GL.createCapabilities();
	}

	protected Vec2f transformPosToVertex(Vec2f gamePos) {
		Vec2f cameraAlteredPos = new Vec2f(gamePos).sub(new Vec2f(camera.zeroPoint).mult(multiplicator));

		float x = cameraAlteredPos.getX();
		x -= resSpecs.halfWidth;
		x /= resSpecs.halfWidth;

		float y = cameraAlteredPos.getY();
		y -= resSpecs.halfHeight;
		y /= resSpecs.halfHeight;

		return new Vec2f(x, y);
	}

	public void createCamera() {
		this.camera = new Camera(resSpecs, g);
		this.physics.addBody(camera);
	}

	// TODO drawBackground
	private void drawBackground() {
		if (background != null) {
			int width = (int) (background.getImg().getWidth() * multiplicator);
			int height = (int) (background.getImg().getHeight() * multiplicator);
			int x = background.getOffsetX();
			while (x < resSpecs.gameWidth) {
//				g2d.drawImage(background.getImg(), x, 0, width, height, null);
				x += width;
			}
		}
	}

	/**
	 * Needs to run after Resolution is changed.
	 */
	public void changeResolution() {
		glfwSetWindowSize(window, resSpecs.width, resSpecs.height);
		camera.changeResolution();
		multiplicator = (float) resSpecs.height / resSpecs.GAME_HEIGHT;
	}

	protected void clearWindow() {
		glClearColor(0f, 0f, 0f, 1f);
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public void renderGame() {
		clearWindow();
		camera.update();
//		drawBackground();
		chooseRenderedShapes();
		initVerticesAndElements();
		drawBodies();
//		drawAABBs();
//		drawContacts();
		displayFrame();
	}

	protected void initVerticesAndElements() {
		int numVertices = 0;
		int numElements = 0;
		for (int i = 0; i < numRenderedShapes; i++) {
			Shape s = renderedShapes[i];
			numVertices += s.getNumRenderVertices();
			numElements += s.getNumRenderElements();
		}
		vertices = new float[numVertices * (POSITION_SIZE + COLOR_SIZE)];
		elements = new int[numElements * 3];
	}

	protected void displayFrame() {
		vertexArrayObjectID = glGenVertexArrays();
		glBindVertexArray(vertexArrayObjectID);

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
		vertexBuffer.put(vertices).flip();

		vertexBufferObjectID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elements.length);
		elementBuffer.put(elements).flip();

		elementBufferObjectID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTE, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTE, POSITION_SIZE * Float.BYTES);
		glEnableVertexAttribArray(1);
		shader.use();

		glDrawElements(GL_TRIANGLES, elements.length, GL_UNSIGNED_INT, 0);

		// Unbind
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		shader.detach();

		glfwSwapBuffers(window);
	}

	/**
	 * Determines which Shapes will be rendered.
	 */
	private void chooseRenderedShapes() {
		numRenderedShapes = 0;
		renderedShapes = new Shape[physics.getNumBodies() * Body.MAX_SHAPES];
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			if (!body.isRemoved()) {
				for (int j = 0; j < body.getNumShapes(); j++) {
					Shape shape = body.getShape(j);
					if (shape.getAABB().overlaps(camera.getWindow())) {
						if (shape.isVisible()) {
							renderedShapes[numRenderedShapes++] = shape;
						}
					}
				}
			}
		}
	}

	/**
	 * draw all bodies, that are determined to be rendered.
	 * 
	 * @param cox: Camera Offset X
	 * @param coy: Camera Offset Y
	 */
	private void drawBodies() {
		countCurrentVertices = 0;
		countCurrentElements = 0;
		// draw bodies
		for (int layer = 1; layer < Shape.MAX_RENDER_LAYER; layer++) {
			for (int i = 0; i < numRenderedShapes; i++) {
				Shape shape = renderedShapes[i];
				if (shape.RENDER_LAYER == layer) {
					// TODO drawSprite
//					if (shape.hasSprite()) {
//						SpriteController[] spriteControllers = shape.getSpriteControllers();
//						for (SpriteController spriteController : spriteControllers) {
//							float scale = spriteController.getScale();
//							if (!g.pause) {
//								spriteController.step();
//							}
//							BufferedImage frame = spriteController.getFrame();
//							Vec2f pos = shape.getAbsPos();
//							Vec2f spriteAbsPos = new Vec2f(pos).add(spriteController.getSpritePosOffset());
////							int posX = (int) spriteAbsPos.getX();
////							int posY = (int) spriteAbsPos.getY();
//							int posX = Scalar.round(spriteAbsPos.getX());
//							int posY = Scalar.round(spriteAbsPos.getY());
//							drawImage(frame, scale, posX, posY, g2d);
//						}

//					if (ShapeType.PLANE.equals(shape.getType())) {
//						Plane plane = (Plane) shape;
//						Vec2f startPoint = transformPosToVertex(plane.getPoint());
//						Vec2f perp = new Vec2f(plane.getNormal()).perpRight();
//						// len won't be accurate because it's calculated as if the plane is
//						// horizontally. Not gonna change it because it's irrelevant for later game.
//						float len = (plane.getLen() - resSpecs.halfWidth) / resSpecs.halfWidth;
//						Vec2f endPoint = transformPosToVertex(new Vec2f(plane.getPoint()).addMult(perp, len));
//						drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(),
//								plane.getColor());
//
//						// draws normal of planes
//						Vec2f center = transformPosToVertex(new Vec2f(startPoint).addMult(perp, len * 0.5f));
//						drawNormal(center, plane.getNormal());
//					}

//					else if (ShapeType.LINESEGMENT.equals(shape.getType())) {
//						LineSegment lineSegment = (LineSegment) shape;
//						Vec2f startPoint = transformPosToVertex(
//								new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosA()));
//						Vec2f endPoint = transformPosToVertex(
//								new Vec2f(lineSegment.getAbsPos()).add(lineSegment.getRelPosB()));
//						drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(),
//								lineSegment.getColor());
//					}

					// TODO drawCircle
//					else if (ShapeType.CIRCLE.equals(shape.getType())) {
//						Circle circle = (Circle) shape;
//						drawCircle((int) circle.getAbsPos().getX(), (int) circle.getAbsPos().getY(),
//								(int) circle.getRad(), circle.getColor(), g2d);
//					}

					if (ShapeType.BOX.equals(shape.getType())) {
						Box box = (Box) shape;
						Vec2f absPos = box.getAbsPos();
						Vec2f rad = box.getRad();
						Vec2f point1 = transformPosToVertex(new Vec2f(absPos));
						Vec2f point2 = transformPosToVertex(new Vec2f(absPos.getX() + rad.getX(), absPos.getY()));
						Vec2f point3 = transformPosToVertex(new Vec2f(absPos).add(rad));
						Vec2f point4 = transformPosToVertex(new Vec2f(absPos.getX(), absPos.getY() + rad.getY()));
						drawRect(point1, point2, point3, point4, box.getColor());
					}

					else if (ShapeType.POLYGON.equals(shape.getType())) {
						Polygon pol = (Polygon) shape;
						int numPoints = pol.getNumPoints();
						Vec2f[] points = new Vec2f[numPoints];
						Vec2f absPos = pol.getAbsPos();
						for (int p = 0; p < numPoints; p++) {
							Vec2f pointAbsPos = new Vec2f(pol.getPoint(p)).add(absPos);
							points[p] = transformPosToVertex(pointAbsPos);
						}
						Vec2f center = transformPosToVertex(absPos);
						drawPolygon(points, center, pol.getColor());
					}

//					else if (ShapeType.POINT.equals(shape.getType())) {
//						Point point = (Point) shape;
//						Vec2f absPos = transformPosToVertex(point.getAbsPos());
//						drawLine(absPos.getX(), absPos.getY(), absPos.getX(), absPos.getY(), point.getColor());
//					}
				}
			}
		}
	}

	/**
	 * Display AABBs
	 */
	// TODO display AABBs
	private void drawAABBs() {
		if (showAABBs) {
			for (int i = 0; i < physics.getNumBodies(); i++) {
				Body body = physics.getBodies(i);
				for (int j = 0; j < body.getNumShapes(); j++) {
					Shape shape = body.getShape(j);
					AABB aabb = shape.getAABB();
					Vec2f min = transformPosToVertex(aabb.getMin());
					Vec2f max = transformPosToVertex(aabb.getMax());
					Vec2f diameter = transformPosToVertex(new Vec2f(aabb.getMax()).sub(aabb.getMin()));
//					drawRect(aabb.getMin().getX(), aabb.getMax().getY(), diameter.getX(), diameter.getY(), Color.GREEN);
				}
			}
		}
	}

	/**
	 * Display contacts.
	 */
	// TODO displayContacts
	private void drawContacts() {
		if (showContacts) {
			for (int i = 0; i < physics.getNumShapePairs(); i++) {
				ShapePair shapePair = physics.getShapePairs(i);
				Contact contact = shapePair.getContact();
				if (contact != null && shapePair.getShapeA().isVisible() == true
						&& shapePair.getShapeB().isVisible() == true) {
					Vec2f normal = contact.getNormal();
					Vec2f closestPointOnPlane = contact.getPoint();
					Vec2f closestPointOnBox = new Vec2f(closestPointOnPlane).addMult(normal, contact.getDistance());
//					fillCircle((int) closestPointOnPlane.getX(), (int) closestPointOnPlane.getY(), 4, Color.PINK, g2d);
//					fillCircle((int) closestPointOnBox.getX(), (int) closestPointOnBox.getY(), 4, Color.YELLOW, g2d);
				}
			}
		}
	}

	// TODO drawImage
	private void drawImage(BufferedImage img, float scale, int posX, int posY) {
		posX -= Scalar.round(camera.zeroPoint.getX());
		posY -= Scalar.round(camera.zeroPoint.getY());
		int width = (int) (img.getWidth() * multiplicator * Sprite.SCALE * scale);
		int height = (int) (img.getHeight() * multiplicator * Sprite.SCALE * scale);
		posY += img.getHeight() * Sprite.SCALE * scale;
		posX = (int) (posX * multiplicator);
		posY = (int) (posY * multiplicator);
		posY = Scalar.getY(posY, resSpecs.height);
	}

	private void drawLine(float x1, float y1, float x2, float y2, Color color) {

	}

	private void drawCircle(int x, int y, int rad, Color color) {
		x -= Scalar.round(camera.zeroPoint.getX());
		y -= Scalar.round(camera.zeroPoint.getY());
		x -= rad;
		y += rad;
		x = (int) (x * multiplicator);
		y = (int) (y * multiplicator);
		rad = (int) (rad * multiplicator);
		y = Scalar.getY(y, resSpecs.height);
	}

	private void fillCircle(int x, int y, int rad, Color color) {
		x -= Scalar.round(camera.zeroPoint.getX());
		y -= Scalar.round(camera.zeroPoint.getY());
		x -= rad;
		y += rad;
		x = (int) (x * multiplicator);
		y = (int) (y * multiplicator);
		rad = (int) (rad * multiplicator);
		y = Scalar.getY(y, resSpecs.height);
	}

	private void drawRect(Vec2f point1, Vec2f point2, Vec2f point3, Vec2f point4, Color color) {
		int verticeStartingIndex = countCurrentVertices * VERTEX_SIZE;
		int elementsStartingIndex = countCurrentElements * ELEMENT_SIZE;

		int rgb = color.getRGB();
		int red = (rgb & 0x000000FF);
		int green = (rgb & 0x0000FF00) >> 8;
		int blue = (rgb & 0x00FF0000) >> 16;

		// Positions
		vertices[verticeStartingIndex + 0] = point1.getX();
		vertices[verticeStartingIndex + 1] = point1.getY();
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 7] = point2.getX();
		vertices[verticeStartingIndex + 8] = point2.getY();
		vertices[verticeStartingIndex + 9] = 0f;

		vertices[verticeStartingIndex + 14] = point3.getX();
		vertices[verticeStartingIndex + 15] = point3.getY();
		vertices[verticeStartingIndex + 16] = 0f;

		vertices[verticeStartingIndex + 21] = point4.getX();
		vertices[verticeStartingIndex + 22] = point4.getY();
		vertices[verticeStartingIndex + 23] = 0f;

		// Colors
		vertices[verticeStartingIndex + 3] = red;
		vertices[verticeStartingIndex + 4] = green;
		vertices[verticeStartingIndex + 5] = blue;
		vertices[verticeStartingIndex + 6] = 1f;

		vertices[verticeStartingIndex + 10] = red;
		vertices[verticeStartingIndex + 11] = green;
		vertices[verticeStartingIndex + 12] = blue;
		vertices[verticeStartingIndex + 13] = 1f;

		vertices[verticeStartingIndex + 17] = red;
		vertices[verticeStartingIndex + 18] = green;
		vertices[verticeStartingIndex + 19] = blue;
		vertices[verticeStartingIndex + 20] = 1f;

		vertices[verticeStartingIndex + 24] = red;
		vertices[verticeStartingIndex + 25] = green;
		vertices[verticeStartingIndex + 26] = blue;
		vertices[verticeStartingIndex + 27] = 1f;

		// Elements
		elements[elementsStartingIndex + 0] = countCurrentVertices + 0;
		elements[elementsStartingIndex + 1] = countCurrentVertices + 1;
		elements[elementsStartingIndex + 2] = countCurrentVertices + 2;

		elements[elementsStartingIndex + 3] = countCurrentVertices + 0;
		elements[elementsStartingIndex + 4] = countCurrentVertices + 2;
		elements[elementsStartingIndex + 5] = countCurrentVertices + 3;

		
		countCurrentVertices += 4;
		countCurrentElements += 2;
	}

	private void drawPolygon(Vec2f[] points, Vec2f center, Color color) {
		int verticeStartingIndex = countCurrentVertices * VERTEX_SIZE;
		int elementsStartingIndex = countCurrentElements * ELEMENT_SIZE;

		int rgb = color.getRGB();
		int red = (rgb & 0x000000FF);
		int green = (rgb & 0x0000FF00) >> 8;
		int blue = (rgb & 0x00FF0000) >> 16;

		// Center
		vertices[verticeStartingIndex + 0] = center.getX();
		vertices[verticeStartingIndex + 1] = center.getY();
		vertices[verticeStartingIndex + 2] = 0f;

		vertices[verticeStartingIndex + 3] = red;
		vertices[verticeStartingIndex + 4] = green;
		vertices[verticeStartingIndex + 5] = blue;
		vertices[verticeStartingIndex + 6] = 1f;

		// Point0
		vertices[verticeStartingIndex + 7] = points[0].getX();
		vertices[verticeStartingIndex + 8] = points[0].getY();
		vertices[verticeStartingIndex + 9] = 0f;

		vertices[verticeStartingIndex + 10] = red;
		vertices[verticeStartingIndex + 11] = green;
		vertices[verticeStartingIndex + 12] = blue;
		vertices[verticeStartingIndex + 13] = 1f;

		for (int i = 1; i < points.length; i++) {
			Vec2f point = points[i];

			vertices[verticeStartingIndex + 14 + (i - 1) * VERTEX_SIZE] = point.getX();
			vertices[verticeStartingIndex + 15 + (i - 1) * VERTEX_SIZE] = point.getY();
			vertices[verticeStartingIndex + 16 + (i - 1) * VERTEX_SIZE] = 0f;

			vertices[verticeStartingIndex + 17 + (i - 1) * VERTEX_SIZE] = red;
			vertices[verticeStartingIndex + 18 + (i - 1) * VERTEX_SIZE] = green;
			vertices[verticeStartingIndex + 19 + (i - 1) * VERTEX_SIZE] = blue;
			vertices[verticeStartingIndex + 20 + (i - 1) * VERTEX_SIZE] = 1f;

			elements[elementsStartingIndex + 0 + (i - 1) * ELEMENT_SIZE] = countCurrentVertices;
			elements[elementsStartingIndex + 1 + (i - 1) * ELEMENT_SIZE] = countCurrentVertices + i;
			elements[elementsStartingIndex + 2 + (i - 1) * ELEMENT_SIZE] = countCurrentVertices + i + 1;
		}

		// last element
		elements[elementsStartingIndex + 0 + (points.length - 1) * ELEMENT_SIZE] = countCurrentVertices;
		elements[elementsStartingIndex + 1 + (points.length - 1) * ELEMENT_SIZE] = countCurrentVertices + points.length;
		elements[elementsStartingIndex + 2 + (points.length - 1) * ELEMENT_SIZE] = countCurrentVertices + 1;

		countCurrentVertices += points.length + 1;
		countCurrentElements += points.length;
	}

	/**
	 * draw a normal.
	 * 
	 * @param pos
	 * @param normal
	 */
	// TODO drawNormal
	private void drawNormal(Vec2f pos, Vec2f normal) {
		pos.mult(multiplicator);

		int arrowRadLen = 15;
		int arrowRadWid = 15;
		int nLen = 40;
		Vec2f perp = new Vec2f(normal).perpRight();

		Vec2f arrowTip = new Vec2f(pos).addMult(normal, nLen);
		Vec2f leftArmPos = new Vec2f(arrowTip).addMult(perp, -arrowRadLen).addMult(normal, -arrowRadWid);
		Vec2f rightArmPos = new Vec2f(arrowTip).addMult(perp, arrowRadLen).addMult(normal, -arrowRadWid);

//		drawLine((int) pos.getX(), (int) pos.getY(), (int) arrowTip.getX(), (int) arrowTip.getY(), Color.WHITE, g2d);
//		drawLine((int) arrowTip.getX(), (int) arrowTip.getY(), (int) leftArmPos.getX(), (int) leftArmPos.getY(),
//				Color.WHITE, g2d);
//		drawLine((int) arrowTip.getX(), (int) arrowTip.getY(), (int) rightArmPos.getX(), (int) rightArmPos.getY(),
//				Color.WHITE, g2d);
	}

	/**
	 * Get camera.
	 * 
	 * @return
	 */
	public Camera getCamera() {
		return camera;
	}

	public void changeBackground(File file) {
		background = new Background(file, resSpecs.GAME_HEIGHT);
	}

	public Background getGameBackground() {
		return background;
	}

	public void moveBackground() {
		g.addEvent(new MoveBackground(g, this));
	}

	public long getWindow() {
		return window;
	}
}
