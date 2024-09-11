package kane.renderer;

import kane.math.Vec2f;
import kane.math.Vec4f;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.Renderer.RENDERER;
import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BackgroundBatch{
    private static final int POS_SIZE = 2;
    private final static int COLOR_SIZE = 4;
    private final static int TEX_COORDS_SIZE = 2;
    private final static int TEX_ID_SIZE = 1;

    private final static int POS_OFFSET = 0;
    private final static int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final static int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final static int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final static int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
    private final static int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private final static int ELEMENTS_PER_RENDER_OBJECT = 4;

    private final int maxBatchSize;
    private final int backgroundSize;
    private final float[] vertices;
    private final int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private final Background background;
    private int vaoID, vboID;

    public BackgroundBatch(Background background){
        backgroundSize = (int) (background.spriteController.sprite.FRAME_WIDTH * background.spriteController.scale.x);
        this.maxBatchSize = ResolutionSpecification.RES_SPECS.gameWidth / backgroundSize + 1;
        vertices = new float[maxBatchSize * ELEMENTS_PER_RENDER_OBJECT * VERTEX_SIZE];

        this.background = background;
    }

    public void start(){
        initBuffers();
        initAttribPointer();
        unbind();
    }

    private static void unbind(){
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private static void initAttribPointer(){
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    private void initBuffers(){
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_STATIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    public void render(){
        for (int i = 0; i < maxBatchSize; i++){
            loadVertexProperties(i);
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        RENDERER.shader.use();
        glActiveTexture(GL_TEXTURE0 + 1);
        background.spriteController.sprite.texture.bind();
        RENDERER.shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, maxBatchSize * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);

        background.spriteController.sprite.texture.unbind();
        RENDERER.shader.detach();

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void loadVertexProperties(int index){
        int offset = index * ELEMENTS_PER_RENDER_OBJECT * VERTEX_SIZE;

        Vec2f[] pos = calculateVertexPositions(index);
        Vec4f color = new Vec4f(0, 0, 0, 0);
        Vec2f[] texCoords = background.spriteController.getFrameTexCoords();

        int texId = 1;

        for (int vertexCounter = 0; vertexCounter < ELEMENTS_PER_RENDER_OBJECT; vertexCounter++){
            offset += loadVertexProperties(pos[vertexCounter], color, texCoords[vertexCounter], texId, offset);
        }
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

    private Vec2f[] calculateVertexPositions(int index){
        Sprite sprite = background.spriteController.sprite;
        Vec2f scale = new Vec2f(background.spriteController.scale).mult(Sprite.SCALE);
        Vec2f gamePos = new Vec2f(background.getOffsetX() + index * backgroundSize * Sprite.SCALE, 0);
        Vec2f monitorPos = transformPosToVertex(gamePos);
        Vec2f dimension = new Vec2f((float) sprite.FRAME_WIDTH / RES_SPECS.halfGameWidth,
                (float) sprite.FRAME_HEIGHT / RES_SPECS.halfGameHeight).mult(scale).mult(RENDERER.multiplier);

        Vec2f[] vertexPositions = new Vec2f[ELEMENTS_PER_RENDER_OBJECT];
        vertexPositions[0] = monitorPos;
        vertexPositions[1] = new Vec2f(monitorPos.x + dimension.x, monitorPos.y);
        vertexPositions[2] = new Vec2f(monitorPos.x + dimension.x, monitorPos.y + dimension.y);
        vertexPositions[3] = new Vec2f(monitorPos.x, monitorPos.y + dimension.y);
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
}
