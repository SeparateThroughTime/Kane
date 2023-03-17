package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;

public class Jump extends GameEvent {

	Mob mob;

	public Jump(Mob mob) {
		super(0);
		this.mob = mob;
	}

	@Override
	public void start() {
		mob.acc.add(mob.getJumpAcc());

	}

	@Override
	public void procedure() {

	}

	@Override
	public void end() {

	}

}
