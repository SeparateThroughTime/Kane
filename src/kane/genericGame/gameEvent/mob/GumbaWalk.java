package kane.genericGame.gameEvent.mob;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.genericGame.MobDirection;
import kane.genericGame.PassiveAttributes;

public class GumbaWalk extends GameEvent {

	private Mob walker;
	private GameEvent currentWalkEvent;

	public GumbaWalk(Game g, Mob walker) {
		super(g, 2);
		this.walker = walker;
	}

	@Override
	public void start() {
		walker.walkLeft();

	}

	@Override
	public void procedure() {
		if (walker.getDirection().equals(MobDirection.LEFT)) {
			if (walker.getShape(PassiveAttributes.MOB_LEFT).getColidedShapes(PassiveAttributes.PHYSICAL).length > 0) {
				walker.walkRight();
			}
		} else if (walker.getShape(PassiveAttributes.MOB_RIGHT)
				.getColidedShapes(PassiveAttributes.PHYSICAL).length > 0) {
			walker.walkLeft();
		}

		reduceFrameCounter();
	}

	@Override
	public void end() {
		currentWalkEvent.killEvent();

	}

}
