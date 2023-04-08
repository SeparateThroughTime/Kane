package kane.renderer;

import kane.math.Vec2f;

public class SpriteController {
	public static final int ANIMATION_RATE = 10;
	public static final SpriteState[] LEFT_SPRITE_STATES = { SpriteState.ATTACK_LEFT, SpriteState.RUNNING_LEFT,
			SpriteState.STANDING_LEFT };
	public static final SpriteState[] RIGHT_SPRITE_STATES = { SpriteState.ATTACK_RIGHT, SpriteState.RUNNING_RIGHT,
			SpriteState.STANDING_RIGHT };

	public SpriteState currentSpriteState;
	public int currentSpriteStateFrameNo;
	public Vec2f spritePosOffset;
	private int frameCounter;
	public float scale;

	public Sprite sprite;

	public SpriteController(Sprite sprite) {
		this.spritePosOffset = new Vec2f();
		this.sprite = sprite;
		this.scale = 1;
	}

	/**
	 * Returns the frame of a state with the specific frameNo.
	 * 
	 * @return
	 */
	public Vec2f[] getFrameTexCoords() {
		return sprite.getTexCoords(currentSpriteState, currentSpriteStateFrameNo);
	}

	public void setCurrentSpriteState(SpriteState state) {
		if (sprite.stateIsAssigned(state)) {
			this.currentSpriteState = state;
			this.currentSpriteStateFrameNo = 0;
			this.frameCounter = 0;
		}
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
		currentSpriteStateFrameNo++;
		if (currentSpriteStateFrameNo >= sprite.getFrameCount(currentSpriteState)) {
			currentSpriteStateFrameNo = 0;
		}
	}
}
