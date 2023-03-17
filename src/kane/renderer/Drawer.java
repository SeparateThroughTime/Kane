package kane.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Physics;
import kane.physics.Shape;
import kane.physics.ShapeType;
import kane.physics.shapes.Box;
import kane.physics.shapes.Polygon;

public abstract class Drawer {
	protected Shape[] renderedShapes;
	protected int numRenderedShapes;
	
	public final int POSITION_SIZE = 3;
	public final int COLOR_SIZE = 4;
	public final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE;
	public final int VERTEX_SIZE_BYTE = VERTEX_SIZE * Float.BYTES;
	public final int ELEMENT_SIZE;
	
	protected float[] vertices;
	protected int countCurrentVertices;
	protected int[] elements;
	protected int countCurrentElements;
	
	protected int vertexArrayObjectID;
	protected int vertexBufferObjectID;
	protected int elementBufferObjectID;
	
	public abstract void drawBodies();
	
	public Drawer(int elementSize) {
		ELEMENT_SIZE = elementSize;
	}
	
	protected void initVerticesAndElements() {
		int numVertices = 0;
		int numElements = 0;
		for (int i = 0; i < numRenderedShapes; i++) {
			Shape s = renderedShapes[i];
			numVertices += s.getNumRenderVertices();
			numElements += s.getNumRenderElements();
		}
		vertices = new float[numVertices * VERTEX_SIZE];
		elements = new int[numElements * ELEMENT_SIZE];
	}
	
	public void displayFrame(Shader shader) {
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
	}
	
	/**
	 * Determines which Shapes will be rendered.
	 */
	protected void chooseRenderedShapes() {
		numRenderedShapes = 0;
		renderedShapes = new Shape[physics.getNumBodies() * Body.MAX_SHAPES];
		for (int i = 0; i < physics.getNumBodies(); i++) {
			Body body = physics.getBodies(i);
			if (!body.isRemoved()) {
				for (int j = 0; j < body.getNumShapes(); j++) {
					Shape shape = body.getShape(j);
					if (shape.getAABB().overlaps(renderer.camera.getWindow())) {
						if (shape.isVisible()) {
							if (ShapeType.BOX.equals(shape.getType()) || ShapeType.POLYGON.equals(shape.getType())) {
								renderedShapes[numRenderedShapes++] = shape;
							}

						}
					}
				}
			}
		}
	}
	
	/**
	 * draw all bodies, that are determined to be rendered.
	 * 
	 */
	
	protected Vec2f transformPosToVertex(Vec2f gamePos) {
		Vec2f cameraAlteredPos = new Vec2f(gamePos).sub(new Vec2f(renderer.camera.zeroPoint).mult(renderer.multiplicator));

		float x = cameraAlteredPos.getX();
		x /= renderer.resSpecs.halfGameWidth;
		x -= 1;

		float y = cameraAlteredPos.getY();
		y /= renderer.resSpecs.halfGameHeight;
		y -= 1;

		return new Vec2f(x, y);
	}

}
