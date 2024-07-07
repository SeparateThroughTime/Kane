package kane.genericGame.gameEvent.mob;

import static kane.Kane.GAME;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.genericGame.MobActions;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class Jump extends GameEvent {

	Mob mob;

	public Jump(Mob mob) {
        super(2);
		this.mob = mob;
	}

	@Override
	public void start() {
        mob.putActiveActions(MobActions.JUMPING, true);

		mob.acc.add(mob.getJumpAcc());

        SoundSource soundSource = mob.getSoundSource(SoundType.JUMP);
        if (soundSource != null) {
            soundSource.play();
        }


	}

	@Override
	public void procedure() {
        if (mob.vel.y > 0){
            reduceFrameCounter();
        }
	}

	@Override
	public void end() {
        mob.putActiveActions(MobActions.JUMPING, false);
        GAME.addEvent(new Fall(mob));
	}

}
