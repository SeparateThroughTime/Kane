package kane.exceptions;

public class LoadSoundException extends Exception{
    public LoadSoundException(String filepath, Throwable e){
        super("ERROR: Unable to load soundfile!\nFilepath: '" + filepath + "'", e);
    }
}
