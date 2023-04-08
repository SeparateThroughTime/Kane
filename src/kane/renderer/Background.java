package kane.renderer;

import java.nio.ByteBuffer;

public class Background{
	private Texture texture;
	private int offsetX;
	
	public Background(String filepath) {
		texture = new Texture(filepath);
		
		this.offsetX = 0;
	}
	
	public void setOffsetX(int offsetX) {
		offsetX = -(offsetX % texture.WIDTH);
		this.offsetX = offsetX;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public Texture getTexture() {
		return texture;
	}
}
