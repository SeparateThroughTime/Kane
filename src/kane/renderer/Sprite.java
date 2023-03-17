package kane.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import kane.math.ArrayOperations;
import kane.math.Vec2f;

public class Sprite {
	// This class manages sprites.

	public static final float SCALE = 2f;

	// FRAME_WIDTH and FRAME_HEIGHT defines how many 32x32 blocks are used for one
	// frame.
	public final int FRAME_WIDTH;
	public final int FRAME_HEIGHT;
	public int PIXEL_PER_FRAME;
//	private int[][] spritePixels;
	private BufferedImage[] frames;
	protected SpriteState[] assignedSpriteStates;

	private Map<SpriteState, int[]> states;
	
	public Sprite(BufferedImage img, int frameWidth, int frameHeight) {
		assignedSpriteStates = new SpriteState[0];
		this.FRAME_HEIGHT = frameHeight;
		this.FRAME_WIDTH = frameWidth;
		this.states = new HashMap<SpriteState, int[]>();
		init(img);
		
	}

	public Sprite(File file, int frameWidth, int frameHeight) {
		assignedSpriteStates = new SpriteState[0];
		this.FRAME_HEIGHT = frameHeight;
		this.FRAME_WIDTH = frameWidth;
		this.states = new HashMap<SpriteState, int[]>();
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			init(img);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		 
	}
	
	private void init(BufferedImage img) {
		int imgWidth = img.getWidth(null);
		int imgHeight = img.getHeight(null);
		this.PIXEL_PER_FRAME = FRAME_WIDTH * FRAME_HEIGHT;
		int frameCount = (imgWidth * imgHeight) / PIXEL_PER_FRAME;
//			spritePixels = new int[frameCount][pixelPerFrame];
		int pixelPerFrameX = FRAME_WIDTH;
		int pixelPerFrameY = FRAME_HEIGHT;
		int frameCountX = imgWidth / pixelPerFrameX;
		int frameCountY = imgHeight / pixelPerFrameY;
		frames = new BufferedImage[frameCount];

		for (int x = 0; x < frameCountX; x++) {
			for (int y = 0; y < frameCountY; y++) {
				frames[x + y * frameCountX] = img.getSubimage(x * pixelPerFrameX, y * pixelPerFrameY,
						pixelPerFrameX, pixelPerFrameY);
			}
		}
	}

	/**
	 * Defines which frames are used for the specific state.
	 * 
	 * @param state
	 * @param frameNumbers
	 */
	public void addState(SpriteState state, int[] frameNumbers) {
		states.put(state, frameNumbers);
		assignedSpriteStates = ArrayOperations.add(assignedSpriteStates, state);
	}
	
	public boolean stateIsAssigned(SpriteState state) {
		return ArrayOperations.contains(assignedSpriteStates, state);
	}


	public BufferedImage getFrame(SpriteState spriteState, int spriteStateFrameNo) {
		int frameNo = states.get(spriteState)[spriteStateFrameNo];
		return frames[frameNo];
	}

	public int getFrameCount(SpriteState state) {
		return states.get(state).length;
	}
}
