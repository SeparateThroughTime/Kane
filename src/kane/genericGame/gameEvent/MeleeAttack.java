package kane.genericGame.gameEvent;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.physics.Shape;

public class MeleeAttack extends GameEvent {

	private Mob attacker;

	public MeleeAttack(Game g, int eventDuration, Mob attacker) {
		super(g, eventDuration);
		this.attacker = attacker;
	}

	@Override
	public void start() {
		
		procedure();
	}

	@Override
	public void procedure() {
		if (attacker.getAngle() == 0) {
			Shape attackShape = attacker.getShape(ActiveAttributes.ATTACK_RIGHT);
			attackShape.addActiveAttribute(ActiveAttributes.ATTACKING);
			Shape otherSide = attacker.getShape(ActiveAttributes.ATTACK_LEFT);
			otherSide.remActiveAttribute(ActiveAttributes.ATTACKING);
		}
		else {
			Shape attackShape = attacker.getShape(ActiveAttributes.ATTACK_LEFT);
			attackShape.addActiveAttribute(ActiveAttributes.ATTACKING);
			Shape otherSide = attacker.getShape(ActiveAttributes.ATTACK_RIGHT);
			otherSide.remActiveAttribute(ActiveAttributes.ATTACKING);
		}

	}

	@Override
	public void end() {
		Shape attackRight = attacker.getShape(ActiveAttributes.ATTACK_RIGHT);
		attackRight.remActiveAttribute(ActiveAttributes.ATTACKING);
		Shape attackLeft = attacker.getShape(ActiveAttributes.ATTACK_LEFT);
		attackLeft.remActiveAttribute(ActiveAttributes.ATTACKING);

	}

}
