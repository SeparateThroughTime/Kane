package kane.genericGame.gameEvent.sound;

import kane.genericGame.GameEvent;
import kane.sound.SoundBuffer;
import kane.sound.SoundSource;

public class PlaySound extends GameEvent {

    private SoundSource soundSource;
    private boolean stop;

    public PlaySound(SoundBuffer soundBuffer, boolean loop, boolean pauseOnMenu){
        super(2);
        stop = false;
        soundSource = new SoundSource(loop, false, pauseOnMenu);
        soundSource.setBuffer(soundBuffer.getBufferId());
    }

    @Override
    public void start() {
        soundSource.play();
    }

    @Override
    public void procedure() {
        if (soundSource.isPlaying() && !stop) {
            reduceFrameCounter();
        }
    }

    @Override
    public void end() {
        soundSource.stop();
    }

    public void stopSound() {
        stop = true;
    }
}
