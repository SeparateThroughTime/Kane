package kane.exceptions;

public class CompileShaderException extends Exception{

    public CompileShaderException(String filepath, String shaderType, String compileLog){
        super("ERROR: Unable to compile " + shaderType + " Shader!\nFilepath: '" + filepath + "'\nShader Info Log:\n"
                + compileLog);
    }

}
