package kane.sound;

import static kane.renderer.ResolutionSpecification.RES_SPECS;
import static kane.sound.SoundEngine.SOUND;

import kane.math.Vec2f;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

public class SoundSource{
    private final int sourceId;
    public boolean pauseOnMenu;
    public boolean currentlyPausingOnMenu;

    private final float ROLL_OF_FACTOR;

    public SoundSource(boolean loop, boolean relative, boolean pauseOnMenu){
        ROLL_OF_FACTOR = RES_SPECS.halfGameWidth / 100;

        sourceId = alGenSources();
        if (loop){
            alSourcei(sourceId, AL_LOOPING, AL_TRUE);
        }

        if (relative){
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }

        this.pauseOnMenu = pauseOnMenu;
        SOUND.addSoundSource(this);
        currentlyPausingOnMenu = false;

        setDistanceAttenuationOptions();
    }

    private void setDistanceAttenuationOptions(){
        AL10.alSourcef(sourceId, AL_ROLLOFF_FACTOR, ROLL_OF_FACTOR);
        AL10.alSourcef(sourceId, AL_REFERENCE_DISTANCE, RES_SPECS.halfGameWidth * 1.2f);
        AL10.alSourcef(sourceId, AL_MAX_DISTANCE, RES_SPECS.gameWidth * 5);
    }

    public void setBuffer(int bufferId){
        stop();
        alSourcei(sourceId, AL_BUFFER, bufferId);
    }

    public void setPos(Vec2f pos){
        alSource3f(sourceId, AL_POSITION, pos.x, pos.y, 0f);
    }

    public void setGain(float gain){
        alSourcef(sourceId, AL_GAIN, gain);
    }

    public void play(){
        alSourcePlay(sourceId);
    }

    public boolean isPlaying(){
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause(){
        alSourcePause(sourceId);
    }

    public void stop(){
        alSourceStop(sourceId);
    }

    public void cleanUp(){
        stop();
        alDeleteSources(sourceId);
    }
}
