package kane.exceptions;

public class LinkShaderException extends Exception{
    public LinkShaderException(String vertexFilepath, String fragmentFilepath, int shaderProgramID,
                               String programInfoLog){
        super("ERROR: Linking Shaders failed!\nVertex Filepath: '" + vertexFilepath + "'\nFragment Filepath: '"
                + fragmentFilepath + "'\nShader Program ID: " + shaderProgramID + "\nGL Program Info Log:\n"
                + programInfoLog);
    }
}
