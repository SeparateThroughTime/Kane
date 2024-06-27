package kane.genericGame.gameEvent.mob;

import kane.genericGame.GameEvent;
import kane.genericGame.Mob;
import kane.sound.SoundSource;
import kane.sound.SoundType;

public class Death extends GameEvent {

    private Mob mob;

    public Death(Mob mob) {
        super(1);
        this.mob = mob;
    }

    @Override
    public void start() {
        SoundSource soundSource = mob.getSoundSource(SoundType.DEATH);
        if (soundSource != null) {
            soundSource.play();
        }

        mob.remove();
    }

    @Override
    public void procedure() {

    }

    @Override
    public void end() {

    }
}
