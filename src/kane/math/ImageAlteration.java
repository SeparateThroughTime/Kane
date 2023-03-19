package kane.math;

import static kane.renderer.drawer.ImageDrawer.IMAGE_DRAWER;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

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

	// Code from:
	// https://computergraphics.stackexchange.com/questions/4936/lwjgl-opengl-get-bufferedimage-from-texture-id
	public static ByteBuffer bufferedImageToByteBuffer(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width * height];

		img.getRGB(0, 0, width, height, pixels, 0, width);
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * IMAGE_DRAWER.COLOR_SIZE);

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int pixel = pixels[x + y * width];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}

		buffer.flip();
		return buffer;
	}
}
