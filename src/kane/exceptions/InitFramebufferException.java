package kane.exceptions;

public class InitFramebufferException extends Exception{
    public InitFramebufferException(int fboStatus){
        super("Error initializing OpenGL Framebuffer! Error code: " + fboStatus);
    }
}
