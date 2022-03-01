package kane.genericGame.gameEvent.mob;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.PassiveAttributes;
import kane.math.ArrayOperations;
import kane.physics.Shape;
import kane.renderer.SpriteController;
import kane.renderer.SpriteState;

public class MeleeAttack extends GameEvent {

	protected Mob attacker;
	protected Shape attackShape;
	protected SpriteState[] previousSpriteStates;

	public MeleeAttack(Game g, int eventDuration, Mob attacker) {
		super(g, eventDuration);
		this.attacker = attacker;
	}

	@Override
	public void start() {
		attacker.getActiveActions().put(MobActions.ATTACKING, true);
		if (attacker.getActiveActions().get(MobActions.WALK_LEFT)
				|| attacker.getActiveActions().get(MobActions.STAND_LEFT)) {
			attacker.setCurrentSpriteState(SpriteState.ATTACK_LEFT);
		} else {
			attacker.setCurrentSpriteState(SpriteState.ATTACK_RIGHT);

		}

		attackShape = attacker.getShape(PassiveAttributes.ATTACKING_FIELD);
		attackShape.addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
	}

	@Override
	public void procedure() {

	}

	@Override
	public void end() {
		attacker.getActiveActions().put(MobActions.ATTACKING, false);
		attacker.refreshSpriteStates();

		attackShape.remActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
	}

}
