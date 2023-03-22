package kane.renderer;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL46.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.stb.STBImage;

import kane.math.ArrayOperations;
import kane.math.ImageAlteration;

public class Sprite {
	// This class manages sprites.

	public static final float SCALE = 2f;

	public final int FRAME_WIDTH;
	public final int FRAME_HEIGHT;
	public int PIXEL_PER_FRAME;
//	private int[][] spritePixels;
	private ByteBuffer[] frames;
	protected SpriteState[] assignedSpriteStates;

	public final int TEXTURE_ID;

	private Map<SpriteState, int[]> states;

	public Sprite(BufferedImage img, int frameWidth, int frameHeight) {
		assignedSpriteStates = new SpriteState[0];
		this.FRAME_HEIGHT = frameHeight;
		this.FRAME_WIDTH = frameWidth;
		this.states = new HashMap<SpriteState, int[]>();

		TEXTURE_ID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D_ARRAY, TEXTURE_ID);
		init(img);

	}

	public Sprite(File file, int frameWidth, int frameHeight) {
		assignedSpriteStates = new SpriteState[0];
		this.FRAME_HEIGHT = frameHeight;
		this.FRAME_WIDTH = frameWidth;
		this.states = new HashMap<SpriteState, int[]>();
		
		TEXTURE_ID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D_ARRAY, TEXTURE_ID);
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			init(img);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void init(BufferedImage img) {
		int imgWidth = img.getWidth(null);
		int imgHeight = img.getHeight(null);
		this.PIXEL_PER_FRAME = FRAME_WIDTH * FRAME_HEIGHT;
		int frameCount = (imgWidth * imgHeight) / PIXEL_PER_FRAME;
//			spritePixels = new int[frameCount][pixelPerFrame];
		int frameCountX = imgWidth / FRAME_WIDTH;
		int frameCountY = imgHeight / FRAME_HEIGHT;
		frames = new ByteBuffer[frameCount];

		for (int x = 0; x < frameCountX; x++) {
			for (int y = 0; y < frameCountY; y++) {
				frames[x + y * frameCountX] = ImageAlteration.bufferedImageToByteBuffer(
						img.getSubimage(x * FRAME_WIDTH, y * FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT));
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, FRAME_WIDTH, FRAME_HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE,
						frames[x + y * frameCountX]);
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

	public ByteBuffer getFrame(SpriteState spriteState, int spriteStateFrameNo) {
		int frameNo = states.get(spriteState)[spriteStateFrameNo];
		return frames[frameNo];
	}

	public int getFrameCount(SpriteState state) {
		return states.get(state).length;
	}
}
