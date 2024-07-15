package kane.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kane.math.Vec2f;
import kane.math.Vec4f;
import kane.physics.Shape;
import kane.physics.shapes.Point;

import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;

public class SpriteBatch implements Comparable<SpriteBatch>{
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

    private final SpriteController invisibleSpriteController;
    private final Shape invisibleShape;

    private final List<Shape> shapes;
    private int numShapes;
    // One shape can have more than one Sprite to draw in one Frame.
    private int numDrawings;
    private final Map<Integer, Integer> shapeIndexToDrawingIndex;
    private boolean hasRoom;
    private final float[] vertices;
    private final int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private final List<Texture> textures;
    private int vaoID, vboID;
    private final int maxBatchSize;
    private final static int ELEMENTS_PER_RENDER_OBJECT = 4;

    public final int RENDER_LAYER;

    public SpriteBatch(int maxBatchSize, int renderLayer){
        this.maxBatchSize = maxBatchSize;

        vertices = new float[(maxBatchSize + Shape.MAX_SPRITE_CONTROLLERS) * 4 * VERTEX_SIZE];
        shapes = new ArrayList<>();

        this.numShapes = 0;
        this.numDrawings = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
        this.shapeIndexToDrawingIndex = new HashMap<>();
        this.RENDER_LAYER = renderLayer;

        this.invisibleSpriteController = new SpriteController(new Sprite("sprites//core//invisible.png", 1, 1));
        this.invisibleShape = new Point(0, 0, CAMERA, new Color(0, 0, 0, 0), null, 0);
    }

    public void start(){
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

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addShape(Shape shape){
        shapes.add(shape);
        shape.renderBatch = this;
        shapeIndexToDrawingIndex.put(numShapes, numDrawings);

        SpriteController[] spriteControllers = shape.getSpriteControllers();
        int drawingsInShape = spriteControllers.length;
        numShapes++;
        numDrawings += drawingsInShape;

        if (numDrawings >= Renderer.MAX_BATCH_SIZE || textures.size() >= Renderer.MAX_TEXTURES){
            hasRoom = false;
        }
    }

    public void addSpriteControllers(SpriteController[] spriteControllers){

        for (SpriteController spriteController : spriteControllers){
            Texture texture = spriteController.sprite.texture;
            if (texture != null){
                if (!textures.contains(texture)){
                    textures.add(texture);
                }
            }
        }
    }

    public void render(){

        for (int i = 0; i < numShapes; i++){
            if (shapes.get(i).visible && !shapes.get(i).body.isRemoved()){
                loadVertexProperties(i);
            } else{
                loadInvisibleProperties(i);


            }
        }


        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        RENDERER.shader.use();
        for (int i = 0; i < textures.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        RENDERER.shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, this.numDrawings * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);

        for (Texture texture : textures){
            texture.unbind();
        }

        RENDERER.shader.detach();
    }

    private void loadInvisibleProperties(int shapeIndex){
        int drawingIndex = shapeIndexToDrawingIndex.get(shapeIndex);
        Shape shape = shapes.get(shapeIndex);
        int offset = drawingIndex * ELEMENTS_PER_RENDER_OBJECT * VERTEX_SIZE;

        for (int spriteCounter = 0; spriteCounter < shape.getSpriteControllers().length; spriteCounter++){
            offset += loadVertexProperties(invisibleSpriteController, invisibleShape, offset);
        }
    }

    private void loadVertexProperties(int shapeIndex){
        int drawingIndex = shapeIndexToDrawingIndex.get(shapeIndex);
        Shape shape = shapes.get(shapeIndex);
        int offset = drawingIndex * ELEMENTS_PER_RENDER_OBJECT * VERTEX_SIZE;

        for (int spriteCounter = 0; spriteCounter < shape.getSpriteControllers().length; spriteCounter++){
            offset += loadVertexProperties(shape.getSpriteControllers()[spriteCounter], shape, offset);
        }
    }

    private int loadVertexProperties(SpriteController spriteController, Shape shape, int offset){
        Vec2f[] pos = calculateVertexPositions(spriteController, shape);
        Vec4f color = new Vec4f(shape.color);
        Vec2f[] texCoords = spriteController.getFrameTexCoords();
        Texture texture = spriteController.sprite.texture;

        int texId = 0;
        if (texture != null){
            for (int texCounter = 0; texCounter < textures.size(); texCounter++){
                if (textures.get(texCounter) == texture){
                    texId = texCounter + 1;
                    break;
                }
            }
        }

        for (int vertexCounter = 0; vertexCounter < ELEMENTS_PER_RENDER_OBJECT; vertexCounter++){
            offset += loadVertexProperties(pos[vertexCounter], color, texCoords[vertexCounter], texId, offset);
        }

        return VERTEX_SIZE * 4;

    }

    private int loadVertexProperties(Vec2f pos, Vec4f color, Vec2f texCoords, int texId, int offset){
        vertices[offset] = pos.x;
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

    private Vec2f[] calculateVertexPositions(SpriteController spriteController, Shape shape){
        Sprite sprite = spriteController.sprite;
        Vec2f scale = new Vec2f(spriteController.scale).mult(Sprite.SCALE);
        Vec2f shapePos = shape.getAbsPos();
        Vec2f spritePos = transformPosToVertex(new Vec2f(shapePos).add(spriteController.spritePosOffset));
        Vec2f dimension = new Vec2f((float) sprite.FRAME_WIDTH / RES_SPECS.halfGameWidth,
                (float) sprite.FRAME_HEIGHT / RES_SPECS.halfGameHeight).mult(scale).mult(RENDERER.multiplier);

        Vec2f[] vertexPositions = new Vec2f[4];
        vertexPositions[0] = spritePos;
        vertexPositions[1] = new Vec2f(spritePos.x + dimension.x, spritePos.y);
        vertexPositions[2] = new Vec2f(spritePos.x + dimension.x, spritePos.y + dimension.y);
        vertexPositions[3] = new Vec2f(spritePos.x, spritePos.y + dimension.y);
        return vertexPositions;
    }

    protected Vec2f transformPosToVertex(Vec2f gamePos){
        Vec2f cameraAlteredPos = new Vec2f(gamePos).sub(new Vec2f(CAMERA.zeroPoint).mult(RENDERER.multiplier));

        float x = cameraAlteredPos.x;
        x /= RES_SPECS.halfGameWidth;
        x -= 1;

        float y = cameraAlteredPos.y;
        y /= RES_SPECS.halfGameHeight;
        y -= 1;

        return new Vec2f(x, y);
    }

    private int[] generateIndices(){
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index){
        int offsetArrayIndex = 6 * index;
        int offset = ELEMENTS_PER_RENDER_OBJECT * index;

        // 3, 2, 0, 0, 2, 1 7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    @Override
    public int compareTo(SpriteBatch o){
        return Integer.compare(this.RENDER_LAYER, o.RENDER_LAYER);
    }
}