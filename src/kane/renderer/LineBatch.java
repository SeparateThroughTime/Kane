package kane.renderer;

import kane.math.Vec2f;
import kane.math.Vec4f;
import kane.physics.Shape;
import kane.physics.shapes.LineSegment;
import kane.physics.shapes.Plane;

import java.util.ArrayList;
import java.util.List;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL46.GL_LINES;

public class LineBatch {
	private static final int POS_SIZE = 2;
	private final static int COLOR_SIZE = 4;
	private final static int ELEMENT_SIZE = 1;

	private final static int POS_OFFSET = 0;
	private final static int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final static int VERTEX_SIZE = 6;
	private final static int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private final List<Shape> shapes;
	private int numShapes;
	private boolean hasRoom;
	private final float[] vertices;

	private int vaoID, vboID;
	private final int maxBatchSize;
	private final static int ELEMENTS_PER_RENDEROBJECT = 2;

	public LineBatch(int maxBatchSize) {
		this.maxBatchSize = maxBatchSize;

		vertices = new float[maxBatchSize * ELEMENTS_PER_RENDEROBJECT * VERTEX_SIZE];
		shapes = new ArrayList<>();

		this.numShapes = 0;
		this.hasRoom = true;
	}

	public void start() {
		// Generate and bind a Vertex Array Object
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// Allocate space for vertices
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

		// Create and upload indices buffer
		int eboID = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		// Enable the buffer attribute pointers
		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);
	}

	public void addShape(Shape shape) {
		shapes.add(shape);
		numShapes++;

		if (numShapes >= maxBatchSize) {
			hasRoom = false;
		}
	}

	public void render() {

		for (int i = 0; i < numShapes; i++) {
			if (shapes.get(i).visible && !shapes.get(i).body.isRemoved()) {
				loadVertexProperties(i);
			} else {
				loadInvisibleProperties(i);
			}
		}

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

		// Use shader
		RENDERER.shader.use();

		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_LINES, this.numShapes * ELEMENTS_PER_RENDEROBJECT, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);

		RENDERER.shader.detach();
	}

	private void loadInvisibleProperties(int shapeIndex) {
		int offset = shapeIndex * ELEMENTS_PER_RENDEROBJECT * VERTEX_SIZE;

		Vec2f pos = new Vec2f();
		Vec4f color = new Vec4f(0, 0, 0, 0);

		for (int vertexCounter = 0; vertexCounter < 2; vertexCounter++) {
			offset += loadVertexProperties(pos, color, offset);
		}
	}

	private void loadVertexProperties(int shapeIndex) {
		Shape shape = shapes.get(shapeIndex);
		int offset = shapeIndex * ELEMENTS_PER_RENDEROBJECT * VERTEX_SIZE;

		Vec2f[] pos = calculateVertexPositions(shape);
		Vec4f color = new Vec4f(shape.color);

		for (int vertexCounter = 0; vertexCounter < ELEMENTS_PER_RENDEROBJECT; vertexCounter++) {
			offset += loadVertexProperties(pos[vertexCounter], color, offset);
		}
	}

	private int loadVertexProperties(Vec2f pos, Vec4f color, int offset) {
		vertices[offset] = pos.x;
		vertices[offset + 1] = pos.y;

		vertices[offset + 2] = color.x;
		vertices[offset + 3] = color.y;
		vertices[offset + 4] = color.z;
		vertices[offset + 5] = color.w;

		return VERTEX_SIZE;
	}

	private Vec2f[] calculateVertexPositions(Shape shape) {
		Vec2f shapePos = shape.getAbsPos();
		Vec2f[] vertexPositions = new Vec2f[ELEMENTS_PER_RENDEROBJECT];

		switch (shape.type) {
			case LINESEGMENT:
				LineSegment line = (LineSegment) shape;
				vertexPositions[0] = transformPosToVertex(new Vec2f(shapePos).add(line.getRelPosA()));
				vertexPositions[1] = transformPosToVertex(new Vec2f(shapePos).add(line.getRelPosB()));
				break;

			case PLANE:
				Plane plane = (Plane) shape;
				vertexPositions[0] = transformPosToVertex(new Vec2f(shapePos).add(plane.getPoint()));
				vertexPositions[1] = transformPosToVertex(
						new Vec2f(shapePos).add(plane.getPoint().addMult(plane.getNormal(), plane.getLen())));
				break;

			case POINT:
				vertexPositions[0] = transformPosToVertex(new Vec2f(shapePos));
				vertexPositions[1] = new Vec2f(vertexPositions[0]);

			default:
				vertexPositions[0] = new Vec2f();
				vertexPositions[1] = new Vec2f();
				break;
		}

		return vertexPositions;
	}

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

	private int[] generateIndices() {
		int[] elements = new int[ELEMENTS_PER_RENDEROBJECT * maxBatchSize];
		for (int i = 0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);
		}

		return elements;
	}

	private void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = ELEMENT_SIZE * ELEMENTS_PER_RENDEROBJECT * index;
		int offset = ELEMENTS_PER_RENDEROBJECT * index;

		// 3, 2, 0, 0, 2, 1 7, 6, 4, 4, 6, 5
		// Triangle 1
		elements[offsetArrayIndex] = offset;
		elements[offsetArrayIndex + 1] = offset + 1;
	}

	public boolean hasRoom() {
		return this.hasRoom;
	}

}