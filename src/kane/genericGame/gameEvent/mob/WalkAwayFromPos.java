package kane.genericGame.gameEvent.mob;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.math.Vec2f;

public class WalkAwayFromPos extends GameEvent{

	private Mob mob;
	private Vec2f pos;
	
	public WalkAwayFromPos(Game g, Mob mob, Vec2f pos) {
		super(g, 1);
		this.mob = mob;
		this.pos = pos;
	}
	
	@Override
	public void start() {
		float direction = pos.getX() - mob.getPos().getX();
		if(direction < 0) {
			mob.walkRight();
		}
		else {
			mob.walkLeft();
		}
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void end() {
		
	}

}
