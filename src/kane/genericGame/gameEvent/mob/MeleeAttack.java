package kane.genericGame.gameEvent.mob;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.MobDirection;
import kane.genericGame.PassiveAttributes;
import kane.physics.Shape;
import kane.renderer.SpriteState;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class MeleeAttack extends GameEvent {

	protected Mob attacker;
	protected Shape attackShape;
	protected SpriteState[] previousSpriteStates;

	public MeleeAttack(int eventDuration, Mob attacker) {
		super(eventDuration);
		this.attacker = attacker;
	}

	@Override
	public void start() {
        attacker.putActiveActions(MobActions.ATTACKING, true);

		attackShape = attacker.getShape(PassiveAttributes.ATTACKING_FIELD);
		attackShape.addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);

        SoundSource soundSource = attacker.getSoundSource(SoundType.ATACK);
        if (soundSource != null) {
            soundSource.play();
        }
	}

	@Override
	public void procedure() {

	}

	@Override
	public void end() {
        attacker.putActiveActions(MobActions.ATTACKING, false);

		attackShape.remActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
	}

}
