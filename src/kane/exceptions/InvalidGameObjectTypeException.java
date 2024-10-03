package kane.exceptions;

public class InvalidGameObjectTypeException extends Exception{
    public InvalidGameObjectTypeException(String typeError, String filepath) {
        super("Error in Game Object '" + filepath + "': " + typeError);
    }
}
