package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.MobDirection;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class WalkingRight extends GameEvent {

	private Mob walker;
    private SoundSource soundSource;

	public WalkingRight(Mob walker) {
		super(2);
		this.walker = walker;
	}

	@Override
	public void start() {
		// Checks if the player changed direction. If so, the body turns.
		if (ArrayOperations.contains(SpriteController.LEFT_SPRITE_STATES,
				walker.getShape(PassiveAttributes.MOB_ALL).getCurrentSpriteState())) {
			walker.mirrorX();
		}

		walker.getShape(PassiveAttributes.MOB_ALL).setCurrentSpriteState(SpriteState.RUNNING_RIGHT);
		walker.angle = 0f;
		walker.setDirection(MobDirection.RIGHT);
		walker.getActiveActions().put(MobActions.WALK, true);
		walker.getActiveActions().put(MobActions.STAND, false);

        soundSource = walker.getSoundSource(SoundType.WALK);
        if (soundSource != null) {
            soundSource.play();
        }
	}

	@Override
	public void procedure() {
		walker.acc.x = walker.getWalkAcc().x;
		if (walker.vel.x > walker.getWalkSpeed()) {
			walker.vel.x = walker.getWalkSpeed();
		}
		reduceFrameCounter();
	}

	@Override
	public void end() {
		walker.getShape(PassiveAttributes.MOB_ALL).setCurrentSpriteState(SpriteState.STANDING_RIGHT);
		walker.getActiveActions().put(MobActions.WALK, false);
		walker.getActiveActions().put(MobActions.STAND, true);

        if (soundSource != null) {
            soundSource.stop();
        }
	}

}
