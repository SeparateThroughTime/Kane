package kane.sound;

import static kane.sound.SoundEngine.SOUND;

import kane.math.Vec2f;

import static org.lwjgl.openal.AL10.*;

public class SoundSource {
    private final int sourceId;
    private int bufferId;
    private Vec2f pos;
    public boolean pauseOnMenu;
    public boolean currentlyPausingOnMenu;

    public SoundSource(boolean loop, boolean relative, boolean pauseOnMenu){
        sourceId = alGenSources();
        if (loop) {
            alSourcei(sourceId, AL_LOOPING, AL_TRUE);
        }

        if (relative) {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }

        this.pauseOnMenu = pauseOnMenu;
        SOUND.addSoundSource(this);
        currentlyPausingOnMenu = false;
    }

    public void setBuffer(int bufferId) {
        stop();
        alSourcei(sourceId, AL_BUFFER, bufferId);
    }

    public void setPos(Vec2f pos) {
        alSource3f(sourceId, AL_POSITION, pos.x, pos.y, 0f);
    }

    public void setVel(Vec2f vel) {
        alSource3f(sourceId, AL_VELOCITY, vel.x, vel.y, 0f);
    }

    public void setGain(float gain) {
        alSourcef(sourceId, AL_GAIN, gain);
    }

    public void play() {
        alSourcePlay(sourceId);
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause() {
        alSourcePause(sourceId);
    }

    public void stop() {
        alSourceStop(sourceId);
    }

    public void cleanUp() {
        stop();
        alDeleteSources(sourceId);
    }
}
