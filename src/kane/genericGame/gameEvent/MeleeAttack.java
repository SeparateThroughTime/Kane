package kane.genericGame.gameEvent;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.PassiveAttributes;
import kane.physics.Shape;

public class MeleeAttack extends GameEvent {

	protected Mob attacker;
	protected Shape attackShape;

	public MeleeAttack(Game g, int eventDuration, Mob attacker) {
		super(g, eventDuration);
		this.attacker = attacker;
	}

	@Override
	public void start() {
		attackShape = attacker.getShape(PassiveAttributes.ATTACKING_FIELD);
		attackShape.addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
		
		procedure();
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void end() {
		attackShape.remActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
	}

}
