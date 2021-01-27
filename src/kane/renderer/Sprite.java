package kane.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import kane.math.Vec2f;

public class Sprite {
	// This class manages sprites.

	public static final int PIXELS = 32;
	public static final int ANIMATION_RATE = 10;

	// FRAME_WIDTH and FRAME_HEIGHT defines how many 32x32 blocks are used for one
	// frame.
	public final int FRAME_WIDTH;
	public final int FRAME_HEIGHT;
	private int PIXEL_PER_FRAME;
	private int[][] spritePixels;

	protected SpriteState currentSpriteState;
	protected int currentSpriteStateFrame;
	protected Vec2f spritePosOffset;
	private int frameCounter;

	private Map<SpriteState, int[]> states;

	public Sprite(File file, int frameWidth, int frameHeight) {
		this.FRAME_HEIGHT = frameHeight;
		this.FRAME_WIDTH = frameWidth;
		this.states = new HashMap<SpriteState, int[]>();
		this.spritePosOffset = new Vec2f();
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

			for (int x = 0; x < imgWidth; x++) {
				for (int y = 0; y < imgHeight; y++) {
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
	
	/**
	 * Defines which frames are used for the specific state.
	 * @param state
	 * @param frameNumbers
	 */
	public void addState(SpriteState state, int[] frameNumbers) {
		states.put(state, frameNumbers);
	}

	public int[] getFrame() {
		// Returns the frame of a state with the specific frameNo.

		// Transfer the Number of the frame inside the whole sprite to the number of the
		// frame inside the state.
		int frameNo = states.get(currentSpriteState)[currentSpriteStateFrame];
		int[] frame = spritePixels[frameNo];
		return frame;
	}

	public int getFrameCount(SpriteState state) {
		return states.get(state).length;
	}

	public void setCurrentSpriteState(SpriteState state) {
		this.currentSpriteState = state;
		this.currentSpriteStateFrame = 0;
	}

	public SpriteState getCurrentSpriteState() {
		return currentSpriteState;
	}

	/**
	 * get the current frame state of the sprite.
	 * 
	 * @return
	 */
	public int getCurrenSpriteStateFrame() {
		return currentSpriteStateFrame;
	}

	public void setSpritePosOffset(Vec2f offset) {
		this.spritePosOffset = offset;
	}

	public Vec2f getSpritePosOffset() {
		return spritePosOffset;
	}

	/**
	 * Need to run every frame. This produces animation.
	 */
	public void step() {
		if (frameCounter >= ANIMATION_RATE) {
			frameCounter = 0;
			stepCurrentSpriteStateFrame();
		}
		frameCounter++;
	}

	private void stepCurrentSpriteStateFrame() {
		currentSpriteStateFrame++;
		if (currentSpriteStateFrame >= getFrameCount(currentSpriteState)) {
			currentSpriteStateFrame = 0;
		}
	}
}
