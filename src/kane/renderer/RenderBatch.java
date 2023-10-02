package kane.renderer;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import kane.math.Vec2f;
import kane.physics.Shape;

public abstract class RenderBatch <RenderObject> implements Comparable<RenderBatch>{

	
	protected final int VERTEX_SIZE;
	protected final int VERTEX_SIZE_BYTES;
	private final int DRAW_TYPE;
	
	protected boolean hasRoom;
	protected float[] vertices;
	protected int[] texSlots = { 0, 1, 2, 3, 4, 5, 6, 7 };
	protected int numElements;
	
	protected int vaoID, vboID;
	protected int maxBatchSize;
	
	public final int RENDER_LAYER;
	protected final int ELEMENTS_PER_RENDEROBJECT;
	
	public abstract void add(RenderObject renderObject);
	protected abstract void loadVertices();
	protected abstract void bindTextures();
	protected abstract void setNumElements();
	protected abstract void unbindTextures();
	protected abstract int[] generateIndices();
	protected abstract void enableBufferAttributePointer();
	protected abstract void enableVertexAttribArrays();
	protected abstract void disableVertexAttribArrays();
	
	public RenderBatch(int maxBatchSize, int renderLayer, int elementsPerRenderObject, int vertexSize, int drawType) {
		this.maxBatchSize = maxBatchSize;
		this.RENDER_LAYER = renderLayer;
		this.ELEMENTS_PER_RENDEROBJECT = elementsPerRenderObject;
		this.VERTEX_SIZE = vertexSize;
		this.VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
		this.DRAW_TYPE = drawType;
		
		// TODO: Can crash because too low. Need to change shapes to spriteControllers in children.
		vertices = new float[maxBatchSize * elementsPerRenderObject * VERTEX_SIZE];
		this.hasRoom = true;
	}
	
	public void start() {
		// Generate and bind a Vertex Array Object
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// Allocate space for vertices
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

		// Create and upload indices buffer
		int eboID = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		enableBufferAttributePointer();
	}
	
	public void render() {
		
		loadVertices();

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

		RENDERER.shader.use();
		bindTextures();
		

		glBindVertexArray(vaoID);
		enableVertexAttribArrays();

		setNumElements();
		glDrawElements(DRAW_TYPE, numElements, GL_UNSIGNED_INT, 0);

		disableVertexAttribArrays();
		glBindVertexArray(0);

		unbindTextures();
		RENDERER.shader.detach();
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
	
	public boolean hasRoom() {
		return this.hasRoom;
	}
	
	@Override
	public int compareTo(RenderBatch o) {
		return Integer.compare(this.RENDER_LAYER, o.RENDER_LAYER);
	}
	
}
