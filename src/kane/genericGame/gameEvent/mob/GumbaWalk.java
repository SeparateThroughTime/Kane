package kane.genericGame.gameEvent.mob;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.PassiveAttributes;

public class GumbaWalk extends GameEvent {

	private Mob walker;
	private boolean leftWalk;
	private GameEvent currentWalkEvent;

	public GumbaWalk(Game g, Mob walker) {
		super(g, 2);
		this.walker = walker;
	}

	@Override
	public void start() {
		currentWalkEvent = new WalkingLeft(g, walker);
		g.addEvent(currentWalkEvent);
		leftWalk = true;

		procedure();

	}

	@Override
	public void procedure() {
		if (leftWalk) {
			if (walker.getShape(PassiveAttributes.MOB_LEFT).getColidedShapes(PassiveAttributes.PHYSICAL).length > 0) {
				leftWalk = false;
				currentWalkEvent.killEvent();
				currentWalkEvent = new WalkingRight(g, walker);
				g.addEvent(currentWalkEvent);
			}
		} else if (walker.getShape(PassiveAttributes.MOB_RIGHT)
				.getColidedShapes(PassiveAttributes.PHYSICAL).length > 0) {
			leftWalk = true;
			currentWalkEvent.killEvent();
			currentWalkEvent = new WalkingLeft(g, walker);
			g.addEvent(currentWalkEvent);
		}

		reduceFrameCounter();
	}

	@Override
	public void end() {
		currentWalkEvent.killEvent();

	}

}
