package kane.renderer;

import kane.exceptions.InitFramebufferException;

import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.renderer.Renderer.RENDERER;

import static org.lwjgl.opengl.GL30.*;

public class PostProcessBatch{

    private static final int POS_SIZE = 2;
    private static final int TEX_COORDS_SIZE = 2;
    private static final int POS_OFFSET = 0;
    private static final int TEX_COORDS_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE = POS_SIZE + TEX_COORDS_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private int vaoID, vboID, fboID, rboID;
    private Texture frameBufferTexture;
    // @formatter:off
    private static final float[] vertices =
            //x      y     Tex Coords
            {-1.0f,  1.0f, 0.0f, 1.0f,
             -1.0f, -1.0f, 0.0f, 0.0f,
              1.0f, -1.0f, 1.0f, 0.0f,
              1.0f,  1.0f, 1.0f, 1.0f,};

    private static final int[] inidices = {
            //Triangle 1
            3, 2, 0,
            //Triangle 2
            0, 2, 1
    };
    // @formatter:on

    public PostProcessBatch() throws InitFramebufferException{
        initBuffers();
        initAttribPointer();
        initTexture();
        initRenderBuffer();
        checkErrors();
        detachFramebuffer();
    }

    private void checkErrors() throws InitFramebufferException{
        int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (fboStatus != GL_FRAMEBUFFER_COMPLETE){
            throw new InitFramebufferException(fboStatus);
        }
    }

    private void initBuffers(){
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_STATIC_DRAW);

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, inidices, GL_STATIC_DRAW);

    }

    private void initAttribPointer(){
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);

    }

    private void initTexture(){
        frameBufferTexture = new Texture(RES_SPECS.width, RES_SPECS.height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, frameBufferTexture.TEX_ID, 0);

        RENDERER.postShader.uploadInt("textureID", 0);

    }

    private void initRenderBuffer(){
        rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, RES_SPECS.width, RES_SPECS.height);

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);
    }

    public void bindFramebuffer(){
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void detachFramebuffer(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void render(){
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        RENDERER.postShader.use();
        glActiveTexture(GL_TEXTURE0);
        frameBufferTexture.bind();
        RENDERER.postShader.uploadInt("textureID", 0);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        frameBufferTexture.unbind();
        RENDERER.postShader.detach();

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
