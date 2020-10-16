package kane.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

public class Sprite {
	// This class manages sprites.
	
	public final int PIXELS = 32;

	// FRAME_WIDTH and FRAME_HEIGHT defines how many 32x32 blocks are used for one
	// frame.
	private final int FRAME_WIDTH;
	private final int FRAME_HEIGHT;
	private int PIXEL_PER_FRAME;
	private int[][] spritePixels;
	
	private Map<SpriteState, int[]> states;

	public Sprite(File file, int frameWidth, int frameHeight) {
		this.FRAME_HEIGHT = frameHeight;
		this.FRAME_WIDTH = frameWidth;
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			int imgWidth = img.getWidth(null);
			int imgHeight = img.getHeight(null);
			int pixelPerFrame = PIXELS * PIXELS * FRAME_WIDTH * FRAME_HEIGHT;
			this.PIXEL_PER_FRAME = pixelPerFrame;
			int frameCount = (imgWidth * imgHeight) / pixelPerFrame;
			spritePixels = new int[frameCount][pixelPerFrame];
			int pixelPerFrameX = PIXELS * FRAME_WIDTH;
			int pixelPerFrameY = PIXELS * FRAME_HEIGHT;
			int frameCountX = imgWidth / pixelPerFrameX;
//			int frameCountY = imgWidth / pixelPerFrameY;

			for (int x = 0; x < frameWidth; x++) {
				for (int y = 0; y < frameHeight; y++) {
					int pixel = img.getRGB(x, y);

					// This is the position of the frame.
					int frameX = x / pixelPerFrameX;
					int frameY = y / pixelPerFrameY;
					int frameNo = frameX + frameY * frameCountX;

					// This is the position of the pixel inside of the frame.
					int pixelX = x - pixelPerFrameX * frameX;
					int pixelY = y - pixelPerFrameY * frameY;
					int pixelNo = pixelX + pixelY * pixelPerFrameX;

					spritePixels[frameNo][pixelNo] = pixel;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setState(SpriteState state, int[] frameNumbers) {
		// Defines which frames are used for the specific state.
		states.put(state, frameNumbers);
	}
	
	public int[] getFrame(SpriteState state, int frameNo) {
		// Returns the frame of a state with the specific frameNo.
		
		// Transfer the Number of the frame inside the whole sprite to the number of the frame inside the state.
		frameNo = states.get(state)[frameNo];
		int[] frame = spritePixels[frameNo];
		return frame;
	}
	
	public int getFrameCount(SpriteState state) {
		return states.get(state).length;
	}
}
