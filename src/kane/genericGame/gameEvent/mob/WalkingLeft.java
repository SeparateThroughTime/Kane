package kane.genericGame.gameEvent.mob;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.PassiveAttributes;
import kane.genericGame.userInteraction.Keys;
import kane.math.ArrayOperations;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class WalkingLeft extends GameEvent {

	private Mob walker;

	public WalkingLeft(Game g, Mob walker) {
		super(g, 2);
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
		walker.setAngle(0f);
		walker.getActiveActions().put(MobActions.WALK_LEFT, true);
		walker.getActiveActions().put(MobActions.STAND_LEFT, false);
		walker.getActiveActions().put(MobActions.STAND_RIGHT, false);
	}

	@Override
	public void procedure() {
		walker.getAcc().setX(-walker.getWalkAcc().getX());
		if (-walker.getVel().getX() > walker.getWalkSpeed()) {
			walker.getVel().setX(-walker.getWalkSpeed());
		}

		reduceFrameCounter();
	}

	@Override
	public void end() {
		walker.getShape(PassiveAttributes.MOB_ALL).setCurrentSpriteState(SpriteState.STANDING_LEFT);
		walker.getActiveActions().put(MobActions.WALK_LEFT, false);
		walker.getActiveActions().put(MobActions.STAND_LEFT, true);
	}

}
