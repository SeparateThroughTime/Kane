package kane.sound;

import org.lwjgl.stb.STBVorbisInfo;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static kane.math.LwjglDemoSnippets.readVorbis;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC11.*;

public class SoundBuffer {
    private final int bufferId;

    public SoundBuffer(String file) {
        this.bufferId = alGenBuffers();
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(file, 32 * 1024, info);

            // Copy to buffer
            alBufferData(bufferId, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
        }
    }


    public int getBufferId() {
        return this.bufferId;
    }

    public void cleanup() {
        alDeleteBuffers(this.bufferId);
    }
}
