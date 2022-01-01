package kane.genericGame.gameEvent.mob;

import java.awt.Event;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;

public class GumbaWalk extends GameEvent{
	
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
			if (walker)
		}
		
		reduceFrameCounter();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
