package kane.exceptions;

public class WriteJsonException extends Exception {
    public WriteJsonException(String filepath, Throwable e) {
        super("Unable to write to file '" + filepath + "'", e);
    }
}
