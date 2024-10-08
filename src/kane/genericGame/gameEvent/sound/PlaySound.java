package kane.genericGame.gameEvent.sound;

import kane.genericGame.GameEvent;
import kane.sound.SoundBuffer;
import kane.sound.SoundSource;

public class PlaySound extends GameEvent{

    private final SoundSource soundSource;
    private boolean stop;

    public PlaySound(SoundBuffer soundBuffer, boolean loop, boolean pauseOnMenu){
        super(2);
        stop = false;
        soundSource = new SoundSource(loop, true, pauseOnMenu);
        soundSource.setBuffer(soundBuffer);
    }

    @Override
    public void start(){
        soundSource.play();
    }

    @Override
    public void procedure(){
        if (soundSource.isPlaying() && !stop){
            reduceFrameCounter();
        }
    }

    @Override
    public void end(){
        soundSource.stop();
    }

    public void stopSound(){
        stop = true;
    }
}
