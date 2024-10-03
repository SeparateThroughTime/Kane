package kane.exceptions;

public class InvalidGameObjectFormatException extends Exception{
    public InvalidGameObjectFormatException(String formattingError, String filepath){
        super("Invalid Syntax for Game Object '" + filepath + "': " + formattingError);
    }
}
