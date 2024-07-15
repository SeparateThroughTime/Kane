package kane.sound;

import org.lwjgl.openal.*;

import java.nio.ByteBuffer;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundEngine{
    public static SoundEngine SOUND;

    private final long DEVICE;
    private final long CONTEXT;
    private final ALCCapabilities alcCapabilities;
    private final ALCapabilities alCapabilities;
    public final SoundListener soundListener;

    public SoundSource[] soundSources;
    public static final int MAX_SOUND_SOURCES = 1000;
    public int numSoundSources;

    private SoundEngine(){
        DEVICE = alcOpenDevice((ByteBuffer) null);
        if (DEVICE == NULL){
            throw new IllegalStateException("Failed to open an OpenAL device.");
        }

        int[] attributes = {0};
        CONTEXT = alcCreateContext(DEVICE, attributes);
        alcMakeContextCurrent(CONTEXT);

        alcCapabilities = ALC.createCapabilities(DEVICE);
        alCapabilities = AL.createCapabilities(alcCapabilities);

        checkALCError(DEVICE);

        soundListener = new SoundListener();

        soundSources = new SoundSource[MAX_SOUND_SOURCES];
        numSoundSources = 0;

        setDistanceAttenuationOptions();
    }

    private void setDistanceAttenuationOptions(){
        AL10.alDistanceModel(AL11.AL_INVERSE_DISTANCE_CLAMPED);

    }

    public static void initializeSound(){
        if (SOUND == null){
            SOUND = new SoundEngine();
        }
    }

    static void checkALCError(long device){
        int err = alcGetError(device);
        if (err != ALC_NO_ERROR){
            throw new RuntimeException(alcGetString(device, err));
        }
    }

    public void addSoundSource(SoundSource soundSource){
        soundSources[numSoundSources++] = soundSource;
    }

    public void clearSoundSources(){
        for (int i = 0; i < numSoundSources; i++){
            soundSources[i] = null;
        }
        numSoundSources = 0;
    }

    public void pause(){
        for (int i = 0; i < numSoundSources; i++){
            SoundSource soundSource = soundSources[i];
            if (soundSource.pauseOnMenu && soundSource.isPlaying()){
                soundSource.pause();
                soundSource.currentlyPausingOnMenu = true;
            }
        }
    }

    public void resume(){
        for (int i = 0; i < numSoundSources; i++){
            SoundSource soundSource = soundSources[i];
            if (soundSource.currentlyPausingOnMenu){
                soundSource.play();
                soundSource.currentlyPausingOnMenu = false;
            }
        }
    }

    public void exit(){
        alcDestroyContext(CONTEXT);
        alcCloseDevice(DEVICE);
    }
}
