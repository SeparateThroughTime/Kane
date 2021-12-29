package kane.genericGame.gameEvent.camera;

import kane.genericGame.Game;
import kane.genericGame.GameEvent;
import kane.genericGame.Mob;

public class Jump extends GameEvent {

	Mob mob;

	public Jump(Game g, Mob mob) {
		super(g, 0);
		this.mob = mob;
	}

	@Override
	public void start() {
		mob.getAcc().add(mob.getJumpAcc());

	}

	@Override
	public void procedure() {

	}

	@Override
	public void end() {

	}

}
