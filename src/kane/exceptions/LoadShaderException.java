package kane.exceptions;

public class LoadShaderException extends Exception{
    public LoadShaderException(String vertexFilepath, String fragmentFilepath, Throwable e){
        super("Unable to load Shader.\nVertex Filepath: '" + vertexFilepath + "'\nFragment Filepath: '"
                + fragmentFilepath + "'", e);
    }
}
