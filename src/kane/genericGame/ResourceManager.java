package kane.genericGame;

import kane.exceptions.LoadShaderException;
import kane.exceptions.LoadSoundException;
import kane.exceptions.LoadTextureException;
import kane.renderer.Shader;
import kane.renderer.Texture;
import kane.sound.SoundBuffer;

import java.io.IOException;
import java.util.HashMap;

public class ResourceManager{

    public static ResourceManager RESOURCE_MANAGER = new ResourceManager();

    private final HashMap<String, Texture> sprites;
    private final HashMap<String, Shader> shaders;
    private final HashMap<String, SoundBuffer> soundBuffers;

    private ResourceManager(){
        sprites = new HashMap<>();
        shaders = new HashMap<>();
        soundBuffers = new HashMap<>();
    }

    public Texture getTexture(String filepath) throws LoadTextureException{

        if (!sprites.containsKey(filepath)){
            return loadTexture(filepath);
        } else{
            return sprites.get(filepath);
        }

    }

    private Texture loadTexture(String filepath) throws LoadTextureException{
        Texture texture;
        try{
            texture = new Texture(filepath);
            sprites.put(filepath, texture);
        } catch (IllegalArgumentException e){
            throw new LoadTextureException(filepath, e);
        }
        return texture;
    }

    public Shader getShader(String vertexFilepath, String fragmentFilepath) throws LoadShaderException{
        String key = vertexFilepath + fragmentFilepath;
        if (!shaders.containsKey(key)){
            return loadShader(vertexFilepath, fragmentFilepath, key);
        } else{
            return shaders.get(key);
        }
    }

    private Shader loadShader(String vertexFilepath, String fragmentFilepath, String key) throws LoadShaderException{
        Shader shader;
        try{
            shader = new Shader(vertexFilepath, fragmentFilepath);
        } catch (IOException e){
            throw new LoadShaderException(vertexFilepath, fragmentFilepath, e);
        }
        shaders.put(key, shader);
        return shader;
    }

    public SoundBuffer getSoundBuffer(String soundFilepath) throws LoadSoundException{
        if (!soundBuffers.containsKey(soundFilepath)){
            return loadSoundBuffer(soundFilepath);
        } else{
            return soundBuffers.get(soundFilepath);
        }
    }

    private SoundBuffer loadSoundBuffer(String soundFilepath) throws LoadSoundException{
        SoundBuffer soundBuffer = new SoundBuffer(soundFilepath);
        soundBuffers.put(soundFilepath, soundBuffer);
        return soundBuffer;
    }
}
