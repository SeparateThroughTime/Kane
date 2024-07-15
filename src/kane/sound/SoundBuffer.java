package kane.sound;

import kane.exceptions.LoadSoundException;
import org.lwjgl.stb.STBVorbisInfo;

import java.nio.ShortBuffer;

import static kane.math.LwjglDemoSnippets.readVorbis;
import static org.lwjgl.openal.AL10.*;

public class SoundBuffer{
    private final int bufferId;

    public SoundBuffer(String filepath) throws LoadSoundException{
        this.bufferId = alGenBuffers();
        STBVorbisInfo info = STBVorbisInfo.malloc();
        try{
            ShortBuffer pcm = readVorbis(filepath, 32 * 1024, info);

            alBufferData(bufferId, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm,
                    info.sample_rate());
        } catch (Exception e){
            throw new LoadSoundException(filepath, e);
        }
    }


    public int getBufferId(){
        return this.bufferId;
    }

    public void cleanup(){
        alDeleteBuffers(this.bufferId);
    }
}
