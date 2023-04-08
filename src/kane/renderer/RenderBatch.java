package kane.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.List;

import kane.physics.Shape;
import renderer.Shader;
import renderer.Texture;

public class RenderBatch {
	private static final int POS_SIZE = 2;
	private final static int COLOR_SIZE = 4;
	private final static int TEX_COORDS_SIZE = 2;
	private final static int TEX_ID_SIZE = 1;

	private final static int POS_OFFSET = 0;
	private final static int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final static int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final static int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
	private final static int VERTEX_SIZE = 9;
	private final static int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private Shape[] shapes;
	private int numShapes;
	// One shape can have more than one Sprite to draw in one Frame.
	private int numDrawings;
	private boolean hasRoom;
	private float[] vertices;
	private int[] texSlots = { 0, 1, 2, 3, 4, 5, 6, 7 };

	private List<Texture> textures;
	private int vaoID, vboID;
	private int maxBatchSize;
	private Shader shader;

	public RenderBatch(int maxBatchSize, Shader shader) {
    	this.shader = shader;
    	this.maxBatchSize = maxBatchSize;
    	
    	vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
    	
    	this.numShapes = 0;
    	this.numSpriteFrames = 0;
    	this.hasRoom = true;
    	this.textures = new ArrayList<>();
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

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
	}
	
	public void addShape(Shape shape) {
		int index = this.numShapes;
		shapes[index] = shape;
		numShapes ++;
		
		SpriteController[] spriteControllers = shape.getSpriteControllers();
		int drawingsInShape = spriteControllers.length;
		numDrawings += drawingsInShape;
		
		for (SpriteController spriteController : spriteControllers) {
			Texture texture = spriteController.getFrame();
			
		}
	}
}
