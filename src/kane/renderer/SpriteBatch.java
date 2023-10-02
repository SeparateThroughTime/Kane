package kane.renderer;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kane.math.Vec2f;
import kane.math.Vec4f;
import kane.physics.Shape;
import kane.physics.shapes.Point;

public class SpriteBatch extends RenderBatch<Shape>{
	
	protected static final int POS_SIZE = 2;
	protected final static int COLOR_SIZE = 4;
	protected final static int TEX_COORDS_SIZE = 2;
	protected final static int TEX_ID_SIZE = 1;
	
	protected final static int POS_OFFSET = 0;
	protected final static int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	protected final static int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	protected final static int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

	private final SpriteController invisibleSpriteController;
	private final Shape invisibleShape;

	private List<Shape> shapes;
	private int numShapes;
	// One shape can have more than one Sprite to draw in one Frame.
	private int numDrawings;
	private Map<Integer, Integer> shapeIndexToDrawingIndex;

	private List<Texture> textures;

	public SpriteBatch(int maxBatchSize, int renderLayer) {
		super(maxBatchSize, renderLayer, 6, 9, GL_TRIANGLES);
		shapes = new ArrayList<>();

		this.numShapes = 0;
		this.numDrawings = 0;
		this.textures = new ArrayList<>();
		this.shapeIndexToDrawingIndex = new HashMap<>();

		this.invisibleSpriteController = new SpriteController(new Sprite("", 1, 1));
		this.invisibleShape = new Point(0, 0, CAMERA, new Color(0, 0, 0, 0), null, 0);
	}

	public void add(Shape shape) {
		shapes.add(shape);
		shape.renderBatch = this;
		shapeIndexToDrawingIndex.put(numShapes, numDrawings);

		SpriteController[] spriteControllers = shape.getSpriteControllers();
		int drawingsInShape = spriteControllers.length;
		numShapes++;
		numDrawings += drawingsInShape;

		if (numDrawings >= maxBatchSize) {
			hasRoom = false;
		}
	}

	public void addSpriteControllers(SpriteController[] spriteControllers) {

		for (SpriteController spriteController : spriteControllers) {
			Texture texture = spriteController.sprite.texture;
			if (texture != null) {
				if (!textures.contains(texture)) {
					textures.add(texture);
				}
			}
		}

		if (textures.size() > Renderer.MAX_GLTEXTURES - Shape.MAX_SPRITE_CONTROLLERS) {
			hasRoom = false;
		}
	}

	protected void loadVertices() {

		for (int i = 0; i < numShapes; i++) {
			if (shapes.get(i).visible && !shapes.get(i).body.isRemoved()) {
				loadVertexProperties(i);
			} else {
				loadInvisibleProperties(i);

			}
		}

	}
	
	protected void enableBufferAttributePointer() {
		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
		glEnableVertexAttribArray(3);
	}
	
