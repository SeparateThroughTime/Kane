package kane.exceptions;

public class LoadTextureException extends Exception{

    public LoadTextureException(String filepath, Throwable e){
        super("Unable to load Texture with filepath: '" + filepath + "'", e);
    }

}
