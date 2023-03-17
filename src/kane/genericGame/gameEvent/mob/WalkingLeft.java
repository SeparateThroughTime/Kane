package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.MobDirection;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class WalkingLeft extends GameEvent {

	private Mob walker;

	public WalkingLeft(Mob walker) {
		super(2);
		this.walker = walker;
	}

	@Override
	public void start() {
		// Checks if the player changed direction. If so, the body turns.
		if (ArrayOperations.contains(SpriteController.RIGHT_SPRITE_STATES,
				walker.getShape(PassiveAttributes.MOB_ALL).getCurrentSpriteState())) {
			walker.mirrorX();
		}

		walker.getShape(PassiveAttributes.MOB_ALL).setCurrentSpriteState(SpriteState.RUNNING_LEFT);
		walker.angle = 0f;
		walker.setDirection(MobDirection.LEFT);
		walker.getActiveActions().put(MobActions.WALK, true);
		walker.getActiveActions().put(MobActions.STAND, false);
	}

	@Override
	public void procedure() {
		walker.acc.x = -walker.getWalkAcc().x;
		if (-walker.vel.x > walker.getWalkSpeed()) {
			walker.vel.x = -walker.getWalkSpeed();
		}

		reduceFrameCounter();
	}

	@Override
	public void end() {
		walker.getShape(PassiveAttributes.MOB_ALL).setCurrentSpriteState(SpriteState.STANDING_LEFT);
		walker.getActiveActions().put(MobActions.WALK, false);
		walker.getActiveActions().put(MobActions.STAND, true);
	}

}