	protected void enableVertexAttribArrays() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
	}
	
	protected void disableVertexAttribArrays() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}

	protected void bindTextures() {
		for (int i = 0; i < textures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + i + 1);
			textures.get(i).bind();
		}
		RENDERER.shader.uploadIntArray("uTextures", texSlots);
		
	}
	
	public void setNumElements() {
		numElements = numDrawings * ELEMENTS_PER_RENDEROBJECT;
	}
	
	public void unbindTextures() {
		for (int i = 0; i < textures.size(); i++) {
			textures.get(i).unbind();
		}

		
	}

	private void loadInvisibleProperties(int shapeIndex) {
		int drawingIndex = shapeIndexToDrawingIndex.get(shapeIndex);
		Shape shape = shapes.get(shapeIndex);
		int offset = drawingIndex * 4 * VERTEX_SIZE;

		for (int spriteCounter = 0; spriteCounter < shape.getSpriteControllers().length; spriteCounter++) {
			offset += loadVertexProperties(invisibleSpriteController, invisibleShape, offset);
		}
	}

	private void loadVertexProperties(int shapeIndex) {
		int drawingIndex = shapeIndexToDrawingIndex.get(shapeIndex);
		Shape shape = shapes.get(shapeIndex);
		int offset = drawingIndex * 4 * VERTEX_SIZE;

		for (int spriteCounter = 0; spriteCounter < shape.getSpriteControllers().length; spriteCounter++) {
			offset += loadVertexProperties(shape.getSpriteControllers()[spriteCounter], shape, offset);
		}
	}

	protected int loadVertexProperties(SpriteController spriteController, Shape shape, int offset) {
		Vec2f[] pos = calculateVertexPositions(spriteController, shape);
		Vec4f color = new Vec4f(shape.color);
		Vec2f[] texCoords = spriteController.getFrameTexCoords();
		Texture texture = spriteController.sprite.texture;

		int texId = 0;
		if (texture != null) {
			for (int texCounter = 0; texCounter < textures.size(); texCounter++) {
				if (textures.get(texCounter) == texture) {
					texId = texCounter + 1;
					break;
				}
			}
		}

		for (int vertexCounter = 0; vertexCounter < 4; vertexCounter++) {
			offset += loadVertexProperties(pos[vertexCounter], color, texCoords[vertexCounter], texId, offset);
		}

		return VERTEX_SIZE * 4;

	}

	protected int loadVertexProperties(Vec2f pos, Vec4f color, Vec2f texCoords, int texId, int offset) {
		vertices[offset + 0] = pos.x;
		vertices[offset + 1] = pos.y;

		vertices[offset + 2] = color.x;
		vertices[offset + 3] = color.y;
		vertices[offset + 4] = color.z;
		vertices[offset + 5] = color.w;

		vertices[offset + 6] = texCoords.x;
		vertices[offset + 7] = texCoords.y;

		vertices[offset + 8] = texId;
		return VERTEX_SIZE;
	}

	protected Vec2f[] calculateVertexPositions(SpriteController spriteController, Shape shape) {
		Sprite sprite = spriteController.sprite;
		Vec2f scale = new Vec2f(spriteController.scale).mult(Sprite.SCALE);
		Vec2f shapePos = shape.getAbsPos();
		Vec2f spritePos = transformPosToVertex(new Vec2f(shapePos).add(spriteController.spritePosOffset));
		Vec2f dimension = new Vec2f((float) sprite.FRAME_WIDTH / RES_SPECS.halfGameWidth,
				(float) sprite.FRAME_HEIGHT / RES_SPECS.halfGameHeight).mult(scale).mult(RENDERER.multiplicator);

		Vec2f[] vertexPositions = new Vec2f[4];
		vertexPositions[0] = spritePos;
		vertexPositions[1] = new Vec2f(spritePos.x + dimension.x, spritePos.y);
		vertexPositions[2] = new Vec2f(spritePos.x + dimension.x, spritePos.y + dimension.y);
		vertexPositions[3] = new Vec2f(spritePos.x, spritePos.y + dimension.y);
		return vertexPositions;
	}

	protected int[] generateIndices() {
		int[] elements = new int[ELEMENTS_PER_RENDEROBJECT * maxBatchSize];
		for (int i = 0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);
		}

		return elements;
	}

	protected void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = ELEMENTS_PER_RENDEROBJECT * index;
		int offset = 4 * index;

		// 3, 2, 0, 0, 2, 1 7, 6, 4, 4, 6, 5
		// Triangle 1
		elements[offsetArrayIndex] = offset + 3;
		elements[offsetArrayIndex + 1] = offset + 2;
		elements[offsetArrayIndex + 2] = offset + 0;

		// Triangle 2
		elements[offsetArrayIndex + 3] = offset + 0;
		elements[offsetArrayIndex + 4] = offset + 2;
		elements[offsetArrayIndex + 5] = offset + 1;
	}

	//TODO: Delete
//	public boolean hasTextureRoom() {
//		return this.textures.size() < 8;
//	}

	public boolean hasTexture(Texture tex) {
		return this.textures.contains(tex);
	}
}
