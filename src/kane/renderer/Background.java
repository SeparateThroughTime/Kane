package kane.renderer;

import java.nio.ByteBuffer;

public class Background{
	public SpriteController spriteController;
	private int offsetX;
	
	public Background(String filepath) {
		spriteController = new SpriteController(new Sprite(filepath));
		
		this.offsetX = 0;
	}
	
	public void setOffsetX(int offsetX) {
		offsetX = -(offsetX % spriteController.sprite.FRAME_WIDTH);
		this.offsetX = offsetX;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
}
