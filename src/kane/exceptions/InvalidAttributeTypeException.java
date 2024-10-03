package kane.exceptions;

public class InvalidAttributeTypeException extends Exception{
    public InvalidAttributeTypeException(String typeError, String filepath){
        super("Attribute error in file: '" + filepath + "': " + typeError);
    }
}
