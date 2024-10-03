package kane.exceptions;

public class LoadGameObjectException extends Exception{
    public LoadGameObjectException(String objectFilepath, Throwable e){
        super("Unable to load Game Object.\nFilepath: '" + objectFilepath + "'", e);
    }
}
