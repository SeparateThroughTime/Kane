package kane.renderer;

public class ResolutionSpecification{
    public static ResolutionSpecification RES_SPECS;

    public final int GAME_HEIGHT;
    public int gameWidth;
    public int height;
    public int width;
    public float halfGameWidth;
    public float halfWidth;
    public float halfGameHeight;
    public float halfHeight;

    private ResolutionSpecification(int gameHeight, int gameWidth, int height, int width){
        this.GAME_HEIGHT = gameHeight;
        this.gameWidth = gameWidth;
        this.height = height;
        this.width = width;
        this.halfWidth = (float) width / 2;
        this.halfHeight = (float) height / 2;
        this.halfGameHeight = (float) gameHeight / 2;
        this.halfGameWidth = (float) gameWidth / 2;
    }

    public static void initializeResSpecs(int gameHeight, int gameWidth, int height, int width){
        if (RES_SPECS == null){
            RES_SPECS = new ResolutionSpecification(gameHeight, gameWidth, height, width);
        }
    }
}
