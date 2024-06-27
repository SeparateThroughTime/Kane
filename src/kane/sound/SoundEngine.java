package kane.sound;

import kane.genericGame.ContactManagementInterface;
import kane.physics.Physics;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;

import static kane.renderer.Camera.CAMERA;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.system.MemoryUtil.*;

public class SoundEngine {
    public static SoundEngine SOUND;

    private final long DEVICE;
    private final ALCCapabilities capabilities;
    private final SoundListener soundListener;

    private SoundEngine() {
        // Part of initEngine, but Java...
        DEVICE = alcOpenDevice((ByteBuffer) null);
        if (DEVICE == NULL) {
            throw new IllegalStateException("Failed to open an OpenAL device.");
        }
        capabilities = ALC.createCapabilities(DEVICE);
        initEngine();

        soundListener = new SoundListener();
    }

    private void initEngine() {


        if (!capabilities.OpenALC10) {
            throw new IllegalStateException();
        }

        System.out.println("OpenALC10  : " + capabilities.OpenALC10);
        System.out.println("OpenALC11  : " + capabilities.OpenALC11);
        System.out.println("ALC_EXT_EFX: " + capabilities.ALC_EXT_EFX);

        if (capabilities.OpenALC11) {
            List<String> devices = ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);
            if (devices == null) {
                checkALCError(NULL);
            } else {
                for (int i = 0; i < devices.size(); i++) {
                    System.out.println(i + ": " + devices.get(i));
                }
            }
        }

        String defaultDeviceSpecifier = Objects.requireNonNull(alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER));
        System.out.println("Default device: " + defaultDeviceSpecifier);
        System.out.println("ALC device specifier: " + alcGetString(DEVICE, ALC_DEVICE_SPECIFIER));

        long context = alcCreateContext(DEVICE, (IntBuffer) null);
        checkALCError(DEVICE);

        boolean useTLC = capabilities.ALC_EXT_thread_local_context && alcSetThreadContext(context);
        if (!useTLC) {
            if (!alcMakeContextCurrent(context)) {
                throw new IllegalStateException();
            }
        }
        checkALCError(DEVICE);

        // TODO: What for?
        ALCapabilities caps = AL.createCapabilities(capabilities, MemoryUtil::memCallocPointer);

        System.out.println("ALC_FREQUENCY     : " + alcGetInteger(DEVICE, ALC_FREQUENCY) + "Hz");
        System.out.println("ALC_REFRESH       : " + alcGetInteger(DEVICE, ALC_REFRESH) + "Hz");
        System.out.println("ALC_SYNC          : " + (alcGetInteger(DEVICE, ALC_SYNC) == ALC_TRUE));
        System.out.println("ALC_MONO_SOURCES  : " + alcGetInteger(DEVICE, ALC_MONO_SOURCES));
        System.out.println("ALC_STEREO_SOURCES: " + alcGetInteger(DEVICE, ALC_STEREO_SOURCES));

//        try {
//            testPlayback();
//        } finally {
//            alcMakeContextCurrent(NULL);
//            if (useTLC) {
//                AL.setCurrentThread(null);
//            } else {
//                AL.setCurrentProcess(null);
//            }
//            memFree(caps.getAddressBuffer());
//
//            alcDestroyContext(context);
//            alcCloseDevice(DEVICE);
//        }
    }

    public static void initializeSound() {
        if (SOUND == null) {
            SOUND = new SoundEngine();
        }
    }

    static void checkALCError(long device) {
        int err = alcGetError(device);
        if (err != ALC_NO_ERROR) {
            throw new RuntimeException(alcGetString(device, err));
        }
    }

    static void checkALError() {
        int err = alGetError();
        if (err != AL_NO_ERROR) {
            throw new RuntimeException(alGetString(err));
        }
    }
}
