package kane.exceptions;

public class LoadJsonException extends Exception {
    public LoadJsonException(String filepath, Throwable e) {
        super("Unable to load file '" + filepath + "'", e);
    }
}
