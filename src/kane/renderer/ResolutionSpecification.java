package kane.renderer;

/**
 * The ResolutionSpecification saves the information about absolut height and
 * width of the window and those in pseudo length unit.
 */
public class ResolutionSpecification {
	public final int GAME_HEIGHT;
	public int gameWidth;
	public int height;
	public int width;
	public float halfGameWidth;
	public float halfWidth;
	public float halfGameHeight;
	public float halfHeight;

	public ResolutionSpecification(int gameHeight, int gameWidth, int height, int width) {
		// gameWidth and gameHeight are the axis' in pseudo length unit.
		this.GAME_HEIGHT = gameHeight;
		this.gameWidth = gameWidth;
		this.height = height;
		this.width = width;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.halfGameHeight = gameHeight / 2;
		this.halfGameWidth = gameWidth / 2;
	}
}
