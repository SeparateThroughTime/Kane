package kane.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import kane.exceptions.CompileShaderException;
import kane.exceptions.LinkShaderException;
import kane.math.Vec2f;
import kane.math.Vec2i;

public class Shader{

    protected int shaderProgramID;
    protected int vertexID;
    protected int fragmentID;

    private String vertexSource;
    private String fragmentSource;
    private final String vertexFilepath;
    private final String fragmentFilepath;

    public Shader(String vertexFilepath, String fragmentFilepath) throws IOException{
        this.vertexFilepath = vertexFilepath;
        this.fragmentFilepath = fragmentFilepath;
        readFile();

    }

    private void readFile() throws IOException{
        vertexSource = new String(Files.readAllBytes(Paths.get(vertexFilepath)));
        fragmentSource = new String(Files.readAllBytes(Paths.get(fragmentFilepath)));
    }

    public void compile() throws CompileShaderException, LinkShaderException{
        loadVertexShader();
        loadFragmentShader();
        linkShaders();
    }

    private void loadVertexShader() throws CompileShaderException{
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            String compileLog = glGetShaderInfoLog(vertexID, len);
            throw new CompileShaderException(vertexFilepath, "Vertex", compileLog);
        }
    }

    private void loadFragmentShader() throws CompileShaderException{
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        int success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            String compileLog = glGetShaderInfoLog(fragmentID, len);
            throw new CompileShaderException(fragmentFilepath, "Fragment", compileLog);
        }
    }

    private void linkShaders() throws LinkShaderException{
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            String programInfoLog = glGetProgramInfoLog(shaderProgramID, len);
            throw new LinkShaderException(vertexFilepath, fragmentFilepath, shaderProgramID, programInfoLog);
        }
    }

    public void use(){
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadVec2f(String varName, Vec2f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadVec2i(String varName, Vec2i vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2i(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, Float val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLocation, array);
    }
}
