package kane.genericGame.gameEvent;

import kane.genericGame.ActiveAttributes;
import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
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
		// TODO: Potential Bug: If spritestate of shape 0 is static the attack would be always in right direction
		Shape[] spriteShapes = attacker.getSpriteShapes();
		previousSpriteStates = new SpriteState[spriteShapes.length];
		boolean leftAttack = ArrayOperations.contains(SpriteController.LEFT_SPRITE_STATES,
				spriteShapes[0].getCurrentSpriteState());
		for (int i = 0; i < spriteShapes.length; i++) {
			previousSpriteStates[i] = spriteShapes[i].getCurrentSpriteState();
			if (leftAttack) {
				spriteShapes[i].setCurrentSpriteState(SpriteState.ATTACK_LEFT);
			} else {
				spriteShapes[i].setCurrentSpriteState(SpriteState.ATTACK_RIGHT);
			}
			
		}

		attackShape = attacker.getShape(PassiveAttributes.ATTACKING_FIELD);
		attackShape.addActiveAttribute(ActiveAttributes.ATTACKING_FIELD);

		procedure();
	}

	@Override
	public void procedure() {

	}

	@Override
	public void end() {
		Shape[] spriteShapes = attacker.getSpriteShapes();
		for (int i = 0; i < spriteShapes.length; i++) {
			SpriteState spriteState = previousSpriteStates[i];
			spriteShapes[i].setCurrentSpriteState(spriteState);
		}
		
		attackShape.remActiveAttribute(ActiveAttributes.ATTACKING_FIELD);
	}

}
