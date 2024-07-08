package kane.sound;

import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.system.MemoryUtil.*;

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
        // Part of initEngine, but Java...
        DEVICE = alcOpenDevice((ByteBuffer) null);
        if (DEVICE == NULL){
            throw new IllegalStateException("Failed to open an OpenAL device.");
        }

        int[] attributes = {0};
        CONTEXT = alcCreateContext(DEVICE, attributes);
        alcMakeContextCurrent(CONTEXT);

        alcCapabilities = ALC.createCapabilities(DEVICE);
        alCapabilities = AL.createCapabilities(alcCapabilities);

        initEngine();

        soundListener = new SoundListener();

        soundSources = new SoundSource[MAX_SOUND_SOURCES];
        numSoundSources = 0;

        setDistanceAttenuationOptions();
    }

    private void setDistanceAttenuationOptions(){
        AL10.alDistanceModel(AL11.AL_INVERSE_DISTANCE_CLAMPED);

    }

    private void initEngine(){


        if (!alcCapabilities.OpenALC10){
            throw new IllegalStateException();
        }

        //        System.out.println("OpenALC10  : " + alcCapabilities.OpenALC10);
        //        System.out.println("OpenALC11  : " + alcCapabilities.OpenALC11);
        //        System.out.println("ALC_EXT_EFX: " + alcCapabilities.ALC_EXT_EFX);

        if (alcCapabilities.OpenALC11){
            List<String> devices = ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);
            if (devices == null){
                checkALCError(NULL);
            } else{
                for (int i = 0; i < devices.size(); i++){
                    System.out.println(i + ": " + devices.get(i));
                }
            }
        }

        String defaultDeviceSpecifier = Objects.requireNonNull(alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER));
        //        System.out.println("Default device: " + defaultDeviceSpecifier);
        //        System.out.println("ALC device specifier: " + alcGetString(DEVICE, ALC_DEVICE_SPECIFIER));

        long context = alcCreateContext(DEVICE, (IntBuffer) null);
        checkALCError(DEVICE);

        boolean useTLC = alcCapabilities.ALC_EXT_thread_local_context && alcSetThreadContext(context);
        if (!useTLC){
            if (!alcMakeContextCurrent(context)){
                throw new IllegalStateException();
            }
        }
        checkALCError(DEVICE);

        ALCapabilities caps = AL.createCapabilities(alcCapabilities, MemoryUtil::memCallocPointer);

        //        System.out.println("ALC_FREQUENCY     : " + alcGetInteger(DEVICE, ALC_FREQUENCY) + "Hz");
        //        System.out.println("ALC_REFRESH       : " + alcGetInteger(DEVICE, ALC_REFRESH) + "Hz");
        //        System.out.println("ALC_SYNC          : " + (alcGetInteger(DEVICE, ALC_SYNC) == ALC_TRUE));
        //        System.out.println("ALC_MONO_SOURCES  : " + alcGetInteger(DEVICE, ALC_MONO_SOURCES));
        //        System.out.println("ALC_STEREO_SOURCES: " + alcGetInteger(DEVICE, ALC_STEREO_SOURCES));
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
