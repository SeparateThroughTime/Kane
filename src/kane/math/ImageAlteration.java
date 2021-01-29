package kane.math;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageAlteration {

	// Code from:
	// https://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java
	public static BufferedImage resizeWithHeight(BufferedImage img, int height) {
		int width = (img.getWidth() * height) / img.getHeight();
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = newImg.createGraphics();
		try {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.clearRect(0, 0, width, height);
			g2d.drawImage(img, 0, 0, width, height, null);
		} finally {
			g2d.dispose();
		}
		return newImg;
	}
}
