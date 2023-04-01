package kane.renderer;

import static kane.physics.Physics.PHYSICS;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
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
import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import kane.math.Vec2f;
import kane.physics.Body;
import kane.physics.Shape;
import kane.physics.ShapeType;

public abstract class Drawer {
	protected Shape[] renderedShapes;
	protected int numRenderedShapes;

	public final int POSITION_SIZE = 3;
	public final int POSITION_OFFSET;
	public final int COLOR_SIZE = 4;
	public final int COLOR_OFFSET;
	public final int UV_SIZE = 2;
	public final int UV_OFFSET;
	public final int TEX_ID_SIZE = 1;
	public final int TEX_ID_OFFSET;
	public final int VERTEX_SIZE;
	public final int VERTEX_SIZE_BYTE;
	public final int ELEMENT_SIZE;
	public final int DRAW_TYPE;
	public boolean DRAW_IMAGES;
	protected ShapeType[] renderedShapeTypes;

	protected float[] vertices;
	protected int countCurrentVertices;
	protected int[] elements;
	protected int countCurrentElements;
	protected int elementsToDraw;

	protected int vertexArrayObjectID;
	protected int vertexBufferObjectID;
	protected int elementBufferObjectID;

	public abstract void drawBodies();

	protected abstract void initVerticesAndElements();

	public Drawer(int elementSize, int glDrawType, ShapeType[] renderedShapeTypes) {
		this(elementSize, glDrawType);
		this.renderedShapeTypes = renderedShapeTypes;
		DRAW_IMAGES = false; //this overwrites the boolean from other constructor!
	}

	public Drawer(int elementSize, int glDrawType) {
		DRAW_IMAGES = true;
		ELEMENT_SIZE = elementSize;
		DRAW_TYPE = glDrawType;
//		UV_SIZE = uvSize;
//		TEX_ID_SIZE = UV_SIZE == 0 ? 0 : 1;
		VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + UV_SIZE + TEX_ID_SIZE;
		VERTEX_SIZE_BYTE = VERTEX_SIZE * Float.BYTES;

		POSITION_OFFSET = 0;
		COLOR_OFFSET = POSITION_SIZE * Float.BYTES;
		UV_OFFSET = (POSITION_SIZE + COLOR_SIZE) * Float.BYTES;
		TEX_ID_OFFSET = (POSITION_SIZE + COLOR_SIZE + UV_SIZE) * Float.BYTES;

		initVerticesAndElements();
		init();
	}

	protected void init() {
		vertexArrayObjectID = glGenVertexArrays();
		glBindVertexArray(vertexArrayObjectID);

		vertexBufferObjectID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectID);
		glBufferData(GL_ARRAY_BUFFER, vertices.length, GL_DYNAMIC_DRAW);

		elementBufferObjectID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements.length, GL_DYNAMIC_DRAW);

		glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTE, POSITION_OFFSET);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTE, COLOR_OFFSET);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, UV_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTE, UV_OFFSET);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTE, TEX_ID_OFFSET);
		glEnableVertexAttribArray(3);
	}

//	protected void initVerticesAndElements() {
//		int numVertices = 0;
//		int numElements = 0;
//		if (UV_SIZE == 0) {
//			for (int i = 0; i < numRenderedShapes; i++) {
//				Shape s = renderedShapes[i];
//				numVertices += s.numRenderVertices;
//				numElements += s.numRenderElements;
//			}
//		} else {
//			numVertices = numRenderedShapes * 4;
//			numElements = numRenderedShapes * 2;
//		}
//		vertices = new float[numVertices * VERTEX_SIZE];
//		elements = new int[numElements * ELEMENT_SIZE];
//	}

	public void displayFrame(Shader shader) {
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectID);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
//		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_DYNAMIC_DRAW);
//		glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, elements);

		shader.use();

		glBindVertexArray(vertexArrayObjectID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);

		glDrawElements(DRAW_TYPE, ELEMENT_SIZE * elementsToDraw, GL_UNSIGNED_INT, 0);

		// Unbind
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
		shader.detach();
	}

	/**
	 * Determines which Shapes will be rendered.
	 */
	protected void chooseRenderedShapes() {
		initVerticesAndElements();
		numRenderedShapes = 0;
		elementsToDraw = 0;
		renderedShapes = new Shape[PHYSICS.numBodies * Body.MAX_SHAPES];
		for (int i = 0; i < PHYSICS.numBodies; i++) {
			Body body = PHYSICS.bodies[i];
			if (!body.isRemoved()) {
				for (int j = 0; j < body.numShapes; j++) {
					Shape shape = body.shapes[j];
					if (shape.aabb.overlaps(CAMERA.window)) {
						if (shape.visible) {
							if (!DRAW_IMAGES && !shape.hasSprite) {
								for (int k = 0; k < renderedShapeTypes.length; k++) {
									ShapeType shapeType = renderedShapeTypes[k];
									if (shapeType.equals(shape.type)) {
										renderedShapes[numRenderedShapes++] = shape;
										elementsToDraw += shape.numRenderElements;
										break;
									}
								}
							} else {
								if (shape.hasSprite) {
									renderedShapes[numRenderedShapes++] = shape;
									elementsToDraw += shape.getSpriteControllers().length * 2;
								}
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
		Vec2f cameraAlteredPos = new Vec2f(gamePos).sub(new Vec2f(CAMERA.zeroPoint).mult(RENDERER.multiplicator));

		float x = cameraAlteredPos.x;
		x /= RES_SPECS.halfGameWidth;
		x -= 1;

		float y = cameraAlteredPos.y;
		y /= RES_SPECS.halfGameHeight;
		y -= 1;

		return new Vec2f(x, y);
	}

}
