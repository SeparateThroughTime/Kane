package kane.renderer;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import kane.math.ImageAlteration;

public class Background{
	private BufferedImage img;
	private int offsetX;
	
	public Background(File file, int gameHeight) {
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			this.img = ImageAlteration.resizeWithHeight(img, gameHeight);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		this.offsetX = 0;
	}
	
	public void setOffsetX(int offsetX) {
		offsetX = -(offsetX % img.getWidth());
		this.offsetX = offsetX;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public BufferedImage getImg() {
		return img;
	}
}
