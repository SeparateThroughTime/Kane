package kane.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import kane.math.ImageAlteration;

public class Background{
	private ByteBuffer img;
	private int offsetX;
	public int WIDTH;
	public int HEIGHT;
	
	public Background(File file, int gameHeight) {
		BufferedImage awtImg;
		try {
			awtImg = ImageIO.read(file);
			awtImg = ImageAlteration.resizeWithHeight(awtImg, gameHeight);
			img = ImageAlteration.bufferedImageToByteBuffer(awtImg);
			WIDTH = awtImg.getWidth();
			HEIGHT = awtImg.getHeight();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.offsetX = 0;
	}
	
	public void setOffsetX(int offsetX) {
		offsetX = -(offsetX % WIDTH);
		this.offsetX = offsetX;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public ByteBuffer getImg() {
		return img;
	}
}
